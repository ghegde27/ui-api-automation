package org.framework.mobile;

public class Commands {

    private static String OPERATING_SYSTEM ;

    private static String ANDROID_HOME ;

    public  static boolean isMac(){
        return getOperatingSystem().startsWith("Mac");
    }

    public  static boolean isWindows(){
        return getOperatingSystem().startsWith("Windows");
    }

    public static String getOperatingSystem(){
        OPERATING_SYSTEM =  OPERATING_SYSTEM == null ? System.getenv("os.name") : OPERATING_SYSTEM;
        return OPERATING_SYSTEM;
    }

    public static String getAndroidHome(){

        if(ANDROID_HOME == null){
            ANDROID_HOME = System.getenv("ANDROID_HOME");
            if (ANDROID_HOME == null) throw new RuntimeException("Failed to find ANDROID_HOME, make sure the variable is set corectly");
        }
        return ANDROID_HOME;
    }
}
