package org.framework.pages;

import org.framework.utils.LogManager;
import org.framework.utils.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import java.util.List;

public class TestingPracticePage extends BasePage {

    private final SeleniumUtils seleniumUtils ;

    private static final Logger LOG =
            LogManager.getLogger(MoneyControlHomePage.class);

    public TestingPracticePage(WebDriver driver) {

        super(driver, "locators/testing.json");
        this.seleniumUtils  = new SeleniumUtils(driver);
    }


    public void openPageAndViewTable(){
        driver.get("https://afd.calpoly.edu/web/sample-tables");
        WebElement titleBar = ui.findVisible(locator("title"), 20);
        SeleniumUtils.readTable(titleBar);

    }

    public void googleEngine() {

        driver.get("https://www.google.com/");
        seleniumUtils.mouseHover(locator("about") );
        System.out.println( driver.getCurrentUrl() );
        seleniumUtils.rightClick(locator("right"));
        System.out.println( driver.getCurrentUrl() );

    }


    public void googleSearch(String query) {

        driver.get("https://duckduckgo.com");
        WebElement searchBox = ui.findVisible(By.name("q"),10);

        // Enter search text

        searchBox.sendKeys(query);

        // Press ENTER

        searchBox.sendKeys(Keys.ENTER);
        // Get all search results

        List<WebElement> results =

                driver.findElements(

                        By.xpath("//h3")

                );

        System.out.println(

                "===================================="

        );

        System.out.println(

                "GOOGLE SEARCH RESULTS"

        );

        System.out.println(

                "===================================="

        );

        // Print results

        for (int i = 0;

             i < results.size();

             i++) {

            String resultText =

                    results.get(i)

                            .getText()

                            .trim();

            if (!resultText.isEmpty()) {

                System.out.println(

                        (i + 1)

                                + ". "

                                + resultText

                );


    }
    }

}


public void dragAndDropDemo(){

    driver.get("https://jqueryui.com/droppable/");

    driver.switchTo().frame(0);

// Drag and drop
    seleniumUtils.dragAndDrop(By.id("draggable"), By.id("droppable"));
}


}
