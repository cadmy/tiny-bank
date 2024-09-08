FROM openjdk:21
VOLUME /tmp
COPY target/*.jar tiny-bank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/tiny-bank.jar"]