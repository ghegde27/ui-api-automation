package org.framework.pages;

import org.framework.config.ConfigManager;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class AbstractBasePage<T> {

    /*
      Creating the instance of Pages where all the page related functions are created.
      This method is created to achieve abstraction on home pages are instantiated .

     */

    public static <T> T create( Class< ? super T > clz,  WebDriver  driver) {
        Objects.requireNonNull(driver);
        Objects.requireNonNull(clz);
        try {
            Constructor<T> constructor = (Constructor<T>) clz.getDeclaredConstructor( WebDriver.class );
            constructor.setAccessible(true);
            if (isConcreteClass(clz) ) return constructor.newInstance(driver);
            else throw new IllegalAccessException("Class cant be instantiated");
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }



    private static boolean isConcreteClass(Class c) {
       int mod =  c.getModifiers();
        return !Modifier.isAbstract(mod) && !Modifier.isInterface(mod) && !Modifier.isFinal(mod);
    }

}
