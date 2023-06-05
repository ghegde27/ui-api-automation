package org.framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.framework.browser.BrowserOptions;
import org.framework.mobile.AppiumLocalBuilder;
import org.framework.utils.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;

import static org.framework.utils.LogManager.*;

public class WebDriverFactory implements DriverManager<WebDriver>  ,LogManager{

    protected BrowserOptions options = new BrowserOptions(configManager);

    private final Logger logger = getLogger(this.getClass().getSimpleName() );

    @Override
    public WebDriver getDriver() {
        return initialiseDriver();
    }

   @Override
    public WebDriver initialiseDriver(){
      final String platform =   configManager.getString( "platform" ) ;
      logger.info( "Configuring driver and platform is  ******** {}",platform );
      switch ( platform.toLowerCase() ) {

          case "mweb":
          case "dweb":
              WebDriver webDriver  ;
              switch (configManager.getString( "browser" ).toLowerCase()){

                  case "chrome" :
                      WebDriverManager.chromedriver().setup();

                      webDriver = new ChromeDriver(options.chromeOptions());
                      break;
                  case "safari" :
                      WebDriverManager.safaridriver().setup();
                      webDriver = new SafariDriver();
                      break;
                  case "ie":
                      WebDriverManager.iedriver().setup();
                      webDriver = new InternetExplorerDriver();
                      break;
                  case "firefox" :
                      WebDriverManager.firefoxdriver().setup();;
                      webDriver = new FirefoxDriver();
                      break;
                  default:
                      throw new IllegalArgumentException("Browser not found");
              }
            return webDriver;
          case "android":
          case "ios":
              switch (platform.toLowerCase()){

                  case "android" :

                  case "ios" :
                      return AppiumLocalBuilder.createDriver();

              }
          default:
              throw new IllegalArgumentException("Platform not found");

      }

    }

    @Override
    public void killDriver( WebDriver driver) {
        logger.debug( "Closing the driver" );
        if (driver != null) driver.quit();

    }
}
