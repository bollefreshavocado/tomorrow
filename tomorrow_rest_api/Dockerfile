
FROM openjdk:8-alpine
VOLUME /tmp
EXPOSE 8080
ADD /build/libs/rest-api-0.0.1-SNAPSHOT.jar rest-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","rest-api-0.0.1-SNAPSHOT.jar"]