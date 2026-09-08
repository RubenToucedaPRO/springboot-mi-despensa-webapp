<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/PantallaPrincipal.PNG?raw=true" alt="Mi Despensa" width="75%">
</p>

<h1 align="center">Mi Despensa</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring_Boot-3.4.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.4.5">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL 8.0">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License MIT">
</p>

<p align="center">
  Aplicación web para gestionar productos de la despensa y la lista de la compra mediante escaneo de código de barras o de forma manual.<br>
  Diseñada para usarse principalmente en el móvil, aunque accesible desde cualquier dispositivo.
</p>

---

## Sobre el Proyecto

**Mi Despensa** es una aplicación web desarrollada con Spring Boot que permite gestionar de forma sencilla los productos de tu despensa y tu lista de la compra desde cualquier dispositivo con navegador, especialmente pensada para usarse en el móvil.

Puedes **añadir productos** mediante la lectura de códigos de barras o manualmente, y mantener siempre un inventario actualizado: cuando consumes un producto, lo mueves a la **lista de la compra**, y al reponerlo lo reintroduces en la despensa.

Además, puedes crear **productos personalizados** (como alimentos frescos o artículos sin código de barras) que solo tú podrás usar y editar.

La **lista de la compra** se puede enviar directamente a tu correo electrónico para que la lleves contigo a cualquier lugar.

La aplicación también incorpora una sección de **recetas colaborativas**, donde los usuarios pueden **crear, consultar y compartir recetas** con la comunidad, construyendo juntos un **recetario colectivo en constante crecimiento**.

---

## Funcionalidades Principales

### Gestión de Productos
- Añadir productos a la despensa mediante escaneo de código de barras o de forma manual.
- Mover productos de la despensa a la lista de la compra y viceversa.
- Crear productos personalizados (sin código de barras).
- Visualización y edición de productos por parte del usuario que los creó.

### Lista de la Compra
- Añadir productos desde la despensa.
- Enviar la lista de la compra al correo electrónico del usuario.

### Gestión de Usuarios
- Registro con verificación de correo electrónico.
- Inicio de sesión seguro.
- Restablecimiento de contraseña mediante enlace enviado por email.
- Cambiar correo electrónico desde la sección de ajustes (requiere nueva verificación del correo).
- Cambiar contraseña desde la sección de ajustes.

### Escaneo de Productos (Códigos de Barras)
- Buscar el producto en la base de datos por usuario.
- Si no existe, buscar productos públicos (`idUser = 1`).
- Si no se encuentra, consultar la API de OpenFoodFacts.
- Si tampoco existe en la API, solicitar datos manualmente y guardar el producto como personalizado.

### Recetas
- Consultar recetas públicas.
- Crear recetas propias y elegir si son públicas o privadas.
- Filtrar recetas por categoría o buscar por ingredientes.

### Roles de Usuario
- **USER**: Acceso a funciones básicas de gestión de productos, recetas y lista de la compra.
- **ADMIN**: Ver y editar todos los productos, sincronizar con la API, gestionar usuarios y convertir productos personalizados en públicos.

### Seguridad y Autenticación
- Control de roles (`USER` y `ADMIN`) con Spring Security.
- Verificación por token para validar cuentas y cambios de correo o restablecimiento de contraseña.

### Contacto y Privacidad
- Formulario de contacto para enviar mensajes al administrador.
- Página de política de privacidad.

---

## Stack Tecnológico

