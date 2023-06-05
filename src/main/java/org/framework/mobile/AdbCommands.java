package org.framework.mobile;

import org.framework.mobile.MobileDeviceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AdbCommands extends Commands {

    private String ID ;


    public AdbCommands(){

    }

    public AdbCommands(String deviceId){

        this.ID = deviceId;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public static void killServer() {
        command("adb-kill-server");
    }

    public static void startServer() {
        command("adb-start-server");
    }

    public String getForegroundActivity() {
        return command("adb -s" + ID + " shell dumpsys window windows | grep mCurrentFocus");
    }

    public String getAndroidVersionAsString() {
        String output = command("adb -s " + ID + " getprop ro.build.version.release");
        if (output.length() == 3) output += ".0";
        return output;
    }

    public int getAndroidVersion() {
        return Integer.parseInt(getAndroidVersionAsString().replaceAll("\\.", ""));
    }

    public ArrayList getInstalledPackages() {
        ArrayList packages = new ArrayList();
        String[] output = command("adb -s " + ID + " shell pm list packages").split("\n");
        for (String packageID : output) packages.add(packageID.replace("package:", "").trim());
        return packages;
    }

    public void openAppsActivity(String packageID, String activityID) {
        command("adb -s " + ID + " shell am start api.android.intent.category.LAUNCHER -a api.android.intent.action.MAIN -n " + packageID + "/" + activityID);
    }

    public void clearAppsData(String packageID) {
        command("adb -s " + ID + " shell pm clear " + packageID);
    }

    public void forceStopApp(String packageID) {
        command("adb -s " + ID + " shell am force-stop " + packageID);
    }

    public void installApp(String apkPath) {
        command("adb -s " + ID + " install " + apkPath);
    }

    public void uninstallApp(String packageID) {
        command("adb -s " + ID + " uninstall " + packageID);
    }

    public void clearLogBuffer() {
        command("adb -s " + ID + " shell -c ");
    }

    public void pushFileToDevice(String source, String target) {
        command("adb - s " + ID + " push " + source + " " + target);
    }

    public void pullFileFromDevice(String source, String target) {
        command("adb - s " + ID + " pull " + source + " " + target);
    }

    public void deleteFile(String target) {
        command("adb - s " + ID + " shell rm " + target);
    }

    public void moveFile(String source, String target) {
        command("adb - s " + ID + " shell mv " + source + " " + target);
    }

    public void takeScreenshot(String target) {
        command("adb - s " + ID + " shell screencap " + target);
    }

    public void rebootDevice() {
        command("adb - s " + ID + " reboot ");
    }

    public String getDeviceModel() {
        return command("adb - s " + ID + " shell getprop ro.product.model");
    }

    public String getDeviceSerialNumber() {
        return command("adb - s " + ID + " shell getprop ro.product.serialno");
    }

    public String getDeviceCarrier() {
        return command("adb - s " + ID + " shell getprop gsm.operator.alpha");
    }

    protected static Set getConnectedDevices(){

        Set devices = new HashSet();
        String output = command("adb devices");
        for (String line : output.split("\n")) {
            line = line.trim();
            if (line.endsWith("device")) devices.add(line.replace("device", "").trim());
        }
        return devices;

    }


    public static String command(String command){
        if(command.startsWith("adb")) command = command.replace("adb ", getAndroidHome()+"/platform-tools/adb ");
        else throw new RuntimeException("This method is designed to run ADB commands only!");
        //MyLogger.log.debug("Formatted ADB Command: "+command);
        String output = runCommand(command);
        //MyLogger.log.debug("Output of the ADB Command: "+output);
        if(output == null) return "";
        else return output.trim();
    }


    public static  String runCommand(String command){
        String output = null;
        try {
            Scanner scanner = null;
            try {
                Process pb = Runtime.getRuntime().exec(command);
                pb.waitFor();
                scanner = new Scanner(pb.getInputStream()).useDelimiter("\\A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (scanner.hasNext()) output = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
