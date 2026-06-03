import org.framework.driver.core.DriverManager;
import org.framework.pages.PageFactory;
import org.framework.pages.TestingPracticePage;
import org.testng.annotations.Test;

public class SeleniumTests {


    @Test
    public void seleniumLearning() {
        var driver = DriverManager.getDriver();
        driver.get("https://www.google.com/");
        PageFactory.create( TestingPracticePage.class , driver)
               .googleSearch("Bengaluru");
    }

    @Test
    public void verifyDragAndDrop(){

        var driver = DriverManager.getDriver();
        driver.get("https://www.google.com/");
        PageFactory.create(TestingPracticePage.class, driver)
               .dragAndDropDemo();
    }

}
