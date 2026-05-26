package org.framework.pages;

import org.framework.utils.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import java.util.List;

public class MoneyControlHomePage extends BasePage{

    private static final Logger LOG =
            LogManager.getLogger(MoneyControlHomePage.class);

    public MoneyControlHomePage(WebDriver driver) {

        super(driver, "locators/moneycontrol.json");
    }

    public MoneyControlHomePage tapSearch() {
        ui.click(locator("search_icon"));
        return this;

    }

    public MoneyControlHomePage searchAndSelectStock(String stockName) {

        LOG.info("🔍 Searching and selecting stock: {}", stockName);

        ui.clearAndSendKeys(
                locator("search_input"),
                stockName
        );

        ui.waitUntilElementsPresent(
                locator("search_results"),
                20L
        );

        List<WebElement> results =
                ui.findAll(locator("search_results"),20L);

        LOG.info( "Total 📈 found  {} ", results.size() );

        for (WebElement row : results) {

            String text = row.getText().trim();
            LOG.info("📈 Found result: {}", text);

            if (text.contains("Hindalco")) {
                LOG.info("✅ Clicking stock: {}", text);
                row.click();
                return this;
            }
        }

        throw new AssertionError(
                "❌ Stock not found in search results: " + stockName
        );
    }
}