| Tecnología | Uso |
|------------|-----|
| **Java 17** | Lenguaje de programación principal |
| **Spring Boot 3.4.5** | Framework principal de desarrollo |
| **Spring Data JPA** | Gestión de la base de datos con JPA y Hibernate |
| **Spring Security** | Seguridad, autenticación y autorización |
| **Spring Boot Mail** | Envío de correos electrónicos |
| **Thymeleaf** | Motor de plantillas para renderizar vistas en el servidor |
| **Thymeleaf Layout Dialect** | Reutilización de layouts en Thymeleaf |
| **Bootstrap** | Framework CSS para diseño responsivo |
| **MySQL 8.0** | Base de datos relacional |
| **MapStruct** | Mapeo entre entidades y DTOs |
| **Lombok** | Reducción de código boilerplate |
| **OpenFoodFacts Java Wrapper** | Consulta de API de productos alimenticios |
| **Cloudinary SDK** | Almacenamiento de imágenes en la nube |
| **html5-qrcode** | Escaneo de códigos de barras desde el navegador |
| **Tagify** | Gestión de etiquetas e ingredientes |
| **Thumbnailator** | Redimensionamiento de imágenes |
| **Dotenv Java** | Carga de variables de entorno desde archivos `.env` |
| **Maven** | Gestión de dependencias |
| **Docker** | Contenerización y despliegue |
| **Git + GitHub** | Control de versiones |

---

## Arquitectura

La aplicación está organizada siguiendo una **arquitectura por capas**, separando las responsabilidades de cada parte del sistema:

<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/Programa.PNG?raw=true" alt="Estructura" width="30%">
</p>

### Backend (Spring Boot)

| Paquete | Responsabilidad |
|---------|-----------------|
| **Configs** | Configuraciones de correo, Spring Security e imágenes |
| **Controllers** | Peticiones HTTP y respuestas (`@Controller`, `@RestController`) |
| **DTOs** | Transferencia de datos entre backend y frontend |
| **Entities** | Representación de tablas de la base de datos (`@Entity`) |
| **Exceptions** | Control centralizado de excepciones (`@RestControllerAdvice`) |
| **Mappers** | Mapeo entre DTOs y entidades |
| **Repositories** | Interfaces JPA para operaciones con la BD |
| **Services** | Lógica de negocio de la aplicación |

### Frontend (Thymeleaf + JavaScript)

- **Templates**: Vistas HTML organizadas por funcionalidad (Pantry, Product, Recipe, ShoppingList, User).
- **Layouts**: Plantillas base reutilizables (`layout.html`, `layout-scanner.html`, `layout-recipes.html`).
- **Fragments**: Componentes reutilizables (tablas, alertas, navbar).
- **Static**: CSS, iconos, imágenes y scripts JavaScript.
- **Mails**: Fragmentos HTML para correos electrónicos.

---

## Cómo Ejecutar

### Opción A — Docker (Recomendada)

