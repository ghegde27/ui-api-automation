package org.framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.framework.utils.LogManager.*;

public class WebUtils implements DriverFunctions,LogManager {

    private final Logger LOGGER = getLogger(this.getClass().getSimpleName());

    private final WebDriver driver;

    public WebUtils(WebDriver driver){
        this.driver = driver;

    }

    public void click(By by , long timeout){
        findElement( by,timeout ).click();
    }

    public void clickUsingXpath(String xpath){
        Objects.requireNonNull(xpath);
        LOGGER.info( "click on the xpath provided ********** {}",xpath );
        click(By.xpath(xpath));
    }


    public void click(By by){
         findElement( by,DEFAULT_TIMEOUT ).click();
    }
    public void sendKeys(By locator , String text){
        findElement( locator,DEFAULT_TIMEOUT ).sendKeys(text);
    }

    public void clickAndSendKeys(By locator,String text) {

        WebElement element = findElement( locator,DEFAULT_TIMEOUT );
        element.click();
        element.sendKeys(text);
    }

    public void scroll(){


    }


    public boolean isElementPresent(By by , long timeout){
        return findElement(by,timeout ).isDisplayed();

    }

    final long DEFAULT_POLLING_PERIOD = 5L;

    final long DEFAULT_TIMEOUT = 30L;

    public WebElement findElement(By by, long timeout) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(DEFAULT_POLLING_PERIOD))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
        LOGGER.info( "started looking for element ********** with timeout {}" , DEFAULT_TIMEOUT );
        return wait.until(driver1 -> driver1.findElement(by));

    }

    public WebElement findElement(By by){
        return findElement( by, DEFAULT_TIMEOUT );
    }

    public List<WebElement> findElements(By by, long timeout){
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(DEFAULT_POLLING_PERIOD))
                .ignoring(NoSuchElementException.class);
        LOGGER.info( "started looking for element ********** with timeout {}" , DEFAULT_TIMEOUT );
        return wait.until(driver1 -> driver1.findElements(by));

    }



  public boolean isElementDisplayed(By by) {
        return findElement(by,DEFAULT_TIMEOUT).isDisplayed();

  }

  public void selectDropDowns(WebElement element, String option){
        Select objSelect =new Select(element);
        objSelect.selectByVisibleText(option);
  }



}
