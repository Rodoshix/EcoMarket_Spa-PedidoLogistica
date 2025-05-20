# EcoMarket SPA - Microservicio Pedidos y Logística

Este microservicio gestiona el ciclo de vida de los pedidos en EcoMarket SPA del proyecto semestral FullStack_1, desde su creación hasta la entrega. Es activado automáticamente por `venta-service` tras una venta.

---

## Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Lombok
- MySQL
- Mailtrap (SMTP)

---

## Funcionalidades

- Crear pedidos con fecha y estado inicial `PENDIENTE`
- Cambiar el estado de un pedido (EN_PREPARACION, EN_REPARTO, ENTREGADO, CANCELADO)
- Buscar pedidos por:
  - ID
  - Cliente (email)
  - Estado
- Cancelar pedidos (si no han sido entregados)
- Envío automático de notificación por correo usando Mailtrap

---

## Configuración del entorno

### Base de datos

- Motor: MySQL (MariaDB compatible)
- Nombre: `pedidos_db`
- Usuario: `root`
- Contraseña: *(vacía por defecto en XAMPP)*

### Archivo `application.properties`

```properties
spring.application.name=pedidos-logistica

spring.datasource.url=jdbc:mysql://localhost:3306/pedidos_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true
server.port=8085

ecomarket.notificaciones.url=http://localhost:8087/api/notificaciones/enviar 

```

---


## Microservicios integrados

- **venta-service** (`localhost:8084`): envía los pedidos después de cada venta confirmada.
- **notificacion-service** (`localhost:8087`): envía notificaciones por crear pedido (Falta implementar notificacion al cambiar estado).

---

## Endpoints disponibles

Importa la colección incluida:  
**`EcoMarket - Pedido Logística.postman_collection.json`**

| Método  | Ruta                                         | Descripción                                                               |
|---------|----------------------------------------------|---------------------------------------------------------------------------|
| POST    | `/api/pedidos`                               | Crea un nuevo pedido a partir de una venta. Usado por `venta-service`.    |

**Body esperado:**
```json
{
  "idVenta": 123,
  "emailCliente": "cliente@example.com",
  "direccionDespacho": "Av. Siempre Viva 742"
}
```

| Método | Ruta                                           | Descripción                                                          |
|--------|------------------------------------------------|----------------------------------------------------------------------|
| GET    | `/api/pedidos/{id}`                            | Retorna los datos de un pedido por ID.                               |
| GET    | `/api/pedidos/cliente/{email}`                 | Retorna todos los pedidos de un cliente.                             |
| GET    | `/api/pedidos/estado/{estado}`                 | Filtra los pedidos por estado (ej: `EN_CAMINO`, `ENTREGADO`, etc.).  |
| PUT    | `/api/pedidos/{id}/estado?estado=NUEVO_ESTADO` | Actualiza el estado de un pedido.                                    |
| PUT    | `/api/pedidos/{id}/cancelar`                   | Cambia el estado del pedido a `CANCELADO`, si aún no está entregado. |


## Estados posibles del pedido

- `PENDIENTE`
- `EN_PREPARACION`
- `EN_CAMINO`
- `ENTREGADO`
- `CANCELADO`

---
