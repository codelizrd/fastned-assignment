# Willem van Duijn
Software Engineering Assessment for Fastned

## Introduction

My solution uses a multi-module Gradle project, using Spring Boot as the base Framework and Kotlin on the server side, 
and a GUI build with Typescript and React.

I used this assignment to try technologies that are new to me:
 - Multi-module Gradle: used multi-module before in Maven; in Gradle it was quite challenging to get the Gui-module working
 - Kotlin: so far I've been using plain Java; I watched an Udemy course on Kotlin and applied that knowledge 
 - @tanstack/react-query: most applications I wrote used GraphQL with Apollo Client for queries, mutations and caching

### Module: Common
Shared codebase between the webserver and command line application. Build using the Spring Boot framework, programmed
in Kotlin.

It uses an in-memory Repository for storing the network. Includes a `ModelCalculatorService` which is responsible
for calculating a Grid's output. There are two implementations:

 - `LoopedSimplifiedCalculatorService`: loop over all days, and sum the output of that day
 - `IntegralSimplifiedCalculatorService` (Primary): the total output is calculated using the integral of a function

### Module: CommandLine
The commandline application for Story #1

### Module: WebServer
The webserver with REST API for Stories #2 and #3. It also serves the GUI.

### Module: Gui
A React project which creates a Jar to be served by the WebServer.


## Running

Before running, you need to compile using:

```shell
./gradlew build
```

### Story #1: the command line application

The command to calculate the output of a network after a number of days:

```shell
java -jar ./CommandLine/build/libs/CommandLine-0.0.1-SNAPSHOT.jar <inputFile> <elapsedTimeInDays>
```

### Story #2 and #3

To start the webserver:

```shell
./gradlew WebServer:bootRun
```

This will start the webserver on port 8090 to make the requests.

Note that I added two extra fields in the output, as the GUI uses the REST interface to and I needed them for 
Story #5.

### Story #4 and #5

Point your browser to: http://localhost:8090

The application starts with an empty GUI, unless the webserver was previously loaded with a network using a REST call.

To propagate, click "Add first grid" in the GUI, or import a JSON file using the "Load JSON file" button.