FROM maven:3.6.0-jdk-11-slim as builder
COPY . /usr/app
WORKDIR /usr/app
RUN mvn package -DskipTests=true
WORKDIR /usr/app/target
COPY GPSMockService-0.0.1-SNAPSHOT.jar app.jar

FROM openjdk:8-jdk-alpine
WORKDIR /usr/app
COPY --from=build /usr/app/target/app.jar /usr/app
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
