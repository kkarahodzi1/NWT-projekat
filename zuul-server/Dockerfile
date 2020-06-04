FROM openjdk:11
ADD target/zuul-gateway.jar zuul-gateway.jar
EXPOSE 8762
ENTRYPOINT ["java","-jar","zuul-gateway.jar"]