FROM openjdk:17-alpine

WORKDIR /app

COPY /build/libs/places-api-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]