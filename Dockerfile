FROM openjdk:8-alpine
ADD target/iex-data-loader-0.0.1-SNAPSHOT.jar /opt/iex-data-loader.jar
ENTRYPOINT ["java", "-jar", "/opt/iex-data-loader.jar"]