package social;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import social.alert.Alert;
import social.monitor.Monitor;

@ExtendWith(MockitoExtension.class)
class MetricStreamerTest {
  @Mock
  Alerter alerter;
  MetricStreamer metricStreamer;
  Metric metric;
  Monitor[] monitors;

  @Nested
  class GivenNullMetric {
    @BeforeEach
    void setup() {
      metric = null;
    }

    @Nested
    class AndASingleMonitor extends Monitors {
      AndASingleMonitor() {
        super(new int[] { 0 });
      }

      @Nested
      class WhenStreaming {
        @Test
        void thenAnExceptionIsThrown() {
          Exception exception = new NullPointerException("The metric can not be null.");
          metricStreamer = new MetricStreamer(alerter, monitors);
          Exception actualException = assertThrows(NullPointerException.class, () -> metricStreamer.monitor(metric));
          assertEquals(exception.getMessage(), actualException.getMessage());
        }

        @Test
        void thenNoAlertsAreTriggered() {
          metricStreamer = new MetricStreamer(alerter, monitors);
          try {
            metricStreamer.monitor(metric);
          } catch (NullPointerException e) {
            // Swallow exception to check validation
          }
          verify(alerter, times(0)).alert(any());
        }
      }
    }
  }

  @Nested
  class GivenAMetric {
    @Nested
    class AndTheMetricIsInvalid {
      @BeforeEach
      void setup() {
        // Assume valid as the test will flip each field individually for validation
        metric = new ValidMetric();
      }

      @Nested
      class AndASingleMonitor extends Monitors {
        AndASingleMonitor() {
          super(new int[] { 0 });
        }

        @Nested
        class WhenStreaming {
          @ParameterizedTest
          @MethodSource("social.MetricStreamerTest#invalidMetricArgumentsProvider")
          void thenAnExceptionIsRaised(MetricUpdater metricUpdater, String exceptionMessage) {
            metricUpdater.update(metric);
            Exception exception = new RuntimeException(exceptionMessage);
            metricStreamer = new MetricStreamer(alerter, monitors);
            Exception actualException = assertThrows(RuntimeException.class, () -> metricStreamer.monitor(metric));
            assertEquals(exception.getMessage(), actualException.getMessage());
            verify(alerter, times(0)).alert(any());
          }
        }
      }
    }

    @Nested
    class AndTheMetricIsValid {
      @BeforeEach
      void setup() {
        metric = new ValidMetric();
      }

      @Nested
      class AndZeroMonitors extends Monitors {
        AndZeroMonitors() {
          super(new int[0]);
        }

        @Nested
        class WhenStreaming extends Streaming {
          @Test
          void thenNoAlertsAreTriggered() {
            verify(alerter, times(0)).alert(any());
          }
        }
      }

      @Nested
      class AndASingleMonitor {
        @Nested
        class AndMonitorAlerts extends Monitors {
          AndMonitorAlerts() {
            super(new int[] { 1 });
          }

          @Nested
          class WhenStreaming extends Streaming {
            @Test
            void thenAnAlertIsTriggered() {
              verify(alerter, times(1)).alert(any());
            }
          }
        }

        @Nested
        class AndMonitorDoesNotAlert extends Monitors {
          AndMonitorDoesNotAlert() {
            super(new int[] { 0 });
          }

          @Nested
          class WhenStreaming extends Streaming {
            @Test
            void thenNoAlertsAreTriggered() {
              verify(alerter, times(0)).alert(any());
            }
          }
        }
      }

      @Nested
      class AndMultipleMonitors {
        @Nested
        class AndFirstMonitorAlerts extends Monitors {
          AndFirstMonitorAlerts() {
            super(new int[] { 1, 0 });
          }

          @Nested
          class WhenStreaming extends Streaming {
            @Test
            void thenAnAlertIsTriggered() {
              verify(alerter, times(1)).alert(any());
            }
          }
        }

        @Nested
        class AndOtherMonitorAlerts extends Monitors {
          AndOtherMonitorAlerts() {
            super(new int[] { 0, 1 });
          }

          @Nested
          class WhenStreaming extends Streaming {
            @Test
            void thenAnAlertIsTriggered() {
              verify(alerter, times(1)).alert(any());
            }
          }
        }

        @Nested
        class AndAllMonitorsAlert extends Monitors {
          AndAllMonitorsAlert() {
            super(new int[] { 1, 1 });
          }

          @Nested
          class WhenStreaming extends Streaming {
            @Test
            void thenAnAlertIsTriggered() {
              verify(alerter, times(2)).alert(any());
            }
          }
        }

        @Nested
        class AndMonitorsDoNotAlert extends Monitors {
          AndMonitorsDoNotAlert() {
            super(new int[] { 0, 0 });
          }

          @Nested
          class WhenStreaming extends Streaming {
            @Test
            void thenNoAlertsAreTriggered() {
              verify(alerter, times(0)).alert(any());
            }
          }
        }
      }
    }
  }

  class Streaming {
    @BeforeEach
    void setup() {
      metricStreamer = new MetricStreamer(alerter, monitors);
      metricStreamer.monitor(metric);
    }
  }

  class Monitors {
    private final int[] monitorAlertCounts;

    Monitors(final int[] monitorAlertCounts) {
      this.monitorAlertCounts = monitorAlertCounts;
    }

    @BeforeEach
    void setup() {
      monitors = new Monitor[monitorAlertCounts.length];
      for (int i = 0; i < monitorAlertCounts.length; i++) {
        int alertCount = monitorAlertCounts[i];
        Alert[] alerts = new Alert[alertCount];
        for (int alertIndex = 0; alertIndex < alertCount; alertIndex++) {
          alerts[alertIndex] = Mockito.mock(Alert.class);
        }
        monitors[i] = Mockito.mock(Monitor.class);
        lenient().when(monitors[i].monitor(any())).thenReturn(alerts);
      }
    }
  }

  static interface MetricUpdater {
    void update(Metric metric);
  }

  static Stream<Arguments> invalidMetricArgumentsProvider() {
    return Stream.of(
        Arguments.of((MetricUpdater) (m -> m.component = null),
            "The metric is required to have a valid component field."),
        Arguments.of((MetricUpdater) (m -> m.component = ""),
            "The metric is required to have a valid component field."),
        Arguments.of((MetricUpdater) (m -> m.server = null), "The metric is required to have a valid server field."),
        Arguments.of((MetricUpdater) (m -> m.server = ""), "The metric is required to have a valid server field."),
        Arguments.of((MetricUpdater) (m -> m.service = null), "The metric is required to have a valid service field."),
        Arguments.of((MetricUpdater) (m -> m.service = ""), "The metric is required to have a valid service field."),
        Arguments.of((MetricUpdater) (m -> m.active = null), "The metric is required to have a valid active field."),
        Arguments.of((MetricUpdater) (m -> m.timestamp = null),
            "The metric is required to have a valid timestamp field."),
        Arguments.of((MetricUpdater) (m -> m.value = null), "The metric is required to have a valid value field."));
  }

  class ValidMetric extends Metric {
    ValidMetric() {
      active = true;
      component = "TestComponent";
      server = "TestServer";
      service = "TestService";
      timestamp = 0;
      value = 0.0;
    }
  }

  class InvalidMetric extends ValidMetric {
    InvalidMetric() {
      active = null;
    }
  }
}
