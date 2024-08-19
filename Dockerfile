FROM openjdk:17
VOLUME /tmp
ARG JAR_FILE
COPY target/demo1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]