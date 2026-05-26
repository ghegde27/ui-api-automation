package org.framework.synthetic.scheduler;

import org.framework.synthetic.web.LoginSyntheticMonitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SyntheticScheduler {

    public static void main(String[] args) {

        ScheduledExecutorService executor =
                Executors.newScheduledThreadPool(5);

        executor.scheduleAtFixedRate(
                () -> {

                    new LoginSyntheticMonitor()
                            .execute();

                },
                0,
                5,
                TimeUnit.MINUTES
        );
    }
}