**Prerrequisitos:** [Docker](https://docs.docker.com/get-docker/) y [Docker Compose](https://docs.docker.com/compose/install/) instalados.

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp.git
   cd springboot-mi-despensa-webapp
   ```

2. **Configurar las variables de entorno:**
   ```bash
   cp .env.example .env
   ```
   Edita el archivo `.env` con tus credenciales (ver [Variables de Entorno](#variables-de-entorno)).

3. **Levantar la aplicación:**
   ```bash
   docker compose up --build
   ```

4. **Acceder a la aplicación:**
   Abre `https://localhost:8081` en tu navegador.

> **Nota:** La primera vez, MySQL inicializa la base de datos automáticamente con el script `scripts/MiDespensa_BD_v12.sql`.

### Opción B — Ejecución Local

**Prerrequisitos:** Java 17, Maven y MySQL 8.0 instalados.

1. **Crear la base de datos:**
   Ejecuta el script `scripts/MiDespensa_BD_v12.sql` en MySQL Workbench o desde la línea de comandos.

2. **Configurar las variables de entorno:**
   ```bash
   cp .env.example .env
   ```
   Edita el archivo `.env` apuntando a tu instancia local de MySQL (ver [Variables de Entorno](#variables-de-entorno)).

3. **Ejecutar la aplicación:**
   ```bash
   # Con Maven Wrapper
   ./mvnw spring-boot:run

   # O desde Eclipse: botón derecho → Run As → Java Application
   ```

4. **Acceder a la aplicación:**
   Abre `https://localhost:8081` en tu navegador.

### Credenciales de Prueba

| Rol | Usuario | Contraseña |
|-----|---------|------------|
| **Admin** | `admin@admin.com` | `admin` |
| **User** | `user@user.com` | `user` |

> **Nota:** Las cuentas de prueba no permiten funcionalidades que dependan del correo electrónico, ya que usan correos ficticios.

---

## Estructura del Proyecto

```
springboot-mi-despensa-webapp/
├── src/
│   └── main/
│       ├── java/com/midespensa/
│       │   ├── configs/          # Configuraciones (Mail, Security, etc.)
│       │   ├── controllers/      # Controladores HTTP
│       │   ├── dtos/             # Data Transfer Objects
│       │   ├── entities/         # Entidades JPA
│       │   ├── exceptions/       # Manejo centralizado de excepciones
│       │   ├── mappers/          # Mapeo DTO ↔ Entity
│       │   ├── repositories/     # Interfaces JPA
│       │   ├── services/         # Lógica de negocio
│       │   └── MidespensaApplication.java
│       └── resources/
│           ├── static/           # CSS, JS, imágenes
│           ├── templates/        # Vistas Thymeleaf
│           │   ├── fragments/    # Componentes reutilizables
│           │   ├── layouts/      # Plantillas base
│           │   ├── mails/        # Plantillas de correos
│           │   ├── pantry/       # Vistas de despensa
│           │   ├── product/      # Vistas de productos (admin)
│           │   ├── recipe/       # Vistas de recetas
│           │   ├── shoppinglist/ # Vistas de lista de la compra
│           │   └── user/         # Vistas de usuario
│           ├── keystore.p12      # Certificado SSL
│           └── application.properties
├── scripts/
│   └── MiDespensa_BD_v12.sql    # Script de inicialización de la BD
├── docker-compose.yml            # Configuración Docker
├── Dockerfile                    # Build multi-etapa
├── .env.example                  # Plantilla de variables de entorno
└── pom.xml                       # Dependencias Maven
```

---

## Variables de Entorno

Copia `.env.example` como `.env` y configura los siguientes valores:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `CLOUDINARY_URL` | URL de conexión a Cloudinary para imágenes | `cloudinary://API_KEY:API_SECRET@CLOUD_NAME` |
| `SERVER_PORT` | Puerto del servidor | `8081` |
| `DB_URL` | URL de conexión a MySQL | `jdbc:mysql://localhost:3306/midespensa_bd` |
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASS` | Contraseña de MySQL | `tu_password` |
| `SERVER_URL_MAIL` | URL pública de la aplicación (para enlaces en correos) | `https://localhost:8081` |
| `MAIL_HOST` | Servidor SMTP | `smtp.gmail.com` |
| `MAIL_PORT` | Puerto SMTP | `587` |
| `MAIL_USER` | Correo de envío | `tu_email@gmail.com` |
| `MAIL_PASS` | Contraseña de aplicación del correo | `tu_app_password` |
| `SSL_ENABLE` | Habilitar HTTPS | `True` / `False` |
| `SSL_STORE` | Ruta al keystore SSL | `classpath:keystore.p12` |
| `SSL_PASS` | Contraseña del keystore | `tu_ssl_password` |
| `SSL_TYPE` | Tipo de keystore | `PKCS12` |
| `SSL_ALIAS` | Alias del certificado | `midespensa` |

---

## Seguridad

### HTTPS / SSL
La aplicación requiere HTTPS para funcionar correctamente, ya que la funcionalidad de escaneo de código de barras necesita acceso a la cámara del navegador, la cual solo está disponible en contextos seguros (HTTPS).

El keystore se genera con `keytool`:
```bash
keytool -genkeypair -alias midespensa -keyalg RSA -keysize 2048 \
  -validity 365 -keystore src/main/resources/keystore.p12 \
  -storetype PKCS12 -storepass tu_password \
  -dname "CN=localhost"
```

### Spring Security
- **Autenticación** basada en correo electrónico y contraseña.
- **Autorización** por roles: `USER` y `ADMIN`.
- **Tokens** para verificación de cuentas, cambios de correo y restablecimiento de contraseñas (validez de 24 horas).
- **CSRF** habilitado por defecto.
- **Contraseñas** cifradas con BCrypt.

---

## API Externa — OpenFoodFacts

La aplicación integra la API de [OpenFoodFacts](https://world.openfoodfacts.org/) para obtener información de productos alimenticios a partir de su código de barras.

### Flujo de escaneo

```
Código de barras escaneado
        │
        ▼
¿Existe en la BD del usuario? ──Sí──▶ Mostrar producto
        │
        No
        ▼
¿Existe en productos públicos? ──Sí──▶ Mostrar producto
        │
        No
        ▼
Consultar API OpenFoodFacts ──Encontrado──▶ Guardar y mostrar
        │
        No encontrado
        ▼
Solicitar datos manualmente ──▶ Guardar como personalizado
```

Se utiliza la librería `openfoodfacts-java-wrapper` para simplificar la consulta a la API.

---

## Cloudinary

La aplicación utiliza [Cloudinary](https://cloudinary.com/) como servicio de almacenamiento en la nube para las imágenes asociadas a las recetas subidas por los usuarios.

**Flujo de imagen:**
1. El usuario sube una imagen desde el formulario de recetas.
2. En el backend, **Thumbnailator** redimensiona la imagen para optimizar el tamaño.
3. La imagen se sube a Cloudinary mediante el SDK de Java.
4. Se almacena la URL de la imagen en la base de datos.

Esta aproximación garantiza escalabilidad y rendimiento sin necesidad de almacenar archivos localmente en el servidor.

---

## Tests

La aplicación incluye **pruebas unitarias** utilizando **JUnit 5** y **Mockito**, enfocadas en servicios y métodos con lógica crítica.

### Ejecutar tests

```bash
# Con Maven Wrapper
./mvnw test

# O con Maven instalado
mvn test
```

<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/TestUnitariosProductService.PNG?raw=true" alt="Tests unitarios" width="50%">
</p>

<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/TestUnitariosProductServiceCobertura.PNG?raw=true" alt="Cobertura de tests" width="50%">
</p>

---

## Capturas de Pantalla

<details>
<summary><strong>Pantallas Públicas (sin autenticación)</strong></summary>

**Pantalla principal**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/PantallaPrincipal.PNG?raw=true" alt="Pantalla principal" width="75%">
</p>

**Inicio de sesión**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/InicioSesion.PNG?raw=true" alt="Inicio de sesión" width="75%">
</p>

**Registro**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/Registro.PNG?raw=true" alt="Registro" width="75%">
</p>

**Formulario de contacto**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/FormularioDeContacto.PNG?raw=true" alt="Formulario de contacto" width="75%">
</p>

</details>

<details>
<summary><strong>Pantallas de Usuario (rol USER)</strong></summary>

**Despensa**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/Despensa.PNG?raw=true" alt="Despensa" width="75%">
</p>

**Escáner de código de barras**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/Escaner.PNG?raw=true" alt="Escáner" width="75%">
</p>

**Lista de la compra**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/ListaDeLaCompra.PNG?raw=true" alt="Lista de la compra" width="75%">
</p>

**Recetas**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/Recetas.PNG?raw=true" alt="Recetas" width="75%">
</p>

</details>

<details>
<summary><strong>Pantallas de Administrador (rol ADMIN)</strong></summary>

**Gestión de productos**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/AdminProductos.PNG?raw=true" alt="Admin productos" width="75%">
</p>

**Productos personalizados**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/AdminProductosPersonalizados.PNG?raw=true" alt="Admin productos personalizados" width="75%">
</p>

**Gestión de usuarios**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/AdminUsuarios.PNG?raw=true" alt="Admin usuarios" width="75%">
</p>

</details>

<details>
<summary><strong>Vista Móvil</strong></summary>

<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/PantallaPrincipalMovil.PNG?raw=true" alt="Móvil principal" width="30%">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/DespensaMovil.PNG?raw=true" alt="Móvil despensa" width="30%">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp/blob/main/images/ListaDeLaCompraMovil.PNG?raw=true" alt="Móvil lista compra" width="30%">
</p>

</details>

---

## Licencia

Distribuido bajo la licencia MIT. Ver `LICENSE` para más información.

---

## Autor

**Rubén Touceda**

- GitHub: [@RubenToucedaPRO](https://github.com/RubenToucedaPRO)
