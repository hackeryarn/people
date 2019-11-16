FROM openjdk:8-alpine

COPY target/uberjar/people-api.jar /people-api/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/people-api/app.jar"]
