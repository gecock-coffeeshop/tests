FROM maven:3.6-jdk-8-openj9

COPY src /project/src
COPY pom.xml /project/pom.xml