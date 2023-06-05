import org.framework.driver.WebDriverFactory;
import org.framework.utils.LogManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.xml.XmlSuite;

import static org.framework.utils.LogManager.*;

public class BaseTest implements LogManager {

    private final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private final WebDriverFactory factory = new WebDriverFactory();

    private final Logger LOGGER = getLogger( BaseTest.class );

    @BeforeSuite
    public void configureSuite(ITestContext context){
        LOGGER.debug(  "Test configuration is in progress" );
        DRIVER.set(factory.getDriver());
        configureSuite(context.getCurrentXmlTest().getSuite());

    }


    @AfterSuite
    public void cleanUp(){
        factory.killDriver( DRIVER.get() );
    }

    public WebDriver getWebDriver(){
        return DRIVER.get() ;
    }

    private void configureSuite(XmlSuite suite){
        suite.addListener( ITestListener.class.getName()  );
        int MAX_THREAD_COUNT = 1;
        suite.setThreadCount(Math.min(MAX_THREAD_COUNT, suite.getThreadCount()));
    }



}
