FROM maven:3.9 AS builder
WORKDIR /server
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:21
ENV SPRING_PROFILES_ACTIVE=production
WORKDIR /server
COPY --from=builder "/warehouse/target/preferans-0.1.0.jar" /server/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
