# BookStore Backend

## 1. Introduction

### 1.1 Project Overview
This project is a backend demo system for a bookshop, built on `Spring Boot3.x` and `Java 17`, designed to showcase modern best practices in Java backend development.

### 1.2 Why Spring Boot?
As one of the most mainstream backend frameworks in the Java ecosystem, `Spring Boot` is renowned for its outstanding performance, rich ecosystem, and efficient development experience. The choice of `Spring Boot` is based on the following considerations:
- Maturity & Stability: With strong community support and long-term maintenance, `Spring Boot` ensures system reliability and scalability.
- Development Efficiency: Its convention-over-configuration approach and auto-configuration mechanisms significantly boost productivity.
- Comprehensive Ecosystem: Seamless integration with the Spring ecosystem and various middleware solutions meets diverse business requirements.

Leveraging extensive hands-on experience with `Spring Boot`, I can efficiently tackle development challenges and ensure high-quality project delivery.

### 1.3 Why Choose Reactive?
Traditional Java backends use a `blocking I/O` model, where each request monopolizes a thread resource. In `I/O-intensive` scenarios, threads remain idle while waiting for `I/O` operations, resulting in inefficient system resource utilization and limited concurrency.

This project adopts the reactive programming paradigm (based on `WebFlux`), offering the following core advantages:

Resource Efficiency
- Event-loop-based `non-blocking I/O` model
- Threads are only occupied during `CPU` computation
- Thread resources are automatically released during `I/O` waits

Performance Benefits
- Supports higher concurrency under the same hardware conditions (theoretically 5-10x improvement)
- 30%-50% reduction in memory usage (compared to the traditional Servlet model)
- Lower latency and significantly increased throughput

With deep hands-on experience in `Netty` and `WebFlux`, I have successfully implemented reactive architectures in multiple high-concurrency production environments, achieving stable performance at `100,000+ QPS`.

### 1.4 Project Layout
Main directory：
```text
.
├── database: H2 database persistence file directory
└── src: 
    ├── main
    │   ├── java: Java source code directory
    │   └── resources
    │       ├── ddl: Database scripts
    │       └── i18n: Internationalization configuration files
    └── test: Unit test files
```

Source code directory：
```text
.
├── bean: Pojo
│   ├── entity: Database entity class
│   ├── request：request parameter
│   └── vo: Response struct
├── component
│   ├── constants: Constants
│   ├── database: Database configuration and utility classes
│   ├── exception: Business exception definitions
│   ├── i18n: Internationalization configuration and utilities
│   ├── jackson: Jackson configuration class
│   ├── lambda: Lambda utility class
│   ├── springdoc: SpringDoc configuration
│   └── webflux: WebFlux configuration
├── controller: Routing layer
├── repository: Persistence layer
└── service: Business logic layer
```

## 2. Usage Instructions

### 2.1 How to Start the Project
- Running from Source Code: You can start the project by launching `BookstoreApplication`. The necessary initialization will be handled automatically upon startup—no additional setup is required. Once the application starts successfully, it's ready for use.
- Running from JAR: You can also start the project by executing `java -jar app.jar`. Similarly, all required initialization will be performed automatically. The application will be fully operational once it boots up successfully.

### 2.2 How to Access API Documentation
Once the system is started successfully, you can view the API documentation by visiting the [Swagger UI](http://localhost:8080/swagger-ui.html)

### 2.3 How to run unit tests

Run the Maven command:

```shell
mvn test
```

### 2.4 How to compile the project

Run the Maven command:
```shell
mvn clean package -Dmaven.test.skip=true
```

## 3. Unit Testing
This project employs a layered testing strategy to ensure code quality and functional reliability:

### 3.1 API Layer Testing (ControllerTest)
- Scope: All API endpoints
- Verifications：
  - Parameter validation (including boundary value testing)
  - HTTP status code responses
  - Exception handling mechanisms

### 3.2 Service Layer Testing (ServiceTest)
- Scope: Core business logic
- Verifications：
  - Business process correctness
  - Exception scenario handling
  - Data consistency
- Test Environment:
  - H2 in-memory database
  - Automatic test dataset initialization

Test Execution Instructions:
- Test code location: `src/test/`
- Execution command: `mvn test`