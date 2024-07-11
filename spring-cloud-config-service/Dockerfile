FROM openjdk:17-jdk-slim

VOLUME /tmp

ENV SERVER_PORT=8888

COPY target/spring-cloud-config-service-0.0.1-SNAPSHOT.jar spring-cloud-config-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "spring-cloud-config-service-0.0.1-SNAPSHOT.jar"]