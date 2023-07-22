FROM ghcr.io/graalvm/graalvm-ce:ol9-java11-22.3.2
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]