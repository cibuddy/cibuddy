package com.codeminders.hidapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class to manually load the native libraries from the local class path.
 * 
 * This class is not required in an OSGi environment and thus is only located in
 * tests. Native library loading is done by the OSGi environment with the meta
 * data located in the <code>META-INF/manifest.mf</code> file instead.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class ClassPathLibraryLoader {

    private static final String[] HID_LIB_NAMES = {
        "/lib/linux/x86_64/libhidapi-jni.so",
        "/lib/linux/x86_32/libhidapi-jni.so",
        "/lib/osx/x86_64/libhidapi-jni.jnilib",
        "/lib/osx/x86_32/libhidapi-jni.jnilib",
        "/lib/win/x86_64/hidapi-jni.dll",
        "/lib/win/x86_32/hidapi-jni.dll"
    };

    public static boolean loadNativeHIDLibrary() {
        boolean isHIDLibLoaded = false;

        for (String path : HID_LIB_NAMES) {
            try {
                // have to use a stream
                InputStream in = ClassPathLibraryLoader.class.getResourceAsStream(path);
                if (in != null) {
                    try {
                        // always write to different location
                        String tempName = path.substring(path.lastIndexOf('/') + 1);
                        File fileOut = File.createTempFile(tempName.substring(0, tempName.lastIndexOf('.')), tempName.substring(tempName.lastIndexOf('.'), tempName.length()));
                        fileOut.deleteOnExit();

                        OutputStream out = new FileOutputStream(fileOut);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        Runtime.getRuntime().load(fileOut.toString());
                        isHIDLibLoaded = true;
                    } finally {
                        in.close();
                    }
                }
            } catch (Exception e) {
                // ignore
            } catch (UnsatisfiedLinkError e) {
                // ignore
            }

            if (isHIDLibLoaded) {
                break;
            }
        }

        return isHIDLibLoaded;
    }
}
