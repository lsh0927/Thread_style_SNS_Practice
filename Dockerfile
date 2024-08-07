# Use Eclipse Temurin OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the argument for the JAR file
ARG JAR_FILE=build/libs/*.jar

# Copy the JAR file into the image
COPY ${JAR_FILE} app.jar

# Define environment variables
ARG PROFILES
ARG ENV

# Set the entry point for the container
ENTRYPOINT ["java", "-DSPRING.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]
