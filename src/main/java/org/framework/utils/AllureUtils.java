package org.framework.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class AllureUtils implements LogManager {

    private static  final Logger LOGGER = LogManager.getLogger( AllureUtils.class );

    /*
    Taking the screenshot when any test fails and attach it to Allure report
    @param
     driver : Webdriver instance
     methodName : Failed test name
    */
    @Attachment(type = "image/png")
    @Step()
    public static void screenshot(WebDriver driver, String methodName)/* throws IOException */ {
        Objects.requireNonNull(driver);
        try {
            File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile( screen, new File("logs/" + methodName + "-" + LocalDate.now() + ".png") );
            Allure.addAttachment( screen.getName(), FileUtils.openInputStream(screen) );
        }
        catch (IOException e) {
            LOGGER.error( "Driver might have closed ***** {}",e.getCause().toString());
            throw new RuntimeException(e);
        } catch (WebDriverException  w) {
            LOGGER.error( "Driver might have closed ***** {}",w.getCause().toString());
        }
    }
}

