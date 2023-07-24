FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /acc-app

COPY . .

RUN mvn clean package -DskipTests

FROM ghcr.io/graalvm/graalvm-ce:ol9-java17-22.3.2

COPY --from=build /acc-app/target/*.jar /usr/local/lib/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]