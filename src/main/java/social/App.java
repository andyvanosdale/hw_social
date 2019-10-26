package social;

import java.util.HashMap;
import java.util.Map;

import social.monitor.InstanceDiskUsageAbove90PercentMonitor;
import social.monitor.Monitor;
import social.monitor.ServiceLoad5MinAvgExceeds4Monitor;

public class App {
  private final Alerter alerter;

  App() {
    this(new Alerter());
  }

  App(final Alerter alerter) {
    this.alerter = alerter;
  }

  void start(String filePath) {
    Map<String, Double> serviceServerCounts = serviceServerCounts();
    Monitor[] monitors = new Monitor[] { new InstanceDiskUsageAbove90PercentMonitor(),
        new ServiceLoad5MinAvgExceeds4Monitor(serviceServerCounts) };
    MetricStreamer metricStreamer = new MetricStreamer(alerter, monitors);
    new JsonMetricsFileStreamer(metricStreamer).start(filePath);
  }

  Map<String, Double> serviceServerCounts() {
    Map<String, Double> serviceServerCounts = new HashMap<>();
    serviceServerCounts.put("InboxService", 3.0);
    serviceServerCounts.put("ReportPDFGenerator", 2.0);
    serviceServerCounts.put("FacebookRealtime", 2.0);
    return serviceServerCounts;
  }

  public static void main(String[] args) {
    if (args.length == 0 || args.length > 1) {
      System.err.println("Social Systems Service Monitor");
      System.err.println("missing metrics file path");
      System.err.println("Usage: monitor json_metrics_file_path");
      return;
    }

    new App().start(args[0]);
  }
}
