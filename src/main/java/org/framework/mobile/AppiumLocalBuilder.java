package org.framework.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.framework.utils.LogManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.framework.utils.LogManager.*;

public class AppiumLocalBuilder extends MobileDeviceManager  {

    private static AppiumDriverLocalService service;


    private static HashMap<String, URL> hosts;

    private static String deviceID;

    private static String unlockPackage = "io.appium.unlock";

    private static Logger logger = getLogger(AppiumLocalBuilder.class );

    private static AppiumDriverLocalService createService() throws MalformedURLException {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {

            service = AppiumDriverLocalService.buildDefaultService();
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File("/usr/local/bin/node"))
                    .withAppiumJS(new File("/Applications/Appium.app/Contents/Resources/node_modules/appium/build/lib/main.js"))
                    .withIPAddress( "127.0.0.1" )
                    .withArgument(GeneralServerFlag.ALLOW_INSECURE)
                    .withLogFile(new File( "target/" + deviceID + ".log"  ))
                    .usingPort(4723));

        }
        else if (osName.contains("Windows")) {
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingPort(4723)
                    .withIPAddress( "127.0.0.1" )
                    .withArgument(GeneralServerFlag.ALLOW_INSECURE)
                    .withLogFile(new File("target/"+deviceID+".log")));
        }
        else {
            logger.info("Unspecified OS found, Appium can't run");
        }
        return service;
    }



    private static String getRandomPort(int start,int end ){

        int port = ThreadLocalRandom.current().nextInt(start,end);
        if (isPortInUse(port)){
           return getRandomPort(start,end);

        } else {
            return String.valueOf(port);
        }
    }


    private static boolean isPortInUse(int port){
        try {
            new ServerSocket(port);
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private static Set <String> getAvailableDevices() {
        logger.info("Checking for available device:");
        Set<String> availableDevices = new HashSet<>();
        Set connectedDevices = AdbCommands.getConnectedDevices();
        for (Object connectedDevice : connectedDevices) {
            String device = connectedDevice.toString();
            ArrayList apps = new AdbCommands(device).getInstalledPackages();
            if (!apps.contains(unlockPackage)) availableDevices.add(device);
            else
                logger.info("Device: " + device + " has " + unlockPackage + "installed, it is already under testing");
        }
        if (availableDevices.size() == 0) throw new RuntimeException("No device is available for testing");
        return availableDevices;
    }


    public static AppiumDriver createDriver()  {
        Set<String> devices = getAvailableDevices();
        String configuredDevices = configManager.getString("appium.deviceId");
        if(devices.contains(configuredDevices)){
            try {
                createService().start();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return new AppiumDriver(service.getUrl(),capabilities());

        } else {

            throw new IllegalArgumentException("Connected device is not found :: Make sure device is connected");
        }


       /* for (String device : devices) {
            try {
                deviceID = device;
                LOGGER.info("Attempting to create new driver for device: " + device);
                LOGGER.info("- - - - - - - - Starting Appium Server- - - - - - - - ");
                createService().start();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }*/
    }


    private static DesiredCapabilities capabilities() {
        Map<String, String> defaultCapabilities = getDefaultCapabilities();
        final Properties properties = configManager.getAll();
        Set<String> uiMobileOptions = properties.keySet().stream().map(Object::toString).filter(s -> s.startsWith("appium"))
                .collect(Collectors.toSet());
        uiMobileOptions.stream().map(

                s -> defaultCapabilities.computeIfPresent(s, (k, v) -> properties.get(s).toString())
        );
        return new DesiredCapabilities(defaultCapabilities);
    }


    private static DesiredCapabilities getCaps(){
        return new DesiredCapabilities();

    }




    }


