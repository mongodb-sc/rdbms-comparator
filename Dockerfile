#Build angular webapp
FROM node:alpine AS angular

COPY web/rdbms-comparator /usr/src/app/
WORKDIR /usr/src/app
RUN npm install -g @angular/cli
RUN npm install
RUN ng build -c docker



#Spring Spring Boot App
FROM gradle:jdk23-alpine AS gradle
COPY build.gradle /build/
COPY settings.gradle /build/
COPY data /build/data/
COPY src /build/src/
COPY --from=angular /usr/src/app/dist/rdbms-comparator/browser/ /build/src/main/resources/static/

WORKDIR /build
RUN gradle build --no-daemon


# Setup container to run app
EXPOSE 8080
FROM amazoncorretto:23-alpine3.20

WORKDIR /app

COPY --from=gradle /build/build/libs/RDBMSComparator-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "RDBMSComparator-0.0.1-SNAPSHOT.jar"]