package org.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DesktopHomePage extends BasePage {



    static {
        final String REPOSITORY_NAME = "src/test/resources/locators/home.json";
        locator = loadLocators( REPOSITORY_NAME );

    }
    protected DesktopHomePage(WebDriver driver) {
        super(driver);
    }

    public DesktopCategoryPage clickCategory(){
        webFunctions.click( getLocator("category"), 20 );
        webFunctions.click( getLocator("electronics"),20 );
        return AbstractBasePage.create( DesktopCategoryPage.class , driver );
    }


    public DesktopSearch searchForItem(String product ,String category){
        webFunctions.clickAndSendKeys( getLocator("searchFromHome"), product );
        WebElement searchCategory = webFunctions.findElement(getLocator("searchCategory"));
        searchCategory.click();
        webFunctions.selectDropDowns(searchCategory,category );
        webFunctions.click( getLocator("searchButton") );
        return AbstractBasePage.create( DesktopSearch.class,driver );
    }




}
