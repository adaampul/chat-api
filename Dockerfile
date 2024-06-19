FROM gradle:8.5.0-jdk17-alpine as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM amazoncorretto:17-alpine

EXPOSE 8080

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/chat-api-0.0.1-SNAPSHOT.jar /app/chat-api/chat-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/chat-api/chat-api-0.0.1-SNAPSHOT.jar"]
