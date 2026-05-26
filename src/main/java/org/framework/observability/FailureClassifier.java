package org.framework.observability;

public final class FailureClassifier {

    private FailureClassifier() {}

    public static String classify(Throwable t) {

        if (t == null) {
            return "UNKNOWN";
        }

        String message = t.getMessage();

        if (message == null) {
            return "UNKNOWN";
        }

        message = message.toLowerCase();

        if (message.contains("timeout")) {
            return "TIMEOUT";
        }

        if (message.contains("stale")) {
            return "STALE_ELEMENT";
        }

        if (message.contains("assert")) {
            return "ASSERTION_FAILURE";
        }

        if (message.contains("connection")) {
            return "INFRA_FAILURE";
        }

        return "APPLICATION_FAILURE";
    }
}