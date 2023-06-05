import org.framework.utils.AllureUtils;
import org.framework.utils.LogManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.ITestResult;

import static org.framework.utils.LogManager.getLogger;

public class ITestListener implements org.testng.ITestListener ,LogManager {

    private final Logger logger = getLogger(ITestListener.class.getSimpleName());

    @Override
    public void onTestFailure(ITestResult tr) {
        logger.info( "Taking screenshot and test is failed********* test name is {}", tr.getName());
        Object testClass = tr.getInstance();
        WebDriver driver = ((BaseTest) testClass).getWebDriver();
        AllureUtils.screenshot( driver,tr.getName() );
    }

}
