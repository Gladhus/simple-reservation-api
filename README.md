# Volcano Camping API

## Running the project

### API
To run the API locally use the maven command `mvn spring-boot:run`

### Tests
To run the tests locally you can run the command `mvn test`

## Pre-requisites

### MySQL
In order to run the project you need to have a MySQL server and put the details in the `applications.properties` file. 

By default, the application will look for a MySQL instance running locally on port 3306 with the following details : 
* Database named `volcano_campsite`
* User `volcanouser // volcanopassword`

### Maven
The easiest way to run the project is through maven with the `mvn spring-boot:run` command. 

### Docker
For the integration tests to run you need to have the docker daemon running.