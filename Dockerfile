FROM openjdk:17-alpine

LABEL authors="user"

ARG JAR_FILE=./build/libs/newECommerce-1.0.0.jar
COPY ${JAR_FILE} /newECommerce.jar

ENTRYPOINT java -Dspring.profiles.active=${PROFILE} -jar /newECommerce.jar


#FROM openjdk:17-alpine
#
#LABEL authors="user"
#
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#
#ENV PROFILE=dev
#ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=$PROFILE -jar /app.jar"]