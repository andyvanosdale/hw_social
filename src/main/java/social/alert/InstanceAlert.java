package social.alert;

import social.Metric;

public class InstanceAlert extends Alert {
  private final String server;

  public InstanceAlert(final Metric metric) {
    super(metric.timestamp, metric.component, metric.value, metric.service);
    this.server = metric.server;
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
    alertText.append(", server: ");
    alertText.append(server);
    alertText.append(", service: ");
    alertText.append(service);
    return alertText.toString();
  }
}
