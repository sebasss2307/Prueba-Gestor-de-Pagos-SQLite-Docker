# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instalar wget para healthcheck
RUN apk add --no-cache wget

# Copiar el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Copiar archivos CSV necesarios para la inicialización
COPY src/main/resources/*.csv /app/

# Crear directorio para la base de datos
RUN mkdir -p /app/data

# Exponer el puerto
EXPOSE 8080

# Variables de entorno
ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/recibos.db

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
