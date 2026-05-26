package org.framework.driver.mobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class AdbCommands {

    private final String deviceId;

    public AdbCommands(String deviceId) {
        this.deviceId = deviceId;
    }

    // =========================
    // Static (ADB Server)
    // =========================
    public static void startServer() {
        run("adb start-server");
    }

    public static void killServer() {
        run("adb kill-server");
    }

    public static Set<String> getConnectedDevices() {
        String output = run("adb devices");
        Set<String> devices = new HashSet<>();

        for (String line : output.split("\n")) {
            if (line.endsWith("\tdevice")) {
                devices.add(line.split("\t")[0]);
            }
        }
        return devices;
    }

    // =========================
    // Instance (Device-specific)
    // =========================
    public List<String> getInstalledPackages() {
        String output = run(adb("shell pm list packages"));
        return output.lines()
                .map(line -> line.replace("package:", "").trim())
                .collect(Collectors.toList());
    }

    public String getForegroundActivity() {
        return run(adb("shell dumpsys window windows | grep mCurrentFocus"));
    }

    public void clearAppData(String packageName) {
        run(adb("shell pm clear " + packageName));
    }

    public void forceStopApp(String packageName) {
        run(adb("shell am force-stop " + packageName));
    }

    public void installApp(String apkPath) {
        run(adb("install " + apkPath));
    }

    public void uninstallApp(String packageName) {
        run(adb("uninstall " + packageName));
    }

    public void reboot() {
        run(adb("reboot"));
    }

    public String getDeviceModel() {
        return run(adb("shell getprop ro.product.model"));
    }

    public String getAndroidVersion() {
        return run(adb("shell getprop ro.build.version.release"));
    }

    // =========================
    // Helpers
    // =========================
    private String adb(String command) {
        return "adb -s " + deviceId + " " + command;
    }

    private static String run(String command) {
        try {
            Process process = new ProcessBuilder(command.split(" "))
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                String output = reader.lines().collect(Collectors.joining("\n"));
                process.waitFor();
                return output.trim();
            }
        } catch (Exception e) {
            throw new RuntimeException("ADB command failed: " + command, e);
        }
    }
}