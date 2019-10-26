package social;

import social.alert.Alert;

class Alerter {
  private final AlertPrinter printer;

  public Alerter() {
    this(System.out::println);
  }
  public Alerter(AlertPrinter printer) {
    this.printer = printer;
  }

  public void alert(Alert alert) {
    printer.print(alert.toString());
  }

  public interface AlertPrinter {
    void print(String text);
  }
}
