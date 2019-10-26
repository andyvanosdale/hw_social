package social;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JsonMetricsFileStreamerTest {
  String filePath;
  @Mock
  MetricStreamer metricStreamer;
  JsonMetricsFileStreamer jsonStreamer;

  @BeforeEach
  void setup() {
    jsonStreamer = new JsonMetricsFileStreamer(metricStreamer);
  }

  @Nested
  class GivenAnInvalidFilePath {
    @BeforeEach
    void setup() throws Exception {
      filePath = Path.of(Files.createTempDirectory("test").toString(), "invalid_path").toString();
    }

    @Nested
    class WhenStarting {
      @Test
      void thenItThrowsAnException() {
        Exception actualException = assertThrows(RuntimeException.class, () -> jsonStreamer.start(filePath));
        assertEquals("The file " + filePath + " does not exist.", actualException.getMessage());
      }
    }
  }

  @Nested
  class GivenZeroMetrics extends MetricsFile {
    GivenZeroMetrics() {
      super("no_metrics.json");
    }

    @Nested
    class WhenStarting extends Start {
      @Test
      void thenNoMetricsAreStreamed() {
        verify(metricStreamer, times(0)).monitor(any());
      }
    }
  }

  @Nested
  class GivenOneMetric {
    @Nested
    class AndTheMetricValueCouldRepresentADouble {
      @Nested
      class WhenStarting {
        @ParameterizedTest
        @MethodSource("social.JsonMetricsFileStreamerTest#valueProvider")
        void thenTheMetricValueIsValid(String filePath, Double value) {
          String metricResourcePath = getAbsolutePathOfMetricResource(filePath);
          jsonStreamer.start(metricResourcePath);
          ArgumentCaptor<Metric> metricCaptor = ArgumentCaptor.forClass(Metric.class);
          verify(metricStreamer, times(1)).monitor(metricCaptor.capture());
          Metric metric = metricCaptor.getValue();
          assertEquals(value, metric.value);
          assertEquals("inbox_production02", metric.server);
          assertEquals("InboxService", metric.service);
          assertEquals("Load5mAvg", metric.component);
          assertEquals(1476057600, metric.timestamp);
          assertEquals(true, metric.active);
        }
      }
    }

    @Nested
    class AndTheMetricValueIsAStringAsNotAsANumber extends MetricsFile {
      AndTheMetricValueIsAStringAsNotAsANumber() {
        super("one_metric_value_string_non_number.json");
      }

      @Nested
      class WhenStarting {
        @Test
        void thenItThrowsAnException() {
          assertThrows(NumberFormatException.class, () -> jsonStreamer.start(filePath));
        }
      }
    }
  }

  @Nested
  class GivenMultipleMetrics extends MetricsFile {
    GivenMultipleMetrics() {
      super("multiple_metrics.json");
    }

    @Nested
    class WhenStarting extends Start {
      @Test
      void thenTheMetricsAreStreamed() {
        verify(metricStreamer, times(2)).monitor(any());
      }
    }
  }

  class MetricsFile {
    private final String fileName;

    MetricsFile(final String fileName) {
      this.fileName = fileName;
    }

    @BeforeEach
    void setup() {
      filePath = getAbsolutePathOfMetricResource(fileName);
    }
  }

  class Start {
    @BeforeEach
    void setup() {
      jsonStreamer.start(filePath);
    }
  }

  static Stream<Arguments> valueProvider() {
    return Stream.of(Arguments.of("one_metric_value_double.json", 3.7),
        Arguments.of("one_metric_value_integer.json", 3.0), Arguments.of("one_metric_value_string_number.json", 3.7));
  }

  static String getAbsolutePathOfMetricResource(String fileName) {
    ClassLoader classLoader = JsonMetricsFileStreamerTest.class.getClassLoader();
    File file = new File(
        classLoader.getResource(Path.of("JsonMetricsFileStreamerTest", fileName).toString()).getFile());
    return file.getAbsolutePath();
  }
}
