package org.framework.synthetic.web;

import org.framework.driver.core.DriverManager;
import org.framework.driver.factory.DriverFactory;
import org.framework.synthetic.metrics.MetricsPublisher;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginSyntheticMonitor {

    public void execute() {

        long start = System.currentTimeMillis();

        try {

            DriverFactory.createBrowser("chrome");

            WebDriver driver =
                    DriverManager.getDriver();

            driver.get("https://example.com/login");

            driver.findElement(By.id("username"))
                    .sendKeys("monitor_user");

            driver.findElement(By.id("password"))
                    .sendKeys("monitor_password");

            driver.findElement(By.id("loginBtn"))
                    .click();

            boolean success =
                    driver.getTitle()
                            .contains("Dashboard");

            long duration =
                    System.currentTimeMillis() - start;

            MetricsPublisher.RESPONSE_TIME
                    .observe(duration);

            if (success) {

                MetricsPublisher.TEST_PASS_COUNTER.inc();

            } else {

                MetricsPublisher.TEST_FAIL_COUNTER.inc();
            }

        } catch (Exception e) {

            MetricsPublisher.TEST_FAIL_COUNTER.inc();

            throw e;

        } finally {

            DriverManager.quit();
        }
    }
}