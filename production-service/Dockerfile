# ---------- STAGE 1: BUILD ----------
#FROM eclipse-temurin:17-jdk-alpine AS builder
#
#WORKDIR /app
#
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
#COPY src ./src
#RUN mvn clean package -DskipTests

# ---------- STAGE 2: RUN ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY /target/production-service-1.0-SNAPSHOT.jar  app.jar

HEALTHCHECK --interval=30s --timeout=5s \
 CMD curl -f http://localhost:8081/actuator/health || exit 1

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]