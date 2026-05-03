# ---------- STAGE 1: BUILD ----------
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- STAGE 2: RUN ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY /target/config-server-1.0-SNAPSHOT.jar  app.jar

EXPOSE 8888

ENTRYPOINT ["java","-jar","app.jar"]