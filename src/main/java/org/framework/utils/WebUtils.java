package org.framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public final class WebUtils {

    private static final Logger LOG =
            LogManager.getLogger(WebUtils.class);

    private final WebDriver driver;
    private final long defaultTimeout;
    private final long pollingInterval;

    public WebUtils(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = 30;        // later from ConfigManager
        this.pollingInterval = 500;      // milliseconds
    }

    // =========================
    // Click / Type
    // =========================
    public void click(By locator) {
        findVisible(locator, defaultTimeout).click();
    }

    public void click(By locator, long timeout) {
        findVisible(locator, timeout).click();
    }

    public void sendKeys(By locator, String text) {
        WebElement element = findVisible(locator, defaultTimeout);
        element.clear();
        element.sendKeys(text);
    }

    public void clickAndSendKeys(By locator, String text) {
        WebElement element = findVisible(locator, defaultTimeout);
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    // =========================
    // Finders
    // =========================
    public WebElement findVisible(By locator, long timeout) {
        LOG.debug("Waiting for element: {}", locator);

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingInterval))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(d -> {
            WebElement el = d.findElement(locator);
            return el.isDisplayed() ? el : null;
        });
    }

    public List<WebElement> findAll(By locator, long timeout) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingInterval))
                .ignoring(NoSuchElementException.class);

        return wait.until(d -> {
            List<WebElement> elements = d.findElements(locator);
            return elements.isEmpty() ? null : elements;
        });
    }

    // =========================
    // Checks
    // =========================
    public boolean isElementPresent(By locator, long timeout) {
        try {
            findVisible(locator, timeout);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementDisplayed(By locator) {
        return isElementPresent(locator, defaultTimeout);
    }

    // =========================
    // Dropdown
    // =========================
    public void selectByVisibleText(By locator, String option) {
        WebElement element = findVisible(locator, defaultTimeout);
        new Select(element).selectByVisibleText(option);
    }

    // =========================
    // Scroll
    // =========================
    public void scrollTo(By locator) {
        WebElement element = findVisible(locator, defaultTimeout);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }


    public void clearAndSendKeys(By locator, String text) {

        Objects.requireNonNull(locator, "Locator cannot be null");
        Objects.requireNonNull(text, "Text cannot be null");

        WebElement element = driver.findElement(locator);

        try {
            element.click();     // bring focus
            element.clear();
            element.sendKeys(text);
        } catch (InvalidElementStateException e) {
            // fallback for some Android fields
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            element.sendKeys(Keys.DELETE);
            element.sendKeys(text);
        }
    }


    public void waitUntilElementsPresent(By locator, long timeoutSeconds) {

        Objects.requireNonNull(locator, "Locator cannot be null");

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        wait.until(d -> {
            List<WebElement> elements = d.findElements(locator);
            return !elements.isEmpty();
        });
    }
}