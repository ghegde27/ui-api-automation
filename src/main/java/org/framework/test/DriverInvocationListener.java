package org.framework.test;

import org.framework.config.ConfigManager;
import org.framework.driver.core.DriverManager;
import org.framework.driver.factory.DriverFactory;
import org.framework.driver.mobile.AppiumServiceManager;
import org.framework.observability.*;
import org.framework.utils.LogManager;

import org.slf4j.Logger;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public final class DriverInvocationListener
        implements IInvokedMethodListener {

    private static final Logger LOG =
            LogManager.getLogger(
                    DriverInvocationListener.class
            );

    private static final ConfigManager CONFIG =
            ConfigManager.getInstance();

    // =========================================================
    // BEFORE INVOCATION
    // =========================================================

    @Override
    public void beforeInvocation(
            IInvokedMethod method,
            ITestResult result
    ) {

        try {

            ExecutionContext.initialize();

            LOG.info(
                    "Execution ID : {}",
                    ExecutionContext.getExecutionId()
            );

            if (!method.isTestMethod()) {
                return;
            }

            String testName =
                    result.getMethod().getMethodName();

            String className =
                    result.getTestClass().getName();

            String platform =
                    CONFIG.getString(
                            "platform",
                            "web"
                    );

            String executionType =
                    CONFIG.getString(
                            "execution.type",
                            "local"
                    );

            String browser =
                    result.getTestContext()
                            .getCurrentXmlTest()
                            .getParameter("browser");

            if (browser == null
                    || browser.isBlank()) {

                browser =
                        CONFIG.getString(
                                "browser",
                                "chrome"
                        );
            }

            String deviceId =
                    result.getTestContext()
                            .getCurrentXmlTest()
                            .getParameter("deviceId");

            String appiumMode =
                    CONFIG.getString(
                            "appium.mode",
                            "local"
                    );

            LOG.info(
                    "================================================="
            );

            LOG.info("🚀 STARTING TEST");

            LOG.info(
                    "Class       : {}",
                    className
            );

            LOG.info(
                    "Test        : {}",
                    testName
            );

            LOG.info(
                    "Platform    : {}",
                    platform
            );

            LOG.info(
                    "Execution   : {}",
                    executionType
            );

            LOG.info(
                    "Browser     : {}",
                    browser
            );

            LOG.info(
                    "Device ID   : {}",
                    deviceId
            );

            LOG.info(
                    "Appium Mode : {}",
                    appiumMode
            );

            LOG.info(
                    "Thread ID   : {}",
                    Thread.currentThread().getId()
            );

            LOG.info(
                    "================================================="
            );

            // =================================================
            // WEB EXECUTION
            // =================================================

            if (platform.equalsIgnoreCase("web")) {

                DriverFactory.createBrowser(browser);


                LOG.info(
                        "Web driver initialized successfully for browser: {}",
                        browser
                );

                boolean networkEnabled =
                        CONFIG.getBoolean(
                                "network.capture.enabled",
                                false
                        );

                if (
                        networkEnabled
                                && browser.equalsIgnoreCase("chrome")
                ) {

                    NetworkObserver.startCapture();
                }
            }

            // =================================================
            // MOBILE EXECUTION
            // =================================================

            else if (
                    platform.equalsIgnoreCase("android")
                            || platform.equalsIgnoreCase("ios")
            ) {

                DriverFactory.create(deviceId);

                LOG.info(
                        "Mobile driver initialized successfully for device: {}",
                        deviceId
                );
            }

            else {

                throw new RuntimeException(
                        "Unsupported platform: "
                                + platform
                );
            }

        } catch (Throwable t) {

            LOG.error(
                    "Critical failure in beforeInvocation",
                    t
            );

            throw new RuntimeException(t);
        }
    }

    // =========================================================
    // AFTER INVOCATION
    // =========================================================

    @Override
    public void afterInvocation(
            IInvokedMethod method,
            ITestResult result
    ) {

        try {

            if (!method.isTestMethod()) {
                return;
            }

            String testName =
                    result.getMethod().getMethodName();

            String status =
                    statusOf(result);

            long duration =
                    result.getEndMillis()
                            - result.getStartMillis();

            String deviceId =
                    DriverManager.getDeviceId();

            String browser =
                    DriverManager.getBrowser();

            String platform =
                    CONFIG.getString(
                            "platform",
                            "web"
                    );

            LOG.info(
                    "-------------------------------------------------"
            );

            LOG.info("🏁 FINISHED TEST");

            LOG.info(
                    "Test        : {}",
                    testName
            );

            LOG.info(
                    "Status      : {}",
                    status
            );

            LOG.info(
                    "Duration    : {} ms",
                    duration
            );

            LOG.info(
                    "Platform    : {}",
                    platform
            );

            LOG.info(
                    "Browser     : {}",
                    browser
            );

            LOG.info(
                    "Thread ID   : {}",
                    Thread.currentThread().getId()
            );

            LOG.info(
                    "-------------------------------------------------"
            );

            // =============================================
            // FAILURE HANDLING
            // =============================================

            if (ITestResult.FAILURE
                    == result.getStatus()) {

                try {

                    ObservabilityManager
                            .captureScreenshot(
                                    testName
                            );

                } catch (Exception e) {

                    LOG.warn(
                            "Failed to capture screenshot",
                            e
                    );
                }

                try {

                    ObservabilityManager
                            .captureBrowserLogs();

                } catch (Exception e) {

                    LOG.warn(
                            "Failed to capture browser logs",
                            e
                    );
                }

                try {

                    DriverManager.getDriver();
                    AllureUtils.attachScreenshot(
                            DriverManager.getDriver(),
                            testName
                    );

                } catch (Exception e) {

                    LOG.warn(
                            "Failed to attach screenshot to Allure",
                            e
                    );
                }

                try {

                    String category =
                            FailureClassifier.classify(
                                    result.getThrowable()
                            );

                    LOG.error(
                            "Failure Category: {}",
                            category
                    );

                } catch (Exception e) {

                    LOG.warn(
                            "Failed to classify failure",
                            e
                    );
                }
            }

            // =============================================
            // DRIVER CLEANUP
            // =============================================

            try {

                DriverManager.quit();

                LOG.info(
                        "Driver quit successfully"
                );

            } catch (Exception e) {

                LOG.warn(
                        "Error while quitting driver",
                        e
                );
            }

            try {

                NetworkObserver.stopCapture();

            } catch (Exception e) {

                LOG.warn(
                        "Failed to stop network observer",
                        e
                );
            }

            // =============================================
            // APPIUM CLEANUP
            // =============================================

            try {

                if (
                        platform.equalsIgnoreCase("android")
                                || platform.equalsIgnoreCase("ios")
                ) {

                    if (
                            deviceId != null
                                    && !deviceId.isBlank()
                    ) {

                        AppiumServiceManager.stop(
                                deviceId
                        );

                        LOG.info(
                                "Stopped Appium server for device {}",
                                deviceId
                        );
                    }
                }

            } catch (Exception e) {

                LOG.warn(
                        "Error while stopping Appium service",
                        e
                );
            }

        } catch (Throwable t) {

            LOG.error(
                    "Critical failure in afterInvocation",
                    t
            );

        } finally {

            // =============================================
            // EXECUTION CONTEXT CLEANUP
            // =============================================

            try {

                ExecutionContext.clear();

            } catch (Exception e) {

                LOG.warn(
                        "Failed to clear execution context",
                        e
                );
            }
        }
    }

    // =========================================================
    // STATUS
    // =========================================================

    private String statusOf(
            ITestResult result
    ) {

        switch (result.getStatus()) {

            case ITestResult.SUCCESS:
                return "PASS";

            case ITestResult.FAILURE:
                return "FAIL";

            case ITestResult.SKIP:
                return "SKIP";

            default:
                return "UNKNOWN";
        }
    }
}