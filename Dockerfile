FROM openjdk:21-ea-slim

COPY remote-schedule-service-standalone.jar .

ENTRYPOINT ["java", "-jar", "remote-schedule-service-standalone.jar"]