package org.framework.config;

import java.util.Properties;

public abstract class ConfigUtils {

    public String getString(Object value){
        return String.valueOf(value);

    }

    public boolean getBoolean(String value){

        return Boolean.getBoolean(value);
    }

    public abstract Properties getAll();
}
