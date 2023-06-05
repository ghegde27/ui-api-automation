package org.framework.config;

public enum Platform {

    ANDROID("android"),
    IOS("ios"),

    MWEB("mobile"),

    DESKTOP("dweb");

    private String name ;
    private Platform(String name) {
        this.name = name;
    }


}
