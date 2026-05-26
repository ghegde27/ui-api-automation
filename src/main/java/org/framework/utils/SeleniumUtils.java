package org.framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeleniumUtils {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SeleniumUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    // =========================================================
    // BROWSER ACTIONS
    // =========================================================

    public void openUrl(String url) {
        driver.get(url);
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public void navigateForward() {
        driver.navigate().forward();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void closeBrowser() {
        driver.quit();
    }

    // =========================================================
    // FIND ELEMENTS
    // =========================================================

    public WebElement findElement(By locator) {
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
    }

    public List<WebElement> findElements(By locator) {
        return wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(locator)
        );
    }

    // =========================================================
    // CLICK ACTIONS
    // =========================================================

    // LEFT CLICK
    public void leftClick(By locator) {
        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );

        element.click();
    }

    // RIGHT CLICK
    public void rightClick(By locator) {
        WebElement element = findElement(locator);

        actions.contextClick(element).perform();
    }

    // DOUBLE CLICK
    public void doubleClick(By locator) {
        WebElement element = findElement(locator);

        actions.doubleClick(element).perform();
    }

    // JAVASCRIPT CLICK
    public void jsClick(By locator) {
        WebElement element = findElement(locator);

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "arguments[0].click();",
                element
        );
    }

    // =========================================================
    // MOUSE ACTIONS
    // =========================================================

    // MOUSE HOVER
    public void mouseHover(By locator) {
        WebElement element = findElement(locator);

        actions.moveToElement(element).pause(Duration.ofSeconds(1)).perform();
    }

    // DRAG AND DROP
    public void dragAndDrop(
            By sourceLocator,
            By targetLocator
    ) {

        WebElement source = findElement(sourceLocator);

        WebElement target = findElement(targetLocator);

        actions.dragAndDrop(source, target)
                .perform();
    }

    // CLICK AND HOLD
    public void clickAndHold(By locator) {

        WebElement element = findElement(locator);

        actions.clickAndHold(element)
                .perform();
    }

    // RELEASE
    public void release(By locator) {

        WebElement element = findElement(locator);

        actions.release(element)
                .perform();
    }

    // =========================================================
    // TEXTBOX ACTIONS
    // =========================================================

    public void enterText(By locator, String text) {

        WebElement element = findElement(locator);

        element.clear();

        element.sendKeys(text);
    }

    public void appendText(By locator, String text) {

        WebElement element = findElement(locator);

        element.sendKeys(text);
    }

    public String getText(By locator) {

        return findElement(locator).getText();
    }

    public String getAttribute(
            By locator,
            String attribute
    ) {

        return findElement(locator)
                .getAttribute(attribute);
    }

    // =========================================================
    // DROPDOWN ACTIONS
    // =========================================================

    public void selectByVisibleText(
            By locator,
            String text
    ) {

        Select select =
                new Select(findElement(locator));

        select.selectByVisibleText(text);
    }

    public void selectByValue(
            By locator,
            String value
    ) {

        Select select =
                new Select(findElement(locator));

        select.selectByValue(value);
    }

    public void selectByIndex(
            By locator,
            int index
    ) {

        Select select =
                new Select(findElement(locator));

        select.selectByIndex(index);
    }

    // =========================================================
    // CHECKBOX / RADIO BUTTON
    // =========================================================

    public void checkCheckbox(By locator) {

        WebElement checkbox = findElement(locator);

        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void uncheckCheckbox(By locator) {

        WebElement checkbox = findElement(locator);

        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void selectRadioButton(By locator) {

        leftClick(locator);
    }

    // =========================================================
    // SCROLL ACTIONS
    // =========================================================

    public void scrollToElement(By locator) {

        WebElement element = findElement(locator);

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "arguments[0].scrollIntoView(true);",
                element
        );
    }

    public void scrollToBottom() {

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "window.scrollTo(0, document.body.scrollHeight)"
        );
    }

    public void scrollToTop() {

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "window.scrollTo(0, 0)"
        );
    }

    // =========================================================
    // ALERT HANDLING
    // =========================================================

    public void acceptAlert() {

        wait.until(
                ExpectedConditions.alertIsPresent()
        );

        driver.switchTo()
                .alert()
                .accept();
    }

    public void dismissAlert() {

        wait.until(
                ExpectedConditions.alertIsPresent()
        );

        driver.switchTo()
                .alert()
                .dismiss();
    }

    public String getAlertText() {

        wait.until(
                ExpectedConditions.alertIsPresent()
        );

        return driver.switchTo()
                .alert()
                .getText();
    }

    // =========================================================
    // FRAME HANDLING
    // =========================================================

    public void switchToFrame(By locator) {

        WebElement frame = findElement(locator);

        driver.switchTo().frame(frame);
    }

    public void switchToDefaultContent() {

        driver.switchTo().defaultContent();
    }

    // =========================================================
    // WINDOW HANDLING
    // =========================================================

    public void switchToWindow(int index) {

        List<String> windows =
                new ArrayList<>(driver.getWindowHandles());

        driver.switchTo()
                .window(windows.get(index));
    }

    public int getWindowCount() {

        return driver.getWindowHandles().size();
    }

    // =========================================================
    // WAIT UTILITIES
    // =========================================================

    public void waitForVisibility(By locator) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public void waitForClickable(By locator) {

        wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    public void waitForInvisibility(By locator) {

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    // =========================================================
    // VALIDATIONS
    // =========================================================

    public boolean isDisplayed(By locator) {

        try {
            return findElement(locator)
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {

        return findElement(locator)
                .isEnabled();
    }

    public boolean isSelected(By locator) {

        return findElement(locator)
                .isSelected();
    }

    // =========================================================
    // SCREENSHOT
    // =========================================================

    public void takeScreenshot(String filePath) {

        TakesScreenshot ts =
                (TakesScreenshot) driver;

        File source = ts.getScreenshotAs(
                OutputType.FILE
        );

        source.renameTo(new java.io.File(filePath));
    }

    // =========================================================
    // TABLE UTILITIES
    // =========================================================

    public List<WebElement> getTableRows(
            By tableLocator
    ) {

        WebElement table =
                findElement(tableLocator);

        return table.findElements(By.tagName("tr"));
    }

    public String getTableCellValue(
            By tableLocator,
            int row,
            int column
    ) {

        WebElement table =
                findElement(tableLocator);

        String xpath =
                ".//tr[" + row + "]/td[" + column + "]";

        return table.findElement(
                By.xpath(xpath)
        ).getText();
    }

    public static void readTable(WebElement table) {

        List<WebElement> rows =
                table.findElements(By.tagName("tr"));

        for (int rowIndex = 0;
             rowIndex < rows.size();
             rowIndex++) {

            List<WebElement> columns =
                    rows.get(rowIndex)
                            .findElements(
                                    By.xpath("./th|./td")
                            );

            System.out.println(
                    "===== ROW "
                            + rowIndex
                            + " ====="
            );

            for (int colIndex = 0;
                 colIndex < columns.size();
                 colIndex++) {

                System.out.println(
                        "COLUMN "
                                + colIndex
                                + " : "
                                + columns.get(colIndex)
                                .getText()
                );
            }
        }
    }

}