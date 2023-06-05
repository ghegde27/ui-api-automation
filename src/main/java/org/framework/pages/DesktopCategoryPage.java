package org.framework.pages;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import static org.framework.parser.LocatorFactory.getDynamicXpath;

public class DesktopCategoryPage extends BasePage {


    static {
        final String REPOSITORY_NAME = "src/test/resources/locators/category.json";
        locator = loadLocators( REPOSITORY_NAME );

    }
    public DesktopCategoryPage(WebDriver driver) {
        super(driver);

        this.driver = driver;
    }

    @Step("Click on selected category ")
    public DesktopCategoryPage clickOnMainCategory(){


        webFunctions.click(getLocator("cellphones"));
        webFunctions.click( getLocator("seeAll") );
        return this;
    }

    @Step("Filtering the categories by screen size")
    @Description("choose three filters - screen size")
    public DesktopCategoryPage filterByScreenSize(String filterName , String subFilterName){
        String fN = getLocatorValue("filter");
        String subFilter = getLocatorValue("subFilter");
        webFunctions.clickUsingXpath( getDynamicXpath( fN,REGEX, filterName ) );
        webFunctions.clickUsingXpath( getDynamicXpath(subFilter, REGEX,subFilterName ) );
        return this;
    }

    @Step("Filtering the categories by price")
    @Description("choose filter Price and price range")
    public DesktopCategoryPage doPriceFiltering(String min ,String max){
        String fN = getLocatorValue("filter");
        webFunctions.clickUsingXpath( getDynamicXpath( fN,REGEX, "Price" ) );
        final String minCurrency = "Minimum Value, US Dollar";
        final String maxCurrency = "Maximum Value, US Dollar";
        String price = getLocatorValue( "priceRange" );
        WebElement minPrice = webFunctions.findElement(By.xpath( getDynamicXpath( price,REGEX,minCurrency ) ));
        minPrice.sendKeys( min);
        WebElement maxPrice = webFunctions.findElement(By.xpath( getDynamicXpath( price,REGEX,maxCurrency ) ));
        maxPrice.sendKeys(max);
        return this;
    }

    @Step("Filtering the categories by location")
    @Description("choose filter by location")
    public DesktopCategoryPage filterByLocation(String filter ,String location){
        String fN = getLocatorValue("filter");
        webFunctions.clickUsingXpath( getDynamicXpath( fN,REGEX, filter ) );
        String itemLocation = getLocatorValue( "itemLocation" );
        webFunctions.clickUsingXpath( getDynamicXpath( itemLocation,REGEX, location ) );
        return this;
    }


    public DesktopCategoryPage applyFilter(){
        webFunctions.click( getLocator("applyFilter") );
        return this;
    }

    public void verifyAppliedFilters(String f1,String f2,String f3){
        webFunctions.click( getLocator("appliedFiltersLocator") );
        String appliedFilters = getLocatorValue("appliedFilters");
        Assert.assertTrue(webFunctions.isElementDisplayed(By.xpath(getDynamicXpath(appliedFilters,REGEX, f1))));
        Assert.assertTrue(webFunctions.isElementDisplayed(By.xpath(getDynamicXpath(appliedFilters,REGEX, f2))));
        Assert.assertTrue(webFunctions.isElementDisplayed(By.xpath(getDynamicXpath(appliedFilters,REGEX, f3))));

    }








}
