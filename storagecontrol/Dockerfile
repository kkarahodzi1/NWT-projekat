FROM openjdk:11
ADD target/storage.jar storage.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","storage.jar"]