# For java 8, try this
FROM openjdk:8-jdk-alpine

# Refer to Maven build ->  finalName
ARG JAR_FILE=target/blog8-1.0.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/blog8-1.0.jar /opt/app/blog8.jar
COPY ${JAR_FILE} blog8.jar

# java -jar /opt/app/blog8.jar
ENTRYPOINT ["java","-jar","blog8.jar"]