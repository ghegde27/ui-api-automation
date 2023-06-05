package org.framework.config;

import org.framework.utils.LogManager;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager extends ConfigUtils implements LogManager {

    Logger logger = LogManager.getLogger(ConfigManager.class.getSimpleName());

    Properties properties = new Properties();
    private static class ConfigLoader{

        static final ConfigManager CONFIGMANAGER = new ConfigManager();
    }
    private ConfigManager(){
        try {
            logger.info( "Loading config file and bringing up the framework" );
            loadConfig();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    private void loadConfig() throws FileNotFoundException {
        try {
            String RESOURCE_PATH = "config.properties";
            properties.load( ClassLoader.getSystemResourceAsStream(RESOURCE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public static ConfigManager getInstance(){
        return ConfigLoader.CONFIGMANAGER;

    }

    @Override
    public String getString(Object value) {
        return String.valueOf( this.properties.get( value ) );
    }

    @Override
    public Properties getAll() {
        return properties;
    }
}
