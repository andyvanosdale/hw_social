# Social Systems Metrics Monitoring

The Social Systems Metrics Monitoring application takes in a stream of metrics and alerts on various issues with a log written to the standard console.

## Architecture

```
                    ┌────────────────┐
                    │                │
                    │     Metric     │
                ┌──▶│     Alert      │───┐
                │   │     Monitor    │   │
┌-──────────┐   │   │                │   │   ┌───────────┐
│           │   │   └────────────────┘   │   │           │
│  Stream   │   │                        ├──▶│  Alerter  │
│ Processor │───┤                        │   │           │
│           │   │   ┌────────────────┐   │   └───────────┘
└-──────────┘   │   │                │   │
                │   │     Metric     │   │
                └──▶│     Alert      │───┘
                    │     Monitor    │
                    │                │
                    └────────────────┘
```

This architecture allows it to evolve into a real-time stream processor. Processors read off the stream of data (and be partioned based on the component with ordering based on the timestamp) and alert as soon as possible.

The logic to alert is built into each monitor processor. Processors could be stateful or not, depending on their requirements (for instance, service level monitors would need to keep some state to detect majorities).

While this is currently a JSON file reader, it is mimicked as a stream processor.

## Metrics

A metric is a JSON object containing the following fields:

| Field       | Type                       | Description                                            |
| ----------- | -------------------------- | ------------------------------------------------------ |
| `server`    | string                     | The server name.                                       |
| `service`   | string                     | The service name.                                      |
| `component` | string                     | The metric name.                                       |
| `value`     | number, string (as number) | The value of the metric.                               |
| `timestamp` | number                     | The epoch time in seconds when the metric was emitted. |
| `active`    | boolean                    | Whether the service instance was active or not.        |

Metrics are always assumed to be in time order.

Metrics are inputed via a UTF-8 file with metric JSON objects contained in a JSON array.

> Note: Malformed metrics (not conforming to the schema above) will stop the stream and present an error. The processor should be fixed and resumed.

## Alerts

Alerts are logged to standard out. Standard error is reserved for runtime errors in the application. As soon as an alert is triggered, it is raised. Other metrics that could trigger the exact same alert (for instance, different servers for the same service and component that has already triggered) should not trigger the alert again.

> Note: Only active instance metrics are only counted. A majority constitutes greater than 50%.

> Note: Metrics that are duplicated (and active) would be counted and could produce a false-positive. The upstream system is responsible (at this time) for ensuring it only sends one metric required.

> Note: Missing metrics could pose an issue with the alerting, since it could be in an alert status or trigger an alert.

### Disk used on any instance exceeds 90%

This indicates that an error is likely to occur in the near future due to the inability to write to files, like a log.

### Load across majority of service on 5 minute average exceeds 4.0

This indicates a higher than normal load, and the service load should be scaled up (or closely monitored).

## Requirements

The source code is compatible with Java 8+. However, it is recommended to run against the lastest Java version (last tested against Java 11).

The build system uses Gradle 5.4.1.

> Note: It is highly recommended to use the Gradle Wrapper for Gradle execution, as it is locked and tested to a specific Gradle version.

## Buildinng

To build:

```bash
$ ./gradlew build
```

> Note: Building also runs the unit tests.

## Running

The application reads a JSON array file of Metric objects and writes out a list of alerts, if any.

After building the application, extract the ZIP or TAR from `build/distributions`. Once extracted, change to the `bin` directory. To run the application:

```bash
$ monitor file_path_to_json_metrics
```

## Testing

Testing follows a Gherkin language format that follows a simple Given-When-Then flow of inputs, some operation, and the outputs. Tests are nested inside classes to denoting Gherkin language structure.

To run the tests:

```bash
$ ./gradlew test
```

## Future Enhancements

- Finish unit tests for App
- Finish unit tests for ServiceLoad5MinAvgExceeds4Monitor
- Duplicate metric detection
- End to end (integration) tests (using a shell script to execute and check the console output)
- Log (or alert) when metrics may be missing
