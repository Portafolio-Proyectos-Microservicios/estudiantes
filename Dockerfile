FROM maven:3.8.4-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-alpine
EXPOSE 57963
COPY --from=build /home/app/target/estudiantes-0.0.1-SNAPSHOT.jar app/app.jar
ENTRYPOINT ["java", "-jar","/app/app.jar"]