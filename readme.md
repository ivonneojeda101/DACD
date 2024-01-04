# DACD Final Project

## Cover Page
- **Title:** Destination Advisor
- **Subject:** Development of Application for Data Science
- **Course:** 4
- **Degree:** Bachelor in Science and Engineering of Data
- **School:** School of Computer Science
- **University:** University Las Palmas de Gran Canaria

## Functionality Summary
This Java-based application recommends an optimal travel destination (It provides daily weather details and a curated list of up to three flights per day to the closest airport of the recommended destination.). By Assessing weather forecasts for the next five days across various destinations considering user-defined importance for temperature, precipitation probability, humidity, cloud cover, and wind speed.

## Application Modules Overview

This application comprises five essential modules:

1. **Weather Provider:**
   - Retrieves a 5-day forecast for specified locations at defined intervals (hours/minutes).
   - Sends the gathered information to the Bus module.

2. **Flight Provider:**
   - Gathers at specified intervals (hours/minutes) available flights for the next five days to specified locations from one provided origin airport.
   - Transmits collected data to the Bus module.

3. **Bus:**
   - Acts as a central broker, facilitating seamless data transfer between the preceding modules and the Datalake-Builder.

4. **Datalake-Builder:**
   - Collects data from the previous modules.
   - Primarily responsible for organizing and storing this information in files.

5. **Destination Advisor:**
   - Primary interface for user interaction.
   - Processes data received from the preceding modules based on user preferences.
   - Offers recommendations for the ideal destination.

## Resources Used
- **Development Environment:** IntelliJ IDEA
- **Version Control Tools:** Git
- **Repository Platform:** GitHub
- **Documentation Tools:** Markdown plugin

## Prerequisites
- **Java Version:** Java 17 or higher is required to run this application.

## Execution Arguments
When running the modules, the following arguments are required:

A. **Weather Provider:**

Execute the .jar with the following parameters :

```bash
java -jar weather-provider.jar apiKey apiURL csvFilePath busUrl topic hoursFrequency
```

1. `apiKey`: API access token.
2. `apiURL`: URL of the meteorological service's API.
3. `csvFilePath`: File path to the CSV file containing geographical locations, separated with ";".
   - Each line should contain:
      - Name of the island
      - Longitude
      - Latitude
      - Code of the nearest airport
     
Note: There is an example in the repository folder examples.
4. `busUrl`: URL of the broker.
5. `topic`: Name of the topic used to deliver weather information messages.
6. `hoursFrequency`: The frequency of the query (in hours, an integer with one decimal).

Examples:
- 1.0 This is one hour
- 2.5 This is two hours and a half
- 0.1 This is six minutes

B. **Flight Provider:**

Execute the .jar with the following parameters :

```bash
java -jar flight-provider.jar apiURL apiURLToken clientId clientSecret busUrl topic csvFilePath hoursFrequency airportCode
```

1. `apiURL`: URL of the flight offers service's API.
2. `apiURLToken`: URL of the service point to generate the token used to consume the flight offers service's API.
3. `clientId`: Client ID associated with the account used to consume the service.
4. `clientSecret`: Client secret linked to the account used for consuming the service.
5. `busUrl`: URL of the broker.
6. `topic`: Name of the topic used to deliver flights information messages.
7. `csvFilePath`: Same file path of the CSV file use in the weather provider module.
8. `hoursFrequency`: The frequency of the query (in hours, an integer with one decimal).
9. `airportCode`: This parameter specifies the origin airport for conducting the flight search. In our case LPA.

C. **Datalake-Builder:**

Execute the .jar with the following parameters :

```bash
java -jar datalake-builder.jar busUrl topics datalakePath
```

1. `busUrl`: URL of the broker.
2. `topics`: Identifiers of topics for the weather and flight messages. Separate the topics by a comma (no space after it). Example: prediction.Weather,provider.Flight
3. `datalakePath`: Directory path where events data will be stored.

D. **Destination Advisor:**

Execute the .jar with the following parameters :

```bash
java -jar destination-advisor.jar busUrl topics datalakePath csvFilePath
```

1. `busUrl`: URL of the broker.
2. `topics`: Identifiers of topics for the weather and flight messages. Separate the topics by a comma (no space after it). Example: prediction.Weather,provider.Flight
3. `datalakePath`: Directory path where the datalake is located..
4. `csvFilePath`: Same file path of the CSV file use in the weather provider module.

## Design

### Architecture

The application is built under the kappa architecture concept since it needs to process immediate data insights to provide accurate information to the user and at the same time to store it in a data-lake then a continuous data stream is required. Because the kappa architecture's emphasis is stream processing without separate batch layers, this streamlined approach aligns with the continuous flow of data and the real-time processing needed by the modules of destination-advisor and datalake-builder in specific.

However, MVC Architecture is used in each module to cleanly separate concerns: the Model manages data logic, the View handles user interface, and the Controller orchestrates interactions, enhancing code organization and maintainability by keeping distinct functionalities apart, thinking in future improvements it allows a certain level of scalability through modular development.

### Design Patterns and Principles

In the development of this application, various design patterns and principles were employed to enhance the structure, maintainability, and extensibility of the codebase.

#### Publisher/Subscriber pattern

The pattern enables modules to communicate asynchronously, fostering flexible and scalable data exchange by dividing senders and receivers within the application.

On the other hand, the pattern promotes a decoupled, efficient system where each module operates autonomously while contributing to the application's collective intelligence and user-centric functionality.

#### SOLID Principles

The SOLID principles are a set of object-oriented design principles that aim to create scalable and maintainable software. Here's a brief overview:

- Each class should have a single responsibility.
- Software entities should be open for extension but closed for modification.
- Objects of a superclass should be able to be replaced with objects of a subclass without affecting the correctness of the program.
- A class should not be forced to implement interfaces it does not use.
- High-level modules should not depend on low-level modules; both should depend on abstractions.

### UML Class Diagram

![UML Class Diagram](./src/files/UML_Ejercicio_1.jpg)
