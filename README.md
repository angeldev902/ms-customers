# Customer Microservice – Spring Boot

## 📌 Descripción

Este proyecto es un **microservicio backend** desarrollado con **Spring Boot 3.4.2** y **Java 21**, diseñado como parte de un sistema distribuido orientado a eventos.

El objetivo del proyecto es mostrar cómo estructuro y desarrollo microservicios reales utilizando buenas prácticas de arquitectura, separación de responsabilidades y comunicación asíncrona mediante eventos.

El microservicio es responsable de la **gestión de clientes** y expone operaciones CRUD, además de emitir eventos de dominio para notificar cambios a otros servicios del sistema.

---

## 🧱 Arquitectura

El proyecto sigue una **Arquitectura en Capas** combinada con principios de **Clean Architecture pragmática**:

```
Controller
   ↓
Service (lógica de negocio y transacciones)
   ↓
DAO (JDBC / Stored Procedures)
   ↓
Oracle Database
```

La comunicación con sistemas externos (base de datos y mensajería) se desacopla mediante **interfaces**, lo que permite:

* Cambiar Oracle por otro motor de base de datos
* Sustituir Kafka por otro broker (RabbitMQ, SNS, etc.)

sin afectar la lógica de negocio.

---

## ✨ Características principales

* CRUD de clientes
* Conexión a **Oracle DB** usando JDBC y Stored Procedures
* Uso de **DTOs** para requests y responses
* Validaciones con **Jakarta Validation**
* Manejo centralizado de excepciones
* Logging en puntos clave del flujo
* Configuración por ambientes (`local`, `dev`, `prod`)
* Manejo transaccional
* **Eventos de dominio con Kafka**:

    * `CUSTOMER_CREATED`
    * `CUSTOMER_UPDATED`
    * `CUSTOMER_DELETED`
* Health checks con **Spring Actuator**
* Pruebas unitarias con **JUnit 5** y **Mockito**

---

## 🛠️ Tecnologías utilizadas

* Java 21
* Spring Boot 3.4.2
* Spring Web
* Spring JDBC
* Spring Kafka
* Spring Actuator
* Oracle Database
* Apache Kafka
* Docker / Docker Compose
* JUnit 5 / Mockito

---

## 🚀 Ejecución del proyecto (alto nivel)

> ⚠️ **Nota**: Este repositorio no incluye los scripts SQL para la creación de paquetes y procedimientos en Oracle.

Para ejecutar el proyecto de forma local es necesario contar con la infraestructura mínima levantada mediante Docker.

### Requisitos

* Java 21
* Maven
* Docker
* Docker Compose

### Infraestructura necesaria

Antes de iniciar la aplicación, deben estar en ejecución los siguientes contenedores:

* Oracle Database
* Apache Kafka
* Zookeeper

Estos servicios pueden levantarse utilizando imágenes oficiales de Docker.

### Variables de entorno

El proyecto utiliza **variables de entorno** para la configuración sensible y dependiente del ambiente, por ejemplo:

* `DB_URL`
* `DB_USER`
* `DB_PASSWORD`
* `KAFKA_BOOTSTRAP_SERVERS`

Cada ambiente (`local`, `dev`, `prod`) cuenta con su propio archivo de configuración.

### Ejecución

Una vez que la infraestructura esté en ejecución y las variables de entorno configuradas:

```bash
mvn spring-boot:run
```

La aplicación iniciará y expondrá los endpoints REST junto con los health checks.

---

## 🧪 Pruebas

El proyecto incluye **pruebas unitarias** para la capa de servicio, utilizando mocks para desacoplar dependencias de infraestructura como base de datos y Kafka.

```bash
mvn test
```

---

## 📦 Eventos

El microservicio publica eventos de dominio en Kafka cuando ocurren cambios en el ciclo de vida del cliente. Estos eventos permiten la comunicación asíncrona con otros microservicios del sistema.

---

## 📄 Notas finales

Este proyecto tiene fines demostrativos y forma parte de mi proceso de mejora continua como desarrollador backend.

Feedback técnico y sugerencias son bienvenidos.
