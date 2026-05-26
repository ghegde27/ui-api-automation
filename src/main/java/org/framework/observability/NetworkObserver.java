package org.framework.observability;

import org.framework.driver.core.DriverManager;
import org.framework.utils.LogManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;

import org.openqa.selenium.devtools.v139.network.Network;
import org.openqa.selenium.devtools.v139.network.model.Request;
import org.openqa.selenium.devtools.v139.network.model.Response;

import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class NetworkObserver {

    private static final Logger LOG =
            LogManager.getLogger(NetworkObserver.class);

    // =========================================================
    // DEVTOOLS STORAGE
    // =========================================================

    private static final ThreadLocal<DevTools> DEVTOOLS =
            new ThreadLocal<>();

    // =========================================================
    // NETWORK LOG BUFFER
    // =========================================================

    private static final ThreadLocal<List<String>> NETWORK_LOGS =
            ThreadLocal.withInitial(ArrayList::new);

    private NetworkObserver() {
    }

    // =========================================================
    // START NETWORK CAPTURE
    // =========================================================

    public static void startCapture() {

        try {

            WebDriver webDriver =
                    DriverManager.getDriver();

            if (webDriver == null) {

                LOG.warn(
                        "Cannot start network capture. Driver is null."
                );

                return;
            }

            if (!(webDriver instanceof ChromeDriver)) {

                LOG.warn(
                        "Network capture supports ChromeDriver only."
                );

                return;
            }

            ChromeDriver driver =
                    (ChromeDriver) webDriver;

            DevTools devTools =
                    driver.getDevTools();

            devTools.createSession();

            DEVTOOLS.set(devTools);

            devTools.send(
                    Network.enable(
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty()
                    )
            );

            LOG.info(
                    "✅ CDP Network monitoring started"
            );

            // =====================================================
            // REQUEST LISTENER
            // =====================================================

            devTools.addListener(
                    Network.requestWillBeSent(),
                    requestEvent -> {

                        try {

                            Request req =
                                    requestEvent.getRequest();

                            String url =
                                    req.getUrl();

                            // OPTIONAL FILTER
                            if (
                                    !isImportantRequest(url)
                            ) {
                                return;
                            }

                            String requestLog =
                                    "REQUEST => "
                                            + req.getMethod()
                                            + " | "
                                            + url;

                            NETWORK_LOGS
                                    .get()
                                    .add(requestLog);

                        } catch (Exception e) {

                            LOG.warn(
                                    "Failed to capture request",
                                    e
                            );
                        }
                    }
            );

            // =====================================================
            // RESPONSE LISTENER
            // =====================================================

            devTools.addListener(
                    Network.responseReceived(),
                    responseEvent -> {

                        try {

                            Response response =
                                    responseEvent.getResponse();

                            String url =
                                    response.getUrl();

                            // OPTIONAL FILTER
                            if (
                                    !isImportantRequest(url)
                            ) {
                                return;
                            }

                            String responseLog =
                                    "RESPONSE => "
                                            + response.getStatus()
                                            + " | "
                                            + url;

                            NETWORK_LOGS
                                    .get()
                                    .add(responseLog);

                        } catch (Exception e) {

                            LOG.warn(
                                    "Failed to capture response",
                                    e
                            );
                        }
                    }
            );

        } catch (Exception e) {

            LOG.error(
                    "❌ Failed to start network monitoring",
                    e
            );
        }
    }

    // =========================================================
    // SAVE NETWORK LOGS
    // =========================================================

    public static void saveLogs(
            String testName
    ) {

        try {

            Path logPath =
                    Paths.get(
                            "logs/network/"
                                    + testName
                                    + ".log"
                    );

            Files.createDirectories(
                    logPath.getParent()
            );

            Files.write(
                    logPath,
                    NETWORK_LOGS.get()
            );

            LOG.info(
                    "✅ Network logs saved: {}",
                    logPath.toAbsolutePath()
            );

        } catch (Exception e) {

            LOG.error(
                    "❌ Failed to save network logs",
                    e
            );
        }
    }

    // =========================================================
    // STOP NETWORK CAPTURE
    // =========================================================

    public static void stopCapture() {

        try {

            DevTools devTools =
                    DEVTOOLS.get();

            if (devTools != null) {

                devTools.disconnectSession();

                LOG.info(
                        "🛑 CDP Network monitoring stopped"
                );
            }

        } catch (Exception e) {

            LOG.warn(
                    "Error while stopping network monitoring",
                    e
            );

        } finally {

            DEVTOOLS.remove();

            NETWORK_LOGS.remove();
        }
    }

    // =========================================================
    // FILTER IMPORTANT REQUESTS
    // =========================================================

    private static boolean isImportantRequest(
            String url
    ) {

        if (url == null) {
            return false;
        }

        return
                url.contains("/api/")
                        || url.contains("graphql")
                        || url.contains("rest")
                        || url.contains("service");
    }
}