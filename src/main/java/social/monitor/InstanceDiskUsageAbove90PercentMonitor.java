package social.monitor;

import social.Metric;
import social.alert.Alert;
import social.alert.InstanceAlert;

public class InstanceDiskUsageAbove90PercentMonitor implements Monitor {
  private static final String COMPONENT = "DiskUsedPercentage";
  private static final double THRESHOLD = 0.9;

  @Override
  public Alert[] monitor(Metric metric) {
    if (!COMPONENT.equals(metric.component) ||
        !metric.active ) {
      return new InstanceAlert[0];
    }

    if (metric.value <= THRESHOLD) {
      return new InstanceAlert[0];
    }

    return new Alert[] { new InstanceAlert(metric) };
  }
}
