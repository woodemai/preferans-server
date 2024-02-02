FROM maven:3.9 AS builder
WORKDIR /app-server
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:21
ENV SPRING_PROFILES_ACTIVE=production
ENV CLIENT_URL=https:woo-preferans.ru
WORKDIR /app-server
COPY --from=builder "/app-server/target/preferans-0.1.0.jar" /app-server/app.jar
EXPOSE 8443
EXPOSE 8086
CMD ["java", "-jar", "app.jar"]