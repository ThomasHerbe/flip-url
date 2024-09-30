FROM openjdk:17-jdk
COPY ./build /app
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "libs/challenge-0.0.1-SNAPSHOT.jar"]