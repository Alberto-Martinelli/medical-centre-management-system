# Medical Centre Scheduler

A Java project for managing doctors, daily schedules, appointment bookings,
patient check-in, and appointment statistics. It originated as a university
object-oriented programming exam project. The
[original requirements](docs/original-requirements.md) are retained for reference.

## Requirements

- JDK 17
- Apache Maven

## Build and test

From the project directory:

```sh
mvn clean verify
```

This compiles the application, runs the JUnit tests, and builds
`target/med-1.0.0.jar`. To run only the tests, use `mvn test`.

The project currently exposes its functionality through `MedManager`; it has no
command-line entry point. See
[TestExample](src/test/java/example/TestExample.java) for an example of the API
covering scheduling, booking, check-in, completion, and statistics.

## Project layout

```text
src/main/java/       Application classes
src/test/java/       JUnit tests and API usage example
docs/                Original exam requirements
pom.xml              Maven build configuration
```

Import the project into your IDE as a Maven project so it detects the source and
test directories. Build output and local IDE configuration are ignored by Git.
