# Contemporary Toll Parking

This Java REST API project is a prototype of a toll parking library.

## Purpose

The application is used to manage a toll parking. It provides an in-memory database to store parking slots and parking bills.

### Prerequisites

Java 8+ & Maven

### Installing

Build the code with

```
mvn clean install
```

Run application with

```
mvn spring-boot:run
```

Swagger documentation can be found at: http://localhost:8888/toll-parking/swagger-ui.html

## REST endpoints

initializeTollParking: initialize toll parking with a given toll parking configuration, the 
configuration has the number of parking slots divided in three categories
 - STANDARD
 - ELECTRIC_CAR_20KW
 - ELECTRIC_CAR_50KW
and a pricing policy. The pricing policy consist of a fixed amount and a hourly price.

updatePricingPolicy: The pricing policy is updated if the values are greater then 0.

enterParking: A parking slot is returned if available for the car type. 

leaveParking: The parking slot is freed and a parking bill is created.
## Built With
* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the MIT License
