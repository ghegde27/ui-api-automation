package org.framework.synthetic.alerts;

public final class SlackAlertManager {

    private SlackAlertManager() {}

    public static void sendFailureAlert(String message) {

        System.out.println("SLACK ALERT => " + message);
    }
}