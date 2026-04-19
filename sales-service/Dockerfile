FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY /target/sales-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","app.jar"]