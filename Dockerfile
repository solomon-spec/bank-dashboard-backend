# Stage 1: Build the JAR inside the container
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy necessary files (ignoring unnecessary ones)
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final runtime image
FROM openjdk:17-jdk-alpine
LABEL maintainer="solomonabate18@gmail.com"

# Set Dockerize version
ENV DOCKERIZE_VERSION v0.2.0

# Create and set the working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint to run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

