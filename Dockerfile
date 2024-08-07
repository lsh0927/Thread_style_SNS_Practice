# Use Eclipse Temurin OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-DSPRING.profiles.active=${PROFILES}", "-Dserver.env=${ENV}","-jar", "app.jar"]
