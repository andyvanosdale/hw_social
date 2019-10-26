package social;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class JsonMetricsFileStreamer {
  private final MetricStreamer metricStreamer;

  JsonMetricsFileStreamer(final MetricStreamer metricStreamer) {
    this.metricStreamer = metricStreamer;
  }

  public void start(String filePath) {
    File file = new File(filePath);
    Metric[] metrics;
    try (FileReader reader = new FileReader(file)) {
      Gson gson = new Gson();
      metrics = gson.fromJson(reader, Metric[].class);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("The file " + filePath + " does not exist.", e);
    } catch (IOException e) {
      throw new RuntimeException("The system encountered an error while reading or closing the file.", e);
    }

    for (Metric metric : metrics) {
      metricStreamer.monitor(metric);
    }
  }
}
