FROM eclipse-temurin:17-jre-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} coding-challenge.jar

ENTRYPOINT ["java","-jar","/coding-challenge.jar"]

EXPOSE 8080
