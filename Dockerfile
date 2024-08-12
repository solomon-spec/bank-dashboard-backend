FROM openjdk:17-jdk-alpine

LABEL maintainer="solomonabate18@gmail.com"

# Set Dockerize version
ENV DOCKERIZE_VERSION v0.2.0



# Create and set the working directory
RUN mkdir /app
WORKDIR /app

# Copy the application JAR file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint to use Dockerize to wait for a service before starting the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
