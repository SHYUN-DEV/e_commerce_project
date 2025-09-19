FROM ubuntu:latest
LABEL authors="user"


FROM openjdk:17-alpine

ARG JAR_FILE=./build/libs/newECommerce-1.0.0.jar

COPY ${JAR_FILE} /newECommerce.jar

ENTRYPOINT java -Dspring.profiles.active=${PROFILE} -jar /newECommerce.jar