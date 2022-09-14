FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar

#copy jar file and rename it
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]