# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/kubernetesdeploymentoptimizer-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application with the correct main class
ENTRYPOINT ["java", "-jar", "app.jar" , "com.kubernetesdeploymentoptimizer.Main"]
