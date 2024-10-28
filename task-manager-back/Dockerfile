# Usa una imagen base de Java
FROM openjdk:11-jre-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR de la aplicación
COPY target/*.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
