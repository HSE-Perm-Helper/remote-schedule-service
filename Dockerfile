FROM openjdk:17-jdk-slim

COPY remote-schedule-service-standalone.jar .

ENTRYPOINT ["java", "-jar", "remote-schedule-service-standalone.jar"]