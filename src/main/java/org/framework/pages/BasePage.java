package org.framework.pages;

import com.fasterxml.jackson.core.type.TypeReference;
import org.framework.config.ConfigManager;
import org.framework.parser.LocatorFactory;
import org.framework.utils.JacksonProvider;
import org.framework.utils.LogManager;
import org.framework.utils.WebUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import static org.framework.utils.LogManager.*;

public class BasePage  implements LogManager, JacksonProvider {

    static Map<String, Map<String,String>> locator;
    WebDriver driver;

    private static final Logger LOGGER = getLogger(BasePage.class);
    WebUtils webFunctions ;

    final String REGEX = "{0}";

    ConfigManager configManager = ConfigManager.getInstance();

    protected BasePage(WebDriver driver){

        this.driver = driver;
        this.webFunctions = new WebUtils(driver);
    }




    protected static  Map<String,Map<String,String>> loadLocators(String fileName) {
        LOGGER.debug("Loading Object repository ************ and file name is {} ", fileName);
        Map<String,Map<String,String>> locators;
        try {
            File file = new File(fileName);
            locators =  OBJECT_MAPPER.readValue( file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return locators;
    }

    private static String fetchResourceFile(String file) {
        return new File(file).getAbsolutePath()  ;
    }


    protected By getLocator(String name){
        Objects.requireNonNull(name);
        LOGGER.debug("Looking for locator in Object repository ************ {}", name);
        return LocatorFactory.getLocator( locator.get( name ).get( configManager.getString( "platform" ) ));
    }

    protected String getLocatorValue(String name){
        Objects.requireNonNull(name);
        LOGGER.debug("Looking for locators value in Object repository ************ {}", name);
        return LocatorFactory.getLocatorValue(locator.get( name ).get( configManager.getString( "platform" ) ));

    }




}
