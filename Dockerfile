FROM eclipse-temurin:21-jdk-alpine
LABEL authors="denismalinin"

VOLUME /tmp
COPY app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]