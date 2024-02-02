FROM maven:3.9 AS builder
WORKDIR /backend
COPY pom.xml .
COPY src ./src
ENV CLIENT_IP 77.105.174.80
RUN mvn clean package -DskipTests
FROM openjdk:21
WORKDIR /backend
COPY --from=builder /backend/target/preferans-0.1.0.jar .
EXPOSE 8080
CMD ["java", "-jar", "preferans-0.1.0.jar"]