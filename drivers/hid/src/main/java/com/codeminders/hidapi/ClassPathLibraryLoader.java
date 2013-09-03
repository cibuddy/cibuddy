package com.codeminders.hidapi;

import com.github.fommil.jni.JniLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


public class ClassPathLibraryLoader {

    /**
     * HID library paths and names.
     * 
     * These properties have been adapted from the original codeminders version
     * in order to actually find the binaries coming with this distribution. Also
     * two binaries for ARM7 (like in the cubox) and ARM6 (like in the Raspberry 
     * Pi) are added.
     */
    private static final String[] HID_LIB_NAMES = {
	        "/lib/linux/x86_64/libhidapi-jni.so",
	        "/lib/linux/x86_32/libhidapi-jni.so",
	        "/lib/osx/x86_64/libhidapi-jni.jnilib",
	        "/lib/osx/x86_32/libhidapi-jni.jnilib",
	        "/lib/win/x86_64/hidapi-jni.dll",
	        "/lib/win/x86_32/hidapi-jni.dll",
	        "/lib/linux/arm7/libhidapi-jni.so",
                "/lib/linux/arm6/libhidapi-jni.so"
	};

  private static final Logger log = LoggerFactory.getLogger(ClassPathLibraryLoader.class);

  // legacy: return true if the libraries are loaded
  // (it would be better to just throw the exception!)
	public static boolean loadNativeHIDLibrary() {
      try {
        JniLoader.load(HID_LIB_NAMES);
      } catch (ExceptionInInitializerError e) {
        log.error("failed to load from: " + Arrays.toString(HID_LIB_NAMES), e);
        return false;
      }
      return true;
    }

}
