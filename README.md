# The Nim Game

This is a Spring Boot project written in Java 17 and built with Maven. It implements the Nim game, where players take turns removing matches. The player that takes the last match loses.


## Getting Started



### Prerequisites

- Java 17 (configured in your JAVA_HOME and path)
- Maven (configured in your M2_HOME and path)

### Configuration


You can configure the game in the 'application.properties' file with the following properties:

- 'server.port': sets the port which the server will run
- 'number.of.matches.at.game.start': Sets the number of matches at the beginning of the game
- 'max.number.of.matches.player.can.take': Sets the maximum number of matches a player can take
- 'is.cpu.smart': set to 'true' if the CPU should play to win.

## Running the Project


To run the project, follow these steps:

1. Navigate to the main folder of the app module '...\nim-game\app', run the following Maven command: 'mvn spring-boot:run'
2. Alternatively, you can run it in IntelliJ by running the main method in the 'NimGameApplication' class.

## Playing the Game

Once the server is running, you can play the game by accessing the Swagger-UI to visually test the API:

- Swagger URL: http://localhost:8085/swagger-ui/index.html

Alternatively, you can use an API client program, such as Postman, and make requests to:

- API Endpoint: http://localhost:8085/game

For more information about the API endpoints consult the 'GameController' class.

## Database Access

An in-memory H2 database is used and is accessible at:

- H2 Console URL: http://localhost:8085/h2-console
    - JDBC URL: jdbc:h2:mem:testdb
    - Username: sa

For more information on the database you can check the 'datasource.properties' file.

Have fun!