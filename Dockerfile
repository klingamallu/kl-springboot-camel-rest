FROM openjdk:8-jdk-alpine
RUN addgroup -S demo && adduser -S demo -G demo
USER demo:demo
ARG JAR_FILE=target/kl-springboot-camel-rest-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]