# stage-1 build artifact
FROM amazoncorretto:17.0.9-alpine3.18 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build 

# stage-2 running image
FROM gcr.io/distroless/java17-debian12:latest

WORKDIR /app

COPY --from=builder ./app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]