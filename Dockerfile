FROM maven:3.8-openjdk-18-slim
WORKDIR /volcano-camping-api
COPY . .
RUN mvn clean install -DskipTests
CMD mvn spring-boot:run