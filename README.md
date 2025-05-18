# EcoMarket SPA - Microservicio Pedidos y Logística

Este microservicio gestiona el ciclo de vida de los pedidos en EcoMarket SPA, desde su creación hasta la entrega. Es activado automáticamente por `venta-service` tras una venta.

---

## Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Lombok
- MySQL

---

## Funcionalidades

- Crear un pedido asociado a una venta.
- Registrar dirección de despacho y fecha de creación.
- Actualizar estado del pedido (PENDIENTE, EN_CAMINO, ENTREGADO, CANCELADO, etc.).
- Buscar pedidos por ID, cliente o estado.
- Cancelar pedidos antes de la entrega.

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
```

---


## Microservicios integrados

- **venta-service** (`localhost:8084`): envía los pedidos después de cada venta confirmada.

---

## Endpoints disponibles

| Método  | Ruta                                         | Descripción                                                       |
|---------|----------------------------------------------|-------------------------------------------------------------------|
| POST    | `/api/pedidos`                       | Crea un nuevo pedido a partir de una venta. Usado por `venta-service`.    |

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
