FROM openjdk:8 as build

WORKDIR /build

COPY .mvn /build/.mvn
COPY  pom.xml mvnw /build/

RUN ./mvnw dependency:go-offline

COPY src /build/src

RUN ./mvnw compile jar:jar spring-boot:repackage

FROM openjdk:8

WORKDIR /app

EXPOSE 8080

ENV DB_HOST=postgres

CMD java -jar customerservice.jar
COPY --from=build /build/target/customerservice.jar /app/customerservice.jar