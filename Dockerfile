FROM adoptopenjdk:16-jre-hotspot
ADD target/luxbank-rest.jar luxbank-rest.jar
ENTRYPOINT ["java", "-jar", "luxbank-rest.jar"]
