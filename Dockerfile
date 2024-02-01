FROM maven:3.9 AS builder
WORKDIR /server
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:21
COPY --from=builder "/server/target/preferans-0.1.0.jar" /server/
EXPOSE 8080
CMD ["java", "-jar", "preferans-0.1.0.jar"]
