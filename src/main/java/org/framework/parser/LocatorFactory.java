package org.framework.parser;

import org.framework.utils.LogManager;
import org.openqa.selenium.By;
import org.slf4j.Logger;

import java.util.Objects;

import static org.framework.utils.LogManager.*;

public class LocatorFactory implements LogManager {

    private static final Logger LOGGER  = getLogger(LocatorFactory.class);

    public static By getLocator(String str){
        LOGGER.info( "Looking for the locator ********{}******* in the repository ",str );
        String[] strings = str.split("=",2);
        String strategy = strings[0].trim();
        String value = strings[1].trim();

        switch (strategy.toLowerCase().trim()) {
            case "xpath" :
                return By.xpath(value);

            case "id" :
              return By.id(value);

            default:
                throw new IllegalArgumentException( String.format( "Locators with key %s and value %s not found" ,strategy,value));

        }

    }

    public static String getLocatorValue(String str){
        return str.split("=",2)[1].trim();

    }

    public static String getDynamicXpath(String basePath , String replace , String target){
        Objects.requireNonNull(basePath);
        Objects.requireNonNull(replace);
        return basePath.replace( replace, target );
    }



}
