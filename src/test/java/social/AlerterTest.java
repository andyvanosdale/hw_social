package social;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import social.Alerter.AlertPrinter;
import social.alert.Alert;

@ExtendWith(MockitoExtension.class)
class AlerterTest {
  @Mock
  Alert alert;
  @Mock
  AlertPrinter alertPrinter;
  Alerter alerter;

  @BeforeEach
  void setup() {
    alerter = new Alerter(alertPrinter);
  }

  @Test
  void printsAlert() {
    when(alert.toString()).thenReturn("TestValue");
    alerter.alert(alert);
    verify(alertPrinter).print("TestValue");
  }
}
