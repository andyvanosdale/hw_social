package social.monitor;

import social.alert.Alert;
import social.Metric;

public interface Monitor {
  Alert[] monitor(Metric metric);
}
