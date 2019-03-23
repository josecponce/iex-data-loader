FROM maven:3.6.0-jdk-8 AS build
COPY . /iex-data-loader
RUN mvn clean package -f /iex-data-loader

FROM openjdk:8-alpine
COPY --from=build /iex-data-loader/target/iex-data-loader-*-SNAPSHOT.jar /opt/iex-data-loader.jar
ENTRYPOINT ["java", "-jar", "/opt/iex-data-loader.jar"]