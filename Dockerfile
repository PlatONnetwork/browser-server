FROM fabric8/java-alpine-openjdk8-jre:latest
ADD *.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=online"]