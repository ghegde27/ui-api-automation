package org.framework.observability;

import org.framework.test.DriverInvocationListener;
import org.framework.utils.LogManager;
import org.slf4j.Logger;

import java.util.UUID;

public final class ExecutionContext {

    private static final Logger LOG =
            LogManager.getLogger(
                    ExecutionContext.class
            );

    private static final ThreadLocal<String> EXECUTION_ID =
            new ThreadLocal<>();

    private ExecutionContext() {}

    public static void initialize() {

        EXECUTION_ID.set(
                UUID.randomUUID().toString()
        );
    }

    public static String getExecutionId() {

        return EXECUTION_ID.get();
    }

    public static void clear() {
        LOG.info(
                "Execution ID : {} has been cleared",
                ExecutionContext.getExecutionId()
        );
        EXECUTION_ID.remove();
    }
}