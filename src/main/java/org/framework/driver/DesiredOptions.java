package org.framework.driver;

import com.fasterxml.jackson.core.type.TypeReference;
import org.framework.utils.JacksonProvider;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.InflaterInputStream;

public interface DesiredOptions extends JacksonProvider {

    default DesiredCapabilities capabilities(){
        Map<String,?> caps;
        try {caps =  OBJECT_MAPPER.readValue( "/src/main/resources/capabilities.json", new TypeReference<Map<String,Map<String,String>>>() {
            });
            return new DesiredCapabilities(caps);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
