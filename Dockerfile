FROM eclipse-temurin:21
LABEL maintainer="m42hub"
WORKDIR /app
COPY target/m42hub-api-0.0.1-SNAPSHOT.jar /app/m42hub-api.jar
ENTRYPOINT ["java", "-jar", "m42hub-api.jar"]
VOLUME ["/data"]