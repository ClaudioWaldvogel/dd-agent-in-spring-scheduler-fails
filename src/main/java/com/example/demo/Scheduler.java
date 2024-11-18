package com.example.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Scheduler {

    //@PostConstruct // Uncomment to fix ClassNotFoundException in scheduled method
    public void init() {
        Span span = GlobalOpenTelemetry.get()
                .getTracer("test")
                .spanBuilder("test")
                .startSpan();

        span.setStatus(StatusCode.ERROR)
                .recordException(new RuntimeException("Test Exception"));

        span.end();

    }

    @Scheduled(initialDelayString = "500", fixedDelayString = "200")
    public void scheduled() {
        Span.current()
                .setStatus(StatusCode.ERROR)
                .recordException(new RuntimeException("Test Exception"));

        System.out.println("Scheduled task done!");

    }

}
