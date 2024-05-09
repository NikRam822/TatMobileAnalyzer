# Use an appropriate base image with Java and Maven installed
FROM maven:3.8.4-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project descriptor
COPY pom.xml .

# Copy the entire project
COPY . .

# Build and generate JavaDocs
RUN mvn clean package

# Use another lightweight base image
FROM openjdk:17-oracle AS final

# Set the working directory in the container
WORKDIR /javapp

# Copy the generated JavaDocs from the builder stage
COPY --from=builder /app/target/TatMobileAnalyzer-0.0.1-SNAPSHOT.jar app.jar

# Copy the application JAR file
# COPY target/TatMobileAnalyzer-0.0.1-SNAPSHOT.jar .

# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]