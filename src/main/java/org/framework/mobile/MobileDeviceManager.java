package org.framework.mobile;

import com.fasterxml.jackson.core.type.TypeReference;
import org.framework.config.ConfigManager;
import org.framework.utils.JacksonProvider;
import org.framework.utils.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public abstract class MobileDeviceManager implements JacksonProvider , LogManager {

    static ConfigManager configManager = ConfigManager.getInstance();

    private static Logger logger = LoggerFactory.getLogger(MobileDeviceManager.class );


    public MobileDeviceManager(){


    }

    protected static Map<String,String> getDefaultCapabilities(){
      Map<String,String> capabilities;
        try {
            capabilities = OBJECT_MAPPER.readValue(new FileInputStream("/resources/capabilities.json"), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return capabilities;
    }

















    }

