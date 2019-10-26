package social.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import social.Metric;
import social.alert.Alert;
import social.alert.InstanceAlert;

class InstanceDiskUsageAbove90PercentMonitorTest {
  Monitor monitor;
  Metric metric;
  Alert[] alerts;

  @BeforeEach
  void setup() {
    monitor = new InstanceDiskUsageAbove90PercentMonitor();
    metric = new Metric();
    metric.server = "TestServer";
    metric.service = "TestService";
    metric.timestamp = 1234567890;
  }

  @Nested
  class GivenMetricBelowThreshold extends MetricBelowThreshold {
    @Nested
    class GivenComponentDoesApply extends ComponentDoesApply {
      @Nested
      class GivenInstanceIsActive extends InstanceIsActive {
        @Nested
        class WhenMonitoring extends Monitoring {
          @Test
          void thenItDoesNotAlert() {
            assertEquals(0, alerts.length);
          }
        }
      }
    }
  }

  @Nested
  class GivenMetricAtThreshold extends MetricAtThreshold {
    @Nested
    class GivenComponentDoesApply extends ComponentDoesApply {
      @Nested
      class GivenInstanceIsActive extends InstanceIsActive {
        @Nested
        class WhenMonitoring extends Monitoring {
          @Test
          void thenItDoesNotAlert() {
            assertEquals(0, alerts.length);
          }
        }
      }
    }
  }

  @Nested
  class GivenMetricAboveThreshold extends MetricAboveThreshold {
    @Nested
    class GivenComponentDoesNotApply extends ComponentDoesNotApply {
      @Nested
      class GivenInstanceIsActive extends InstanceIsActive {
        @Nested
        class WhenMonitoring extends Monitoring {
          @Test
          void thenItDoesNotAlert() {
            assertEquals(0, alerts.length);
          }
        }
      }
    }

    @Nested
    class GivenComponentDoesApply extends ComponentDoesApply {
      @Nested
      class GivenInstanceIsNotActive extends InstanceIsNotActive {
        @Nested
        class WhenMonitoring extends Monitoring {
          @Test
          void thenItDoesNotAlert() {
            assertEquals(0, alerts.length);
          }
        }
      }

      @Nested
      class GivenInstanceIsActive extends InstanceIsActive {
        @Nested
        class WhenMonitoring extends Monitoring {
          @Test
          void thenItAlerts() {
            assertAlert(alerts[0], metric);
          }
        }
      }
    }
  }

  abstract class MetricBelowThreshold {
    @BeforeEach
    void setup() {
      metric.value = 0.89;
    }
  }

  abstract class MetricAtThreshold {
    @BeforeEach
    void setup() {
      metric.value = 0.9;
    }
  }

  abstract class MetricAboveThreshold {
    @BeforeEach
    void setup() {
      metric.value = 0.90001;
    }
  }

  abstract class ComponentDoesApply {
    @BeforeEach
    void setup() {
      metric.component = "DiskUsedPercentage";
    }
  }

  abstract class ComponentDoesNotApply {
    @BeforeEach
    void setup() {
      metric.component = "InvalidComponent";
    }
  }

  abstract class InstanceIsActive {
    @BeforeEach
    void setup() {
      metric.active = true;
    }
  }

  abstract class InstanceIsNotActive {
    @BeforeEach
    void setup() {
      metric.active = false;
    }
  }

  abstract class Monitoring {
    @BeforeEach
    void setup() {
      alerts = monitor.monitor(metric);
    }
  }

  void assertAlert(Alert alert, Metric metric) {
    InstanceAlert instanceAlert = new InstanceAlert(metric);
    assertEquals(instanceAlert.toString(), alert.toString());
  }
}
