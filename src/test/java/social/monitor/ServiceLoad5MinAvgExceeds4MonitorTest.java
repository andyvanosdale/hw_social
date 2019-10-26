package social.monitor;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import java.util.HashMap;
// import java.util.Map;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;

// import social.Metric;
// import social.alert.Alert;
// import social.alert.ServiceAlert;

// TODO Enable tests
class ServiceLoad5MinAvgExceeds4MonitorTest {
  // Monitor monitor;
  // Metric metric;
  // Alert[] alerts;

  // @BeforeEach
  // void setup() {
  //   Map<String, Double> serviceServerCounts = new HashMap<>();
  //   serviceServerCounts.put("Service0", 0.0);
  //   serviceServerCounts.put("Service1", 1.0);
  //   serviceServerCounts.put("Service2", 2.0);
  //   serviceServerCounts.put("Service3", 3.0);

  //   monitor = new ServiceLoad5MinAvgExceeds4Monitor(serviceServerCounts);
  //   metric = new Metric();
  //   metric.server = "TestServer";
  // }

  // @Nested
  // class GivenMetricBelowThreshold extends MetricBelowThreshold {
  //   @Nested
  //   class GivenComponentDoesApply extends ComponentDoesApply {
  //     @Nested
  //     class GivenInstanceIsActive extends InstanceIsActive {
  //       @Nested
  //       class WhenMonitoring extends Monitoring {
  //         @Test
  //         void thenItDoesNotAlert() {
  //           assertEquals(0, alerts.length);
  //         }
  //       }
  //     }
  //   }
  // }

  // @Nested
  // class GivenMetricAtThreshold extends MetricAtThreshold {
  //   @Nested
  //   class GivenComponentDoesApply extends ComponentDoesApply {
  //     @Nested
  //     class GivenInstanceIsActive extends InstanceIsActive {
  //       @Nested
  //       class WhenMonitoring extends Monitoring {
  //         @Test
  //         void thenItDoesNotAlert() {
  //           assertEquals(0, alerts.length);
  //         }
  //       }
  //     }
  //   }
  // }

  // @Nested
  // class GivenMetricAboveThreshold extends MetricAboveThreshold {
  //   @Nested
  //   class GivenComponentDoesNotApply extends ComponentDoesNotApply {
  //     @Nested
  //     class GivenInstanceIsActive extends InstanceIsActive {
  //       @Nested
  //       class WhenMonitoring extends Monitoring {
  //         @Test
  //         void thenItDoesNotAlert() {
  //           assertEquals(0, alerts.length);
  //         }
  //       }
  //     }
  //   }

  //   @Nested
  //   class GivenComponentDoesApply extends ComponentDoesApply {
  //     @Nested
  //     class GivenInstanceIsNotActive extends InstanceIsNotActive {
  //       @Nested
  //       class WhenMonitoring extends Monitoring {
  //         @Test
  //         void thenItDoesNotAlert() {
  //           assertEquals(0, alerts.length);
  //         }
  //       }
  //     }

  //     @Nested
  //     class GivenInstanceIsActive extends InstanceIsActive {
  //       @Nested
  //       class WhenMonitoring extends Monitoring {
  //         @Test
  //         void thenItAlerts() {
  //           assertAlert(alerts[0], metric, 4.0);
  //         }
  //       }
  //     }
  //   }
  // }

  // abstract class MetricBelowThreshold {
  //   @BeforeEach
  //   void setup() {
  //     metric.value = 3.9;
  //   }
  // }

  // abstract class MetricAtThreshold {
  //   @BeforeEach
  //   void setup() {
  //     metric.value = 4.0;
  //   }
  // }

  // abstract class MetricAboveThreshold {
  //   @BeforeEach
  //   void setup() {
  //     metric.value = 4.00001;
  //   }
  // }

  // abstract class ComponentDoesApply {
  //   @BeforeEach
  //   void setup() {
  //     metric.component = "Load5mAvg";
  //   }
  // }

  // abstract class ComponentDoesNotApply {
  //   @BeforeEach
  //   void setup() {
  //     metric.component = "InvalidComponent";
  //   }
  // }

  // abstract class InstanceIsActive {
  //   @BeforeEach
  //   void setup() {
  //     metric.active = true;
  //   }
  // }

  // abstract class InstanceIsNotActive {
  //   @BeforeEach
  //   void setup() {
  //     metric.active = false;
  //   }
  // }

  // abstract class Monitoring {
  //   @BeforeEach
  //   void setup() {
  //     alerts = monitor.monitor(metric);
  //   }
  // }

  // void assertAlert(Alert alert, Metric metric, double threshold) {
  //   ServiceAlert serviceAlert = new ServiceAlert(metric, threshold);
  //   assertEquals(serviceAlert.toString(), alert.toString());
  // }
}
