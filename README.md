[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Gladhus_volcano-camping-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Gladhus_volcano-camping-api)


# Volcano Camping API

## Running the project

### API
The easiest way to run the project locally is to run it in docker using `docker-compose up -d --build`.

The API is exposed on port 8080, so in local you can access it at `http://localhost:8080`

### Tests
To run the tests locally you can run the command `mvn test`.

#### Concurrency Test
The project includes a bash script that runs a concurrency test against the running project. 
To run the test simply use the following command : `./test-concurrency.sh {checkin} {checkout}`

## Pre-requisites

### Docker
The docker daemon is required to run the integration tests and is also the easiest way to run the project locally.

## Swagger

It is possible to access the swagger documentation by going to `http://{BASE_URL}/swagger-ui/index.html`.

For example in local : `http://localhost:8080/swagger-ui/index.html`
