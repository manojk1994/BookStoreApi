# BookStore API Mock & ExtentReports Project
## Overview

This project demonstrates a Cucumber-TestNG BDD framework for testing a BookStore API against WireMock stubs, with reporting powered by ExtentReports. All API calls are mocked in tests, ensuring fast, repeatable, self-contained runs.

## Prerequisites

- Java 17 or later  
- Maven 3.6 or later  
- An IDE supporting Maven (IntelliJ IDEA, Eclipse) or a command-line shell

## Project Structure
src
├── main
│   └── java
│       ├── apiController
│       ├── constants
│       ├── data
│       └── utils
└── test
    ├── java
    │   ├── Hooks
    │   ├── Runner
    │   ├── Steps
    │   └── listeners
    └── resources
        ├── mappings
        ├── __files
        ├── features
        └── testng.xml
pom.xml
extent-config.xml
extent.properties

## Installation

1. Clone or unzip the project into your local workspace.  
2. From the project root, verify you have a valid `pom.xml` and the following folders:
      src/main/java
      src/test/java
      src/test/resources

## Running the Tests
  By default, the tests run against WireMock stubs:
      mvn clean test
  This will:
    Start a WireMock server on a random free port.
    Load all stub definitions from src/test/resources/mappings and response bodies from src/test/resources/__files.
    Execute all feature scenarios in src/test/resources/features via the TestNG Cucumber runner.
    Generate an ExtentReports HTML report.    

## Viewing the Report
    After the tests finish, open the HTML report at:
        target/extent-report/Spark.html

## Stub Configuration
    Mappings folder: src/test/resources/mappings
    Contains JSON files that define request matching and response bodies.
    Files folder: src/test/resources/__files
    Contains JSON response payloads referenced by mapping files.
    Custom stub priorities and error-case mappings are fully supported.      

## Switching to a Real API
    To run the suite against a live BookStore API:
         1.Create a properties file under src/test/resources named application-<ENV>.properties, for example:
            - base.url=https://api.mybookstore.com
            - base.port=443
         2.Pass the environment flag when running:
             - mvn clean test -Denv=real
         3.The Cucumber hook will detect env=real, skip WireMock, and load the real API base URL.

     
  

     
