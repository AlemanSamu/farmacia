FROM openjdk:17 
COPY "./target/FarmaciaGestion-1.jar" "app.jar 
EXPOSE "8105" 
ENTRYPOINT "java", "-jar", "app.jar" I

