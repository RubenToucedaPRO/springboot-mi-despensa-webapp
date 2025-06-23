# Info proyecto - Mi Despensa

## Aplicación web para la gestión de la despensa

Aplicación para gestionar productos de la despensa y la lista de la compra, añadiéndolos a dichas listas mediante la lectura de códigos de barras o de forma manual. Está pensada principalmente para usarse desde el móvil, aunque es accesible desde cualquier dispositivo.

El objetivo principal de esta aplicación es mantener actualizada tu despensa. A medida que vayas consumiendo productos, puedes moverlos a tu **lista de la compra**, y cuando vuelvas a adquirirlos, los reintroduces en la despensa. Así, siempre tendrás un inventario actualizado.

Puedes crear **productos personalizados** (por ejemplo, carne o vegetales frescos que no tienen código de barras o productos con código de barras pero con datos proporcionados por ti manualmente). Estos productos son utilizables  y editables únicamente por el usuario que los creó.

La **lista de la compra** puede ser enviada por correo electrónico al email del usuario.

La aplicación cuenta con dos tipos de usuarios: **usuarios estándar** con rol `USER` (asignado automáticamente al registrarse) y un **usuario administrador** con rol `ADMIN` (este rol solo lo tiene el usuario con `id=1` y está fijado en la base datos).

## Funcionalidades principales de "Mi Despensa"

### Gestión de productos
- Añadir productos a la despensa mediante escaneo de código de barras o de forma manual.
- Mover productos de la despensa a la lista de la compra y viceversa.
- Crear productos personalizados (sin código de barras).
- Visualización y edición de productos por parte del usuario que los creó.

### Lista de la compra
- Añadir productos desde la despensa.
- Enviar la lista de la compra al correo electrónico del usuario.

### Gestión de usuarios
- Registro con verificación de correo electrónico.
- Inicio de sesión seguro.
- Restablecimiento de contraseña mediante enlace enviado por email.
- Cambiar correo electrónico desde la sección de ajustes (requiere nueva verificación del correo).
- Cambiar contraseña desde la sección de ajustes.

### Escaneo de productos (códigos de barras)
- Buscar el producto en la base de datos por usuario.
- Si no existe, buscar productos públicos (`idUser = 1`).
- Si no se encuentra, consultar la API de OpenFoodFacts.
- Si tampoco existe en la API, solicitar datos manualmente y guardar el producto como personalizado.

### Recetas
- Consultar recetas públicas.
- Crear recetas propias y elegir si son públicas o privadas.
- Filtrar recetas por categoría o buscar por ingredientes.

### Roles de usuario
- `USER`: 
  - Acceso a funciones básicas de gestión de productos, recetas y lista de la compra.
- `ADMIN`: 
  - Ver y editar todos los productos.
  - Sincronizar productos con la API.
  - Gestionar usuarios (incluida su eliminación).
  - Convertir productos personalizados en públicos.

### Seguridad y autenticación
- Control de roles (`USER` y `ADMIN`) con Spring Security.
- Verificación por token para validar cuentas y cambios de correo.

### Contacto y privacidad
- Formulario de contacto para enviar mensajes al administrador.
- Página de política de privacidad.  

## Resumen de Tecnologías

- **Spring Boot**: Framework principal para el desarrollo.
- **Spring Data JPA**: Gestión de la base de datos con JPA y Hibernate.
- **Spring Security**: Seguridad, autenticación y autorización.
- **Spring Boot DevTools**: Recarga automática y herramientas de desarrollo.
- **Spring Boot Validation**: Validación de datos con anotaciones estándar (javax.validation, ahora jakarta.validation).
- **Spring Boot Mail**: Envío de correos electrónicos desde la aplicación.
- **Thymeleaf**: Motor de plantillas para renderizar vistas en el servidor.
- **Thymeleaf Layout Dialect**: Extensión para reutilizar layouts en Thymeleaf.
- **Thymeleaf Extras Spring Security 6**: Integración entre Thymeleaf y Spring Security.
- **MapStruct**: Mapeo entre diferentes tipos de objetos.
- **MySQL**: Base de datos relacional.
- **Lombok**: Reducción de código con uso de anotaciones.
- **Jakarta Servlet**: API de servlets para manejar peticiones HTTP.
- **OpenFoodFacts Java Wrapper**: Cliente Java para consultar la API de OpenFoodFacts.
- **Cloudinary Java SDK**: Gestión de imágenes en la nube con Cloudinary.
- **Dotenv Java**: Carga de variables de entorno desde archivos .env.
- **Spring Boot Starter Test + Spring Security Test**: Dependencias para realizar pruebas unitarias e integradas, incluyendo seguridad.

## Base de datos:
La base de datos está implementada en **MySQL**.
  ![Estructura de la base de datos](https://github.com/RubenToucedaPRO/ProyectoFinCursoDAM/blob/main/Documentacion/imagenes/EstructuraBD.png)

 Los productos pueden provenir de distintas fuentes:
- **API OpenFoodFacts**: Productos obtenidos automáticamente a partir del código de barras, siempre llevarán `idUser=1` para ser visibles a todos los usuarios.
- **Administrador (`idUser = 1`)**: Productos añadidos por el administrador.
- **Usuarios**: Productos añadidos manualmente por los propios usuarios, asociados a su `idUser`.


## Roles y credenciales:

La aplicación cuenta con dos roles: **ADMIN** y **USER**.

- **Admin:** Permite administrar todos los productos de la aplicación, crear productos visibles para todos, listar y gestionar usuarios, editar cualquier producto, gestionar productos personalizados de los usuarios y eliminar recetas.
- **User:** Puede gestionar su despensa, su lista de compras y ver las recetas publicadas.

### Credenciales de prueba:

**Admin:**  
- **Usuario:** `admin@admin.com`  
- **Contraseña:** `admin`

**User:**  
- **Usuario:** `user@user.com`  
- **Contraseña:** `user`

## Pasos para la ejecución de la aplicación:

1. Ejecutar el script `MiDespensa_BD_v12.sql`, ubicado e la carpeta del proyecto `Midespensa/BaseDeDatos`, para crear la base de datos en MySQL.
2. Ejecutar la aplicación de una de las siguientes formas:

   ### Opción A – Desde Eclipse:
   1. Abrir el proyecto en Eclipse.
   2. Ejecutar la clase principal desde el entorno (botón derecho → Run As → Java Application).

   ### Opción B – Desde la línea de comandos:
   1. Copiar el archivo `midespensa-0.0.1-SNAPSHOT.jar` y el fichero `.env` en un mismo directorio.
   2. Abrir una terminal en ese directorio.
   3. Ejecutar el siguiente comando:
      ```bash
      java -jar midespensa-0.0.1-SNAPSHOT.jar
      ```
  
3. La aplicación se ejecuta por defecto en el puerto **8081**. Para acceder desde el navegador, abre: 
   `https://localhost:8081/`  
   _(Importante: Para poder usar la camara fue necesario configurarla con HTTPS)._
4. Se mostrará la pantalla principal, desde donde podrás acceder al registro o iniciar sesión.
5. Puedes registrarte como usuario con rol **USER** o usar los usuarios declarados en el apartado `Credenciales de prueba` .
_(Nota: Las cuentas de prueba no permiten funcionalidades que dependan del correo electrónico, ya que usan correos ficticios)._