FROM openjdk:8-jdk-alpine
WORKDIR /usr/app
COPY target/GPSMockService-0.0.1-SNAPSHOT.jar /usr/app/app.jar
COPY src/main/resources/static/GPSData /usr/app/GPSData
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","app.jar"]
