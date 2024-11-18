# Sample project to illustrate ClassNotFoundException in Spring scheduled method
This sample projects illustrates the issue with ClassNotFoundException when using DD Java with OpenTelemetry Api when recording exceptions with `Span.recordException` in a Spring scheduled method.

The exception will only be raised if `Span.recordException` was not invoked before the first call to the scheduled method.


Reproduce the error:
- Attach dd-agent.jar as jvm property
- Set env variable `DD_TRACE_OTEL_ENABLED=true`

The following exception will be raised:

```bash
java.lang.NoClassDefFoundError: datadog/opentelemetry/shim/trace/OtelSpanEvent
	at datadog.opentelemetry.shim.trace.OtelSpan.recordException(OtelSpan.java:123) ~[na:na]
	at io.opentelemetry.api.trace.Span.recordException(Span.java:337) ~[opentelemetry-api-1.43.0.jar:1.43.0]
	at com.example.demo.Scheduler.scheduled(Scheduler.java:32) ~[main/:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
	at org.springframework.scheduling.support.ScheduledMethodRunnable.runInternal(ScheduledMethodRunnable.java:130) ~[spring-context-6.1.13.jar:6.1.13]
	at org.springframework.scheduling.support.ScheduledMethodRunnable.lambda$run$2(ScheduledMethodRunnable.java:124) ~[spring-context-6.1.13.jar:6.1.13]
	at io.micrometer.observation.Observation.observe(Observation.java:499) ~[micrometer-observation-1.13.4.jar:1.13.4]
	at org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:124) ~[spring-context-6.1.13.jar:6.1.13]
	at datadog.trace.instrumentation.springscheduling.SpringSchedulingRunnableWrapper.run(SpringSchedulingRunnableWrapper.java:34) ~[na:na]
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54) ~[spring-context-6.1.13.jar:6.1.13]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:358) ~[na:na]
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: java.lang.ClassNotFoundException: datadog.opentelemetry.shim.trace.OtelSpanEvent
	... 17 common frames omitted
```


If we add a manual Span before the first call to the scheduled method, the exception will not be raised.




