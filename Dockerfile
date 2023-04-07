FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8081

ADD build/libs/conditional-0.0.1-SNAPSHOT.jar myapp.jar

ENTRYPOINT ["java", "-jar", "/myapp.jar"]