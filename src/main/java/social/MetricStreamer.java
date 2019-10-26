package social;

import social.alert.Alert;
import social.monitor.Monitor;

public class MetricStreamer {
  private final Alerter alerter;
  private final Monitor[] monitors;

  MetricStreamer(final Alerter alerter, final Monitor[] monitors) {
    this.alerter = alerter;
    this.monitors = monitors;
  }

  public void monitor(Metric metric) {
    validateMetric(metric);
    for (Monitor monitor : monitors) {
      Alert[] alerts = monitor.monitor(metric);
      for (Alert alert : alerts) {
        alerter.alert(alert);
      }
    }
  }

  private void validateMetric(final Metric metric) {
    if (metric == null) {
      throw new NullPointerException("The metric can not be null.");
    }
    if (isStringNullOrEmpty(metric.component)) {
      throw new RuntimeException("The metric is required to have a valid component field.");
    }
    if (isStringNullOrEmpty(metric.server)) {
      throw new RuntimeException("The metric is required to have a valid server field.");
    }
    if (isStringNullOrEmpty(metric.service)) {
      throw new RuntimeException("The metric is required to have a valid service field.");
    }
    if (metric.active == null) {
      throw new RuntimeException("The metric is required to have a valid active field.");
    }
    if (metric.timestamp == null) {
      throw new RuntimeException("The metric is required to have a valid timestamp field.");
    }
    if (metric.value == null) {
      throw new RuntimeException("The metric is required to have a valid value field.");
    }
  }

  private boolean isStringNullOrEmpty(final String value) {
    return value == null || "".equals(value);
  }
}
