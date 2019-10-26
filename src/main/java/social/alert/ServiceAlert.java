package social.alert;

import social.Metric;

public class ServiceAlert extends Alert {
  public ServiceAlert(final Metric metric, final double threshold) {
    super(metric.timestamp, metric.component, threshold, metric.service);
  }

  @Override
  public String toString() {
    StringBuilder alertText = new StringBuilder();
    alertText.append("ALERT ");
    alertText.append("timestamp: ");
    alertText.append(timestamp);
    alertText.append(", component: ");
    alertText.append(component);
    alertText.append(", value: ");
    alertText.append(value);
    alertText.append(", service: ");
    alertText.append(service);
    return alertText.toString();
  }
}
