# Use official OpenJDK image as base
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy build artifact from Gradle build
COPY build/libs/*.jar app.jar

# Expose port (default for Spring Boot)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]