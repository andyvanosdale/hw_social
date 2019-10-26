package social.alert;

public abstract class Alert {
  protected final int timestamp;
  protected final String component;
  protected final double value;
  protected final String service;

  Alert(final int timestamp, final String component, final double value, final String service) {
    this.timestamp = timestamp;
    this.component = component;
    this.value = value;
    this.service = service;
  }
}
