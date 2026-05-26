package org.framework.synthetic.metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

public final class MetricsPublisher {

    private MetricsPublisher() {}

    public static final Counter TEST_PASS_COUNTER =
            Counter.build()
                    .name("synthetic_test_pass_total")
                    .help("Total synthetic test passes")
                    .register();

    public static final Counter TEST_FAIL_COUNTER =
            Counter.build()
                    .name("synthetic_test_fail_total")
                    .help("Total synthetic test failures")
                    .register();

    public static final Histogram RESPONSE_TIME =
            Histogram.build()
                    .name("synthetic_response_time")
                    .help("Synthetic response times")
                    .register();
}