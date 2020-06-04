FROM openjdk:11
ADD target/notifications.jar notifications.jar
EXPOSE 8081 8084 587
ENTRYPOINT ["java","-jar","notifications.jar"]