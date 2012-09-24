package com.cibuddy.project.configuration.impl.installer;

import com.cibuddy.core.build.configuration.IConfigurationInstaller;
import com.cibuddy.core.build.configuration.IConfigurationService;
import com.cibuddy.project.configuration.impl.Activator;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenient default implementation with helper methods for the {@link ArtifactInstaller
 * } interface.
 * <p>
 * This abstract class provides you with most the required methods to get
 * started using the {@link ArtifactInstaller } interface. You basically only
 * have to overwrite 3 methods. 2 are required for type and package name
 * handling and on does the actual handling of the file content exposed as an
 * xml configuration.
 * </p>
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractXMLArtifactInstaller<T> implements ArtifactInstaller, IConfigurationInstaller {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractXMLArtifactInstaller.class);
    Map<URL, List<ServiceRegistration>> configuredURLs = new HashMap<URL, List<ServiceRegistration>>();

    abstract String getPackageName();

    abstract T getRootTypeInstance();

    abstract void handle(T t, URL url, Bundle bundle);

    @Override
    public final boolean canHandle(File file) {
        try {
            return canHandle(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            LOG.debug("Filepath not URL compatible", ex);
            return false;
        }
    }

    @Override
    public boolean canHandle(URL fileUrl) {
        if (fileUrl == null) {
            return false;
        } else if (fileUrl.getFile().endsWith(".xml")) {
            LOG.debug("Handle: " + fileUrl.getFile());
            try {
                // use this make sure to get the correct class loader
                T temp = getRootTypeInstance();
                ClassLoader cl = temp.getClass().getClassLoader();
                if (cl == null) {
                    // pretty bad. We have a bootstrap classloader. Not much we can do.
                    LOG.warn("Found BootClassLoader, trying the bundle class loader instead. This might not work! "
                            + "Better upgrade your JVM!");
                    cl = this.getClass().getClassLoader();
                }
                JAXBContext jc = JAXBContext.newInstance(getPackageName(), cl);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                // make sure the schema matches. Otherwise an exception will be raised.
                T t = (T) unmarshaller.unmarshal(fileUrl);
                return true;
            } catch (Exception ex) {
                // couldn't parse xml with unexpected schema... do nothing.
                // FIXME: this is nasty! in case this is not an xml according to the schema this log comment is wrong!
                LOG.debug("Problems setting up configuration file.", ex);
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public final void install(File file) throws Exception {
        LOG.debug("Handle Install: " + file.getAbsolutePath());
        install(file.toURI().toURL(), null);
    }

    @Override
    public void install(URL url, Bundle bundle) throws Exception {
        LOG.debug("Handle Install: " + url.getFile());
        try {
            T t = getXMLFromURL(url);
            handle(t, url, bundle);
        } catch (Exception e) {
            LOG.warn("Problems setting up configuration file.", e);
        }
    }
    
    @Override
    public void update(File file) throws Exception {
        // This could be more granular, but for now this is ok.
        LOG.debug("Handle Update: " + file.getAbsolutePath());
        update(file.toURI().toURL(), null);
    }
    
    @Override
    public void update(URL url, Bundle bundle) throws Exception {
        // This could be more granular, but for now this is ok.
        uninstall(url, bundle);
        install(url, bundle);
    }

    @Override
    public final void uninstall(File file) throws Exception {
        LOG.debug("Handle Uninstall: " + file.getAbsolutePath());
        uninstall(file.toURI().toURL(), null);
    }
    
    @Override
    public void uninstall(URL url, Bundle bundle) throws Exception {
        List<ServiceRegistration> srs = configuredURLs.get(url);
        if (srs != null && srs.size() > 0) {
            Iterator<ServiceRegistration> srsIterator = srs.iterator();
            while (srsIterator.hasNext()) {
                ServiceRegistration sr = srsIterator.next();
                try {
                    sr.unregister();
                } catch (Exception e) {
                    // do nothing
                }
            }
            configuredURLs.remove(url);
        }
    }

    T getXMLFromURL(URL url) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(getPackageName(), this.getClass().getClassLoader());
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (T) unmarshaller.unmarshal(url);

    }

    ServiceRegistration registerConfigurationAsService(IConfigurationService ics, URL f, Class clazz) {
        Hashtable<String, String> dict = new Hashtable<String, String>();
        return registerConfigurationAsService(ics, f, dict, new String[]{clazz.getName()}, Activator.getBundleContext());
    }

    ServiceRegistration registerConfigurationAsService(IConfigurationService ics, URL f, Hashtable<String, String> dict, String[] clazzes, BundleContext context) {
        if (dict == null) {
            dict = new Hashtable<String, String>();
        }
        safePut(dict, IConfigurationService.CONFIG_NAME, ics.getName());
        safePut(dict, IConfigurationService.CONFIG_SOURCE, f.getPath());
        return Activator.getBundleContext().registerService(clazzes, ics, dict);
    }
    
    void safePut(Dictionary dict, String key, Object value) {
        if (key != null && value != null) {
            dict.put(key, value);
        }
    }
}
