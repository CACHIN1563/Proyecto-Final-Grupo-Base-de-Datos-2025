# ✈️ Sistema de Gestión para Agencia de Viajes 

## 📑 1. Introducción y Propósito

El presente repositorio alberga el código fuente y el Manual Técnico del **Sistema de Gestión para Agencia de Viajes** (SGA). Este sistema fue desarrollado como proyecto final para el curso **Bases de Datos 1**.

Su propósito es proporcionar una guía completa sobre el funcionamiento interno del software, su diseño y las tecnologías empleadas, facilitando la comprensión y el mantenimiento a futuros desarrolladores y personal técnico.

---

## 🚀 2. Descripción General del Sistema

El SGA es una **aplicación de escritorio** desarrollada en **Java (Swing)** enfocada en la gestión de servicios turísticos. Su objetivo principal es permitir el **mantenimiento (ABCC: Alta, Baja, Cambio, Consulta)** de los registros almacenados en la base de datos.

### 2.1 Objetivos Técnicos

* Desarrollar una aplicación con arquitectura modular basada en el patrón **MVC**.
* Implementar la conexión con **MySQL 8.0** mediante **JDBC**.
* Aplicar un modelo de datos relacional normalizado hasta la **Tercera Forma Normal (3FN)**.
* Facilitar la mantenibilidad y escalabilidad del sistema.

### 2.2 Alcance

El sistema abarca el mantenimiento de las siguientes entidades principales:

* **Hoteles** y **Tarifas** asociadas.
* **Vuelos** (Orígenes y Destinos).
* **Hospedajes** (Registros de estadías).
* **Países**, **Sucursales** y **Roles** de usuario.

---

## 🛠️ 3. Requerimientos Técnicos

### 3.1 Software

| Requerimiento | Versión Mínima | Propósito |
| :--- | :--- | :--- |
| **Lenguaje** | Java SE 8+ | Entorno de Ejecución (JRE) y Desarrollo (JDK). |
| **Base de Datos** | MySQL 8.0 | Servidor de base de datos relacional. |
| **Driver JDBC** | `mysql-connector-j-8.0.xx.jar` | Conector de Java a MySQL. |
| **Bibliotecas GUI** | Java Swing | Interfaz gráfica de usuario. |
| **IDE recomendado** | NetBeans / IntelliJ IDEA / Eclipse | Entorno de desarrollo. |

### 3.2 Hardware

* **Procesador:** Intel i5 o superior.
* **Memoria RAM:** 8 GB mínimo.
* **Espacio en Disco:** 500 MB libres para la aplicación y base de datos.

---

## 🏗️ 4. Arquitectura de Software (Patrón MVC)

El sistema implementa el patrón **Modelo – Vista – Controlador (MVC)**, garantizando una separación clara entre la lógica de presentación, negocio y acceso a datos.

### 4.1 Paquetes Principales

| Paquete | Rol en MVC | Descripción |
| :--- | :--- | :--- |
| `controller` | **Controlador** | Gestiona la lógica del negocio y la interacción entre la vista y el modelo. |
| `model` | **Modelo** (Entidades) | Clases POJO que representan las tablas de la base de datos (Ej: `hotel.java`, `vuelo.java`). |
| `dao` | **Modelo** (Acceso a Datos) | Implementa las operaciones CRUD y la comunicación directa con MySQL (Ej: `hoteldao.java`). |
| `view` | **Vista** | Define las interfaces gráficas de usuario (formularios Swing) para la interacción. |
| `util` | Soporte | Maneja la conexión a la base de datos (`DB.java`) y utilidades comunes. |

### 4.2 Estructura del Proyecto
src/ │ ├── controller/ # AuthController.java ├── dao/ # hoteldao.java, vuelodao.java, etc. (CRUD) ├── model/ # hotel.java, vuelo.java, pais.java, rol.java, etc. (Entidades) ├── util/ # DB.java (Conexión JDBC) └── view/ # LoginView.java, Vistas de Mantenimiento └── Main.java # Punto de entrada de la aplicación
## 💾 5. Modelo de Datos y Entidades

