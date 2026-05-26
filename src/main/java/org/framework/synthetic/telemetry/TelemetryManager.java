package org.framework.synthetic.telemetry;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

public final class TelemetryManager {

    private static final Tracer TRACER =
            GlobalOpenTelemetry.getTracer("synthetic-monitor");

    private TelemetryManager() {}

    public static Span startSpan(String name) {

        return TRACER.spanBuilder(name)
                .startSpan();
    }
}