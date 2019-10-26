package social.monitor;

import social.alert.Alert;
import social.alert.ServiceAlert;

import java.util.HashMap;
import java.util.Map;

import social.Metric;

public class ServiceLoad5MinAvgExceeds4Monitor implements Monitor {
  private static final String COMPONENT = "Load5mAvg";
  private static final double THRESHOLD = 4.0;

  private final Map<String, Double> serviceServerCounts;
  private final Map<String, ServiceAlertState> serviceAlertStates;

  private int previousTimestamp;

  public ServiceLoad5MinAvgExceeds4Monitor(Map<String, Double> serviceServerCounts) {
    this.serviceServerCounts = serviceServerCounts;
    serviceAlertStates = new HashMap<>();
    previousTimestamp = 0;
  }

  @Override
  public Alert[] monitor(Metric metric) {
    if (!COMPONENT.equals(metric.component) || !metric.active) {
      return new ServiceAlert[0];
    }

    if (previousTimestamp < metric.timestamp) {
      // Optimization to save on garbage collection rather than clearing the full map and recreating the state objects
      serviceAlertStates.forEach((k, v) -> v.clear());
    }
    previousTimestamp = metric.timestamp;

    if (metric.value <= THRESHOLD) {
      return new ServiceAlert[0];
    }

    ServiceAlertState serviceAlertState = serviceAlertStates.get(metric.service);
    if (serviceAlertState == null) {
      serviceAlertState = new ServiceAlertState();
      serviceAlertStates.put(metric.service, serviceAlertState);
    }
    serviceAlertState.serversInAlertStateCount++;

    if (serviceAlertState.serversInAlertStateCount / serviceServerCounts.get(metric.service) <= 0.5
        || serviceAlertState.alertSent) {
      return new ServiceAlert[0];
    }

    serviceAlertState.alertSent = true;
    return new ServiceAlert[] { new ServiceAlert(metric, THRESHOLD) };
  }

  private static class ServiceAlertState {
    public double serversInAlertStateCount = 0.0;
    public boolean alertSent = false;

    public void clear() {
      serversInAlertStateCount = 0.0;
      alertSent = false;
    }
  }
}
