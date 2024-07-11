FROM openjdk:17-jdk-slim

VOLUME /tmp

COPY target/spring-cloud-discovery-0.0.1-SNAPSHOT.jar spring-cloud-discovery-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "spring-cloud-discovery-0.0.1-SNAPSHOT.jar"]