FROM maven:3.9.3-eclipse-temurin-17 as build

WORKDIR /app

COPY . /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/edvantis-task-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
