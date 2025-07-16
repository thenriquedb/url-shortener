FROM maven:3.9.10-eclipse-temurin-21-alpine AS build

WORKDIR /usr/app

COPY pom.xml ./
COPY src ./src

RUN mvn install
RUN mvn package

FROM eclipse-temurin:21-jre-alpine AS production

WORKDIR /app

COPY --from=build /usr/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]