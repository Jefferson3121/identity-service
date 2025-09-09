# identity-service 🛡️

Microservicio de autenticación y gestión de usuarios para sistemas distribuidos.

## 🧾 Descripción

Este servicio permite registrar nuevos usuarios, iniciar sesión, modificar datos, cambiar contraseñas y eliminar cuentas. Toda la información se almacena en una base de datos PostgreSQL utilizando JPA para la persistencia. Es parte de una arquitectura basada en microservicios.

## ⚙️ Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security (parcialmente implementado)
- PostgreSQL
- Maven
- Lombok
- MapStruct

> 💡 *Nota:* Aunque Lombok y MapStruct son librerías pequeñas, son esenciales para reducir el código repetitivo y mejorar la legibilidad, así que sí vale la pena mencionarlas.

## 📦 Requisitos previos

> ⚠️ Aún en desarrollo — esta sección se actualizará conforme se definan los pasos exactos.

- Java 17+
- Maven
- PostgreSQL (local o en contenedor)
- Docker (opcional)

## 🚀 Cómo ejecutar el proyecto

```bash
# Ejecutar con Maven
./mvnw spring-boot:run

# O si usas Maven instalado globalmente
mvn spring-boot:run

Estado del proyecto
En desarrollo activo. Algunas funcionalidades están en fase de prueba y otras por implementar.

👤 Autor
Desarrollado por Jefferson Escorcia — en proceso de aprendizaje y construcción de un sistema completo basado en microservicios.
