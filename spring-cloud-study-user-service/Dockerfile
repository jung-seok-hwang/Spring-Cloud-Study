FROM openjdk:17-jdk-slim

VOLUME /tmp

ENV SERVER_PORT=60000

COPY target/spring-cloud-study-user-service-0.0.1-SNAPSHOT.jar spring-cloud-study-user-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "spring-cloud-study-user-service-0.0.1-SNAPSHOT.jar"]