El diseño relacional está estructurado y normalizado hasta **3FN**, lo que asegura la eliminación de redundancias y la integridad referencial de los datos.

### 5.1 Entidades Clave y Relaciones

| Entidad | Propósito | Relación Clave |
| :--- | :--- | :--- |
| **País** | Tabla maestra para ubicación geográfica. | Referenciada por `Sucursal` y `Vuelo` (Origen/Destino). |
| **Hotel** | Información principal del establecimiento. | Se relaciona con `TarifaHotel` y `Hospedaje`. |
| **Vuelo** | Registra el transporte aéreo. | Claves foráneas `origen` y `destino` apuntan a `País`. |
| **TarifaHotel** | Tabla intermedia: Asocia un hotel con un tipo de habitación y un precio. | Permite establecer el precio por noche y régimen. |
| **Hospedaje** | Almacena los registros de estadías de clientes. | Se asocia a un `Hotel`. |
| **Rol** | Define los distintos perfiles de usuario y sus privilegios. | Base para el control de acceso y autenticación. |

### 5.2 Clases de Acceso a Datos (DAO)

Las clases DAO (Data Access Object) contienen la lógica para interactuar con MySQL:

* **`hoteldao`:** Implementa los métodos **CRUD** (`insert`, `update`, `delete`, `listar`) para la entidad Hotel.
* Las operaciones utilizan sentencias SQL parametrizadas a través de **`PreparedStatement`**, protegiendo contra la inyección SQL.
* **`DB.java`:** Encargada de establecer la conexión con el servidor MySQL mediante JDBC.

---

## ⚙️ 6. Instalación y Flujo de Ejecución

### 6.1 Procedimiento de Instalación

1.  **Clonar el Repositorio:** Obtenga el código fuente en su máquina local.
2.  **Configurar MySQL:** Cree la base de datos (`hotel_db`) y ejecute el script SQL para crear las tablas.
3.  **Actualizar Credenciales:** Modifique `URL`, `USER` y `PASS` en la clase `util/DB.java`.
4.  **Añadir Driver:** Incluya el archivo `mysql-connector-j-8.0.xx.jar` en el *classpath* del proyecto en su IDE.
5.  **Ejecutar:** Ejecute la clase `Main.java`.

### 6.2 Flujo de Ejecución

1.  **Inicio:** Se ejecuta `Main`, configurando el entorno visual y cargando `LoginView`.
2.  **Autenticación:** `AuthController` gestiona la validación del usuario contra la tabla de usuarios (usando la entidad `Rol` para permisos).
3.  **Operación:** Al seleccionar un módulo, el sistema carga la Vista correspondiente, la cual interactúa con el Controlador y el DAO para realizar las operaciones CRUD en la base de datos.

---

## 📈 7. Mantenimiento y Extensibilidad

El diseño modular permite la fácil extensión del sistema sin alterar la estructura fundamental:

* **Extensibilidad:** El diseño basado en 3FN permite añadir nuevas entidades (ej. `cliente`, `reserva`, `facturacion`) creando sus respectivos `model`, `dao`, `controller` y `view` sin afectar las entidades existentes.
* **Recomendaciones:** Se aconseja respaldar la base de datos periódicamente y mantener actualizada la versión del driver JDBC.
* **Triggers Sugeridos:** La base de datos puede ser ampliada con *triggers* de auditoría, como:
    ```sql
    CREATE TRIGGER trg_aud_hotel_update
    AFTER UPDATE ON hotel
    FOR EACH ROW
    INSERT INTO auditoria_hotel (id_hotel, accion, fecha) 
    VALUES (NEW.id_hotel, 'ACTUALIZACIÓN', NOW());
    ```

| `util` | Soporte | Maneja la conexión a la base de datos (`DB.java`) y utilidades comunes. |

### 4.2 Estructura del Proyecto
