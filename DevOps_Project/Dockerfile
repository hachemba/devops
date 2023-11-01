FROM openjdk:8
EXPOSE 8082
RUN curl -O http://192.168.33.10:8081/repository/maven-releases/tn/esprit/DevOps_Project/1.0/DevOps_Project-1.0.jar
ENTRYPOINT ["java", "-jar", "/DevOps_Project-1.0.jar"]