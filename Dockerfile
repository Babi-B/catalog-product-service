#FROM openjdk:8
FROM maven 
EXPOSE 8000
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
ADD target/catalog-project-service-03-docker.jar catalog-project-service-03-docker.jar
ENTRYPOINT ["java","-jar","/catalog-project-service-03-docker.jar"]

