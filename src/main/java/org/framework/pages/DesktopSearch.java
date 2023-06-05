package org.framework.pages;

import org.framework.utils.LogManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;

public class DesktopSearch extends BasePage implements LogManager {

    private  final Logger LOGGER = LogManager.getLogger( DesktopSearch.class );


    static {
        final String REPOSITORY_NAME = "src/test/resources/locators/search.json";
        locator = loadLocators( REPOSITORY_NAME );

    }
    protected DesktopSearch(WebDriver driver) {
        super(driver);
    }

    public void verifySearchedResult(String product){
        String productName = webFunctions.findElement( getLocator("searchedItems") ).getText();
        LOGGER.info( "Product searched is *****{} and first product found is *******{}", product,productName );
        Assert.assertTrue( productName.contains(product) );
    }
}
