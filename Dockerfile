# Use a base image with OpenJDK (you can choose a specific version, e.g., openjdk:17)
FROM openjdk:17-jdk-slim
# Set the working directory inside the container
WORKDIR /app
# Copy the .jar file from your local directory to the container's working directory
COPY unit3_bookingcab-0.0.1-SNAPSHOT.jar /app/app.jar
# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]