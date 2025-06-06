FROM openjdk:17
COPY "./target/FarmaciaGestion-1.jar" "app_jar"
EXPOSE 8105
ENTRYPOINT ["java", "-jar", "app_jar"]

