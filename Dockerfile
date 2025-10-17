# Etapa 1: construir el JAR con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiamos primero el pom.xml para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Luego copiamos el código fuente
COPY src ./src

# Compilamos el proyecto y generamos el .jar (sin tests)
RUN mvn clean package -DskipTests=true

# Etapa 2: imagen final más liviana para ejecutar el microservicio
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiamos el jar desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Puerto que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]
