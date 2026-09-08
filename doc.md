# Mi Despensa (versión pública)

Este repositorio contiene una versión reducida del proyecto real acerca de la aplicación desarrollada, con fines demostrativos.

## Objetivos de la aplicacion

Esta aplicación web te permite **gestionar de forma sencilla los productos de tu despensa y tu lista de la compra** desde cualquier dispositivo con navegador, especialmente pensada para usarse en el móvil.  

Puedes **añadir productos** mediante la lectura de códigos de barras o manualmente, y mantener siempre un inventario actualizado: cuando consumes un producto, lo mueves a la **lista de la compra**, y al reponerlo lo reintroduces en la despensa.  

Además, puedes crear **productos personalizados** (como alimentos frescos o artículos sin código de barras) que solo tú podrás usar y editar.  

La **lista de la compra** se puede enviar directamente a tu correo electrónico para que la lleves contigo a cualquier lugar.  

La aplicación también incorpora una sección de **recetas colaborativas**, donde los usuarios pueden **crear, consultar y compartir recetas** con la comunidad, construyendo juntos un **recetario colectivo en constante crecimiento**.  

Existen dos tipos de usuarios:  
- **Usuario estándar** (`USER`), asignado automáticamente al registrarse.  
- **Administrador** (`ADMIN`), reservado al primer usuario creado en la base de datos.  

En resumen, se trata de una herramienta práctica y colaborativa, diseñada para ayudarte a **organizar tu hogar y descubrir nuevas recetas de forma sencilla**.  

### Funcionalidades principales de "Mi Despensa"

#### Gestión de productos
- Añadir productos a la despensa mediante escaneo de código de barras o de forma manual.
- Mover productos de la despensa a la lista de la compra y viceversa.
- Crear productos personalizados (sin código de barras).
- Visualización y edición de productos por parte del usuario que los creó.

#### Lista de la compra
- Añadir productos desde la despensa.
- Enviar la lista de la compra al correo electrónico del usuario.

#### Gestión de usuarios
- Registro con verificación de correo electrónico.
- Inicio de sesión seguro.
- Restablecimiento de contraseña mediante enlace enviado por email.
- Cambiar correo electrónico desde la sección de ajustes (requiere nueva verificación del correo).
- Cambiar contraseña desde la sección de ajustes.

#### Escaneo de productos (códigos de barras)
- Buscar el producto en la base de datos por usuario.
- Si no existe, buscar productos públicos (`idUser = 1`).
- Si no se encuentra, consultar la API de OpenFoodFacts.
- Si tampoco existe en la API, solicitar datos manualmente y guardar el producto como personalizado.

#### Recetas
- Consultar recetas públicas.
- Crear recetas propias y elegir si son públicas o privadas.
- Filtrar recetas por categoría o buscar por ingredientes.

#### Roles de usuario
- `USER`: 
  - Acceso a funciones básicas de gestión de productos, recetas y lista de la compra.
- `ADMIN`: 
  - Ver y editar todos los productos.
  - Sincronizar productos con la API.
  - Gestionar usuarios (incluida su eliminación).
  - Convertir productos personalizados en públicos.

#### Seguridad y autenticación
- Control de roles (`USER` y `ADMIN`) con Spring Security.
- Verificación por token para validar cuentas y cambios de correo o restablecimiento contraseña.

#### Contacto y privacidad
- Formulario de contacto para enviar mensajes al administrador.
- Página de política de privacidad.

## Tecnologías y herramientas utilizadas

- **Lenguaje de programación: Java**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Java.PNG?raw=true"
       alt="Java" 
       width="20%">
</p>

  Lenguaje de programación orientado a objetos, robusto, fácil de aprender, multiplataforma, con un gran ecosistema y comunidad que incluye bibliotecas y herramientas que facilitan el desarrollo de la aplicación. La estructura orientada a objetos facilita la gestión y actualización del código para evolucionar la aplicación de forma sencilla.

- **Framework: Spring Boot**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/SpringBoot.PNG?raw=true"
       alt="Spring Boot" 
       width="20%">
</p>

  Spring Boot es un **framework** de desarrollo que simplifica la creación de aplicaciones basadas en Spring.

- **Motor de plantillas: Thymeleaf**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Thymeleaf.PNG?raw=true"
       alt="Thymeleaf" 
       width="20%">
</p>

  Motor de plantillas que permite integrar código en las vistas generando HTML dinámico en aplicaciones web. Se ejecuta en el lado del servidor proporcionando el HTML correspondiente al cliente. En este proyecto, simplifica principalmente la creación de las listas de la despensa y la compra. También permite crear fragmentos reutilizables, reduciendo la cantidad de código y facilitando su mantenimiento o actualización.

- **Framework CSS: Bootstrap**

Se utilizó Bootstrap para facilitar el diseño responsivo de la aplicación y mejorar la experiencia de usuario en dispositivos móviles y de escritorio. Gracias a sus formularios y componentes reutilizables, se pudieron construir interfaces claras y adaptables sin tener que desarrollar todo el estilo desde cero.

- **Base de datos: MySQL + MySQL Workbench**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/MySQL.PNG?raw=true"
       alt="MySQL" 
       width="20%">
</p>

  Base de datos relacional.

- **ORM: Spring Data JPA**
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/SpringDataJPA.PNG?raw=true"
       alt="Spring Data JPA" 
       width="20%">
</p>

  ORM que proporciona métodos predefinidos optimizados, facilitando las interacciones con la base de datos.

- **Control de versiones: Git + GitHub + Sourcetree**

Se utilizó **Git** como sistema de control de versiones, permitiendo llevar un seguimiento detallado de los cambios en el código. La integración con **GitHub** facilitó el almacenamiento remoto. Además, se empleó **Sourcetree** como interfaz gráfica para simplificar la gestión de ramas, commits y fusiones. Esta combinación permitió organizar mejor el desarrollo, crear ramas para funcionalidades específicas y recuperar versiones anteriores fácilmente en caso de error o necesidad de revisión.

- **IDE: Eclipse**

Tras pruebas con IntelliJ IDEA y **Eclipse**, se optó por este último porque la herramienta Dev Tools (permite la modificación en tiempo de ejecución del código) no funcionaba correctamente en IntelliJ.

- **API externa: OpenFoodFacts**

**OpenFoodFacts** es una base de datos de productos alimenticios libre y abierta. Mediante la inyección de una librería de pl.coderion (desarrollador) se puede emplear un **wrapper** que facilita la obtención de datos desde la API externa a partir del código de barras leído.

- **Escáner de códigos de barras: html5-qrcode + JavaScript**

La funcionalidad de escaneo de códigos de barras se implementó utilizando la librería **html5-qrcode**, una solución basada en JavaScript que permite acceder a la cámara del dispositivo desde cualquier navegador moderno sin necesidad de plugins adicionales ni Flash.

- **Gestión de etiquetas: Tagify + JavaScript**

 Para facilitar la introducción y gestión de etiquetas en los formularios de la aplicación (tags e ingredientes), se utilizó la librería **Tagify**, que permite introducir múltiples valores en un solo campo de texto de forma visual e intuitiva.

El sistema permite tanto seleccionar etiquetas existentes (cargadas desde el backend mediante peticiones fetch) como crear nuevas. Mientras el usuario escribe, ofrece sugerencias automáticas, mejorando así la experiencia de uso y la calidad de los datos guardados.

<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Tagify.PNG?raw=true"
       alt="Tagify" 
       width="75%">
</p>

- **Thumbnailator**

  Thumbnailator es una librería Java de código abierto que permite redimensionar y optimizar imágenes de forma sencilla y eficiente. En el proyecto, se utilizó para procesar las imágenes subidas por los usuarios antes de almacenarlas en Cloudinary.

- **Cloudinary**

  Cloudinary es un servicio de almacenamiento en la nube especializado en la gestión y optimización de archivos multimedia, especialmente imágenes y vídeos. En el proyecto, se utiliza para almacenar las imágenes asociadas a las recetas subidas por los usuarios, permitiendo su acceso desde cualquier dispositivo y garantizando escalabilidad y rendimiento sin necesidad de almacenar los archivos localmente.

- **Gestor de dependencias: Maven**

Se utilizó **Maven** como herramienta de gestión de proyectos y dependencias. A través del archivo `pom.xml` se definieron las librerías necesarias, y Maven se encargó de descargarlas y gestionarlas.

Entre las dependencias destacadas en el desarrollo del proyecto están:

- `spring-boot-starter`: configuración básica de Spring Boot.
- `spring-boot-starter-web`: para desarrollar aplicaciones web.
- `spring-boot-starter-thymeleaf`: para el uso de plantillas con Thymeleaf.
- `spring-boot-starter-data-jpa`: acceso a la base de datos usando JPA y Hibernate.
- `spring-boot-starter-security`: gestión de usuarios, contraseñas y roles.
- `spring-boot-starter-mail`: envío de correos electrónicos (validación de cuenta, lista de la compra, etc).
- `spring-dev-tools`: permite recarga automática de cambios durante el desarrollo, reduciendo el tiempo entre modificaciones y comprobaciones.
- `mysql-connector-j`: driver JDBC para conectar con la base de datos MySQL.
- `thymeleaf-extras-springsecurity6`: integración de Spring Security con las vistas Thymeleaf para mostrar/ocultar elementos según el rol del usuario.
- `thymeleaf-layout-dialect`: permite el uso de layouts y fragmentos, facilitando la creación de una interfaz coherente y reutilizable.
- `mapstruct + mapstruct-processor`: generación automática de mapeos entre entidades y DTOs. Facilita la conversión entre entidades y DTOs, evitando que los cambios en las entidades afecten a los DTOs usados en las vistas, y evitando exponer datos innecesarios.
- `spring-boot-devtools`: recarga automática de cambios durante el desarrollo.
- `spring-boot-starter-test`: para realizar pruebas unitarias y de integración.
- `spring-security-test`: para probar funcionalidades relacionadas con la seguridad.
- `lombok`: reduce la escritura de código (getters/setters, constructores, etc.).
- `openfoodfacts-java-wrapper`: wrapper para interactuar con la API de OpenFoodFacts.
- `cloudinary-http5 + cloudinary-taglib`: para el almacenamiento y gestión de imágenes en Cloudinary.
- `thumbnailator`: librería usada para redimensionar imágenes en el backend antes de enviarlas a Cloudinary.
- `dotenv-java`: permite cargar variables de entorno desde un archivo `.env`, útil para mantener datos sensibles fuera del código fuente.

## Estructura de la aplicación

La aplicación está organizada siguiendo una arquitectura por capas, con el objetivo de separar las responsabilidades de cada parte del sistema y facilitar el mantenimiento del código.

La estructura general de la aplicación es la siguiente:
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Programa.PNG?raw=true"
       alt="Estructura" 
       width="30%">
</p>

### Backend (Spring Boot)

La parte backend está estructurada en los siguientes paquetes:

- **Configs (configuraciones)**: Se encarga de realizar configuraciones como la del correo electrónico, la configuración de Spring Security o la configuración para indicar una carpeta del proyecto con imágenes accesibles.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaConfigs.PNG?raw=true"
       alt="Estructura configuraciones" 
       width="30%">
</p>

- **Controllers (controladores)**: Se encargan de recibir las peticiones HTTP, procesarlas y devolver las respuestas correspondientes. Usan anotaciones como `@Controller` y `@RestController`.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaControllers.PNG?raw=true"
       alt="Estructura controladores" 
       width="30%">
</p>

- **DTOs (Data Transfer Objects)**: Se utilizan para transferir datos entre el backend y el frontend, evitando exponer directamente las entidades y que cambios en estas últimas afecten al frontend.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaDTOs.PNG?raw=true"
       alt="Estructura DTOs" 
       width="30%">
</p>

- **Entidades (entities)**: Representan las tablas de la base de datos y están anotadas con `@Entity`, o son objetos utilizados como `Barcode` (para mapear datos de la API externa) o claves compuestas como `IDProductIdUser`.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaEntities.PNG?raw=true"
       alt="Estructura entidades" 
       width="30%">
</p>

- **Exceptions (excepciones)**: Controlador que agrupa todas las excepciones. Utiliza la anotación `@RestControllerAdvice`.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaExceptions.PNG?raw=true"
       alt="Estructura controlador excepciones" 
       width="30%">
</p>

- **Mappers**: Clases que mapean los DTOs a entidades de la aplicación o viceversa.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaMappers.PNG?raw=true"
       alt="Estructura mappers" 
       width="30%">
</p>

- **Repositories (repositorios)**: Interfaces que extienden de `JpaRepository` para realizar operaciones sobre la base de datos mediante Spring Data JPA.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaRepositories.PNG?raw=true"
       alt="Estructura repositorios" 
       width="30%">
</p>

- **Services (servicios)**: Contienen la lógica de negocio de la aplicación. Aquí se realizan operaciones como el procesamiento de productos, gestión de usuarios o envío de correos electrónicos.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProgramaServices.PNG?raw=true"
       alt="Estructura servicios" 
       width="30%">
</p>

### Frontend (Thymeleaf + JavaScript)

La interfaz de usuario está desarrollada usando **Thymeleaf** como motor de plantillas. Se distinguen dos grupos principales: **static** (recursos estáticos) y **templates** (plantillas dinámicas).
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Resources.PNG?raw=true"
       alt="Estructura resources" 
       width="30%">
</p>

En el grupo **static** se encuentran los archivos CSS, iconos, imágenes y JavaScript.  
En los archivos JavaScript se incluyen librerías utilizadas o pequeños scripts para lectura de códigos de barras, gestión de etiquetas o ingredientes que mejoran la experiencia de usuario.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesStatic.PNG?raw=true"
       alt="Estructura static" 
       width="30%">
</p>

En el grupo **templates** se encuentran las vistas organizadas en archivos `.html`, agrupadas según su funcionalidad. Para evitar duplicidad de código, se reutilizan alertas, barra de navegación, pie de página, etc., mediante **Thymeleaf Layout Dialect**.

Organización de los templates:
- **Fragmentos**: Componentes reutilizables en las distintas páginas HTML. Por ejemplo, la tabla de productos se emplea tanto en la vista de productos del administrador, como en la vista de productos de la despensa del usuario o de la lista de la compra.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesFragments.PNG?raw=true"
       alt="Estructura fragmentos" 
       width="30%">
</p>

- **Layouts**: Plantillas base que comparten estructura común. Todas las vistas utilizan estos modelos, excepto las mencionadas al final de esta lista:
  - `layout.html`: plantilla con barra de navegación y script de **Bootstrap**.
  - `layout-recipes.html`: similar a la anterior, pero incluye scripts para la gestión de etiquetas (tags o ingredientes).
  - `layout-scanner.html`: similar a `layout.html`, pero incluye el script para el escáner de códigos de barras.
  - `layout-without-nav.html`: plantilla que incluye solo la barra de navegación superior sin funcionalidades de navegación. Se emplea en vistas como registro, inicio de sesión, restablecimiento de contraseña...
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesLayouts.PNG?raw=true"
       alt="Estructura layouts" 
       width="30%">
</p>

- **Mails**: Fragmentos de código incluidos en el cuerpo de los correos enviados al usuario para validación de cuenta, restablecimiento de contraseña o envío de la lista de la compra.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesMails.PNG?raw=true"
       alt="Estructura correos electrónicos" 
       width="30%">
</p>

- **Pantry***: Vistas para la gestión de la despensa o formulario de creación de productos personalizados.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesPantry.PNG?raw=true"
       alt="Estructura despensa" 
       width="30%">
</p>

- **Product***: Vistas para la gestión de productos por parte del administrador o vista de características del producto.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesProduct.PNG?raw=true"
       alt="Estructura producto" 
       width="30%">
</p>

- **Recipe***: Vistas para la gestión de recetas.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesRecipe.PNG?raw=true"
       alt="Estructura recetas" 
       width="30%">
</p>

- **ShoppingList***: Vistas para la gestión de la lista de la compra.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesShoppingList.PNG?raw=true"
       alt="Estructura lista de la compra" 
       width="30%">
</p>

- **User***: Vistas para la gestión/registro de usuario.
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ResourcesTemplatesUser.PNG?raw=true"
       alt="Estructura usuario" 
       width="30%">
</p>

- Y finalmente, las páginas principales:
  - `index.html`: página principal
  - `error.html`: página de visualización de excepciones
  - `contact.html`: página de contacto
  - `privacy.html`: página de política de privacidad

<em>*Hacen uso de los layouts para mantener un estándar visual general.</em>

### Base de datos

La base de datos fue diseñada utilizando **MySQL**, debido a la experiencia previa con este sistema gestor y su alta compatibilidad con el resto de tecnologías empleadas en el proyecto.

Se definió la estructura de la base de datos y se generó el script correspondiente utilizando **MySQL Workbench 8.0**, lo que facilitó tanto la creación como la visualización del modelo relacional de manera gráfica y estructurada.

<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/EstructuraBD.PNG?raw=true"
       alt="Estructura de la base de datos" 
       width="80%">
</p>

## Pruebas unitarias con JUnit

Para garantizar que la lógica de la aplicación funciona correctamente de forma aislada, se realizaron **pruebas unitarias** utilizando el framework **JUnit** y la librería **Mockito**. Estas pruebas se centraron en servicios y métodos con lógica crítica que no dependen directamente de la interfaz gráfica.

A continuación, se muestran las pruebas unitarias realizadas para el servicio *ProductService*:
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/TestUnitariosProductService.PNG?raw=true"
       alt="Tests unitarios de ProductService" 
       width="50%">
</p>

Cobertura de los tests unitarios de *ProductService*:
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/TestUnitariosProductServiceCobertura.PNG?raw=true"
       alt="Cobertura de los tests unitarios de ProductService" 
       width="50%">
</p>


## Vistas de la aplicación

### Pantallas iniciales

A las siguientes pantallas se permite el acceso **sin necesidad de autenticación**. Sirven para informar a los usuarios sobre el funcionamiento y las funcionalidades que ofrece la aplicación.

**Pantalla principal**

Esta pantalla ofrece acceso a **inicio de sesión** o **registro**, además de mostrar una descripción de las funcionalidades principales de la aplicación (aunque no se ven todas en la imagen siguiente).

Vista en pantallas grandes:  
<p align="center">
<img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/PantallaPrincipal.PNG?raw=true" alt="Pantalla principal" width="75%">
</p>

Vista en dispositivos móviles:  
<p align="center">
<img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/PantallaPrincipalMovil.PNG?raw=true" alt="Pantalla principal móvil" width="30%">
</p>

**Pantalla política de privacidad**  
Muestra la política de privacidad de la aplicación. La lectura y aceptación de esta política será requerida durante el proceso de registro.  

**Pantalla de formulario de contacto**  
Esta pantalla permite al usuario contactar con el administrador. El administrador recibirá un correo desde la cuenta de la aplicación: **midespensa.contact@gmail.com**.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/FormularioDeContacto.PNG?raw=true"
       alt="Pantalla formulario de contacto" 
       width="75%">
</p>

**Pantalla de inicio de sesión**  
Permite a los usuarios acceder introduciendo su correo electrónico y contraseña. También incluye un botón para registrarse si aún no tienen cuenta y un enlace para recuperar la contraseña en caso de olvidarla.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/InicioSesion.PNG?raw=true"
       alt="Pantalla de inicio de sesión" 
       width="75%">
</p>

**Pantalla de registro**  
Permite a nuevos usuarios crear una cuenta proporcionando correo, contraseña y su confirmación. Es obligatorio aceptar la política de privacidad marcando el checkbox correspondiente. Si no se completan todos los campos, el formulario no permite continuar, asegurando que se aceptan los términos antes de registrarse. También incluye un botón para volver a la pantalla de inicio de sesión.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Registro.PNG?raw=true"
       alt="Pantalla de registro" 
       width="75%">
</p>

Una vez completado el registro, se notifica al usuario que recibirá un correo para validar la cuenta:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/NotificaficaciónCorreoValidacionConta.PNG?raw=true"
       alt="Notificación envío correo validación cuenta" 
       width="25%">
</p>

+ **Correo recibido para validación de cuenta**  
  El usuario recibe un correo con un botón que lleva a la pantalla de activación de la cuenta. El enlace incluye un **token único** en la URL, que se comprueba en el backend contra la base de datos para garantizar que es válido y no ha expirado.  
  El tiempo de validez es de **24 horas**; pasado ese periodo, si no se valida, será necesario registrarse de nuevo.  
  Este mecanismo mejora la seguridad y garantiza que los correos registrados sean válidos.  
  <p align="center">
    <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/CorreoValidacionCuenta.PNG?raw=true"
        alt="Correo validación cuenta" 
        width="50%">
  </p>

**Pantalla de validación del correo**  
Cuando el usuario pulsa el botón de validación del correo recibido, accede a una URL con un token único. La aplicación comprueba que el token es válido y no ha expirado. Si es correcto, muestra una pantalla en la que solo debe pulsar el botón `Validar`.  
Una vez hecho esto, la cuenta queda activada y el usuario puede iniciar sesión con normalidad.  
Este proceso garantiza que solo usuarios con acceso al correo registrado puedan activar la cuenta.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RegistroValidacionCorreo.PNG?raw=true"
       alt="Pantalla validación correo" 
       width="75%">
</p>

+ **Mensaje de validación exitosa**  
  Si la validación es exitosa, se muestra un mensaje de éxito y se redirige al usuario a la pantalla de inicio de sesión. En caso contrario, se muestra un mensaje indicando la causa del fallo y también se redirige.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RegistroValidacionCorreoExitosa.PNG?raw=true"
      alt="Mensaje validación correo exitosa en inicio de sesión" 
      width="75%">
</p>

**Pantalla de solicitud de restablecimiento de contraseña**  
El usuario introduce su correo y se le envía un enlace para restablecer la contraseña.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RestablecerContraseña.PNG?raw=true"
       alt="Pantalla solicitud restablecimiento contraseña" 
       width="75%">
</p>

+ **Correo recibido para restablecer la contraseña**  
Al solicitar restablecer, el usuario recibe un correo con un enlace que incluye un token único. Al acceder, la aplicación comprueba que el token es válido y no ha expirado. Si todo es correcto, el usuario puede introducir una nueva contraseña; en caso contrario, será redirigido a inicio de sesión.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/CorreoRestablecimientoContraseña.PNG?raw=true"
       alt="Correo restablecimiento contraseña" 
       width="50%">
</p>

**Pantalla de restablecimiento de contraseña**  
Si el token es válido, se muestra esta pantalla donde el usuario puede introducir y confirmar una nueva contraseña. Si ambas coinciden, la contraseña se actualiza y ya puede acceder con ella.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RestablecimientoContraseña.PNG?raw=true"
       alt="Pantalla restablecimiento contraseña" 
       width="75%">
</p>

**Pantalla de error**  
Cuando ocurre una **excepción** se muestra esta vista de error personalizada. Indica de forma resumida el tipo de error y ofrece un botón para volver a la pantalla inicial, facilitando la recuperación de navegación.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Error.PNG?raw=true"
       alt="Vista pantalla de error" 
       width="75%">
</p>

**Barra de navegación**  
Una vez iniciada la sesión, aparece una **barra de navegación superior** para navegar entre despensa, lista de la compra, recetas y ajustes de usuario. En el caso del administrador, también se muestra un menú desplegable con opciones de administrador, además de las mismas opciones que para el resto de usuarios.

Vista en pantallas grandes:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/BarraNavegacion.PNG?raw=true"
       alt="Vista barra de navegación" 
       width="75%">
</p>

Vista en dispositivos móviles (menú desplegable):  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/BarraNavegacionMovil.PNG?raw=true"
       alt="Barra de navegación móvil" 
       width="30%">
</p>

### Pantallas del rol de usuario

**Pantalla vista de la despensa**  
Muestra todos los productos almacenados en la despensa del usuario. Desde aquí se puede:
- Añadir nuevos productos manualmente o mediante escáner.
- Modificar la cantidad de un producto existente.
- Eliminar productos de la despensa.
- Añadir productos a la lista de la compra.
- Al pulsar sobre el nombre de un producto, se accede a su vista de detalles, donde se pueden consultar o editar (si es personalizado).
- Hay un botón flotante en la parte inferior derecha para acceder a la lista, que muestra también la cantidad total de productos diferentes añadidos. Facilita la navegación, sobre todo en móviles.

Vista en pantallas grandes:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Despensa.PNG?raw=true"
       alt="Vista de la despensa" 
       width="75%">
</p>

Vista en dispositivos móviles:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/DespensaMovil.PNG?raw=true"
       alt="Despensa móvil" 
       width="30%">
</p>

**Detalles del producto**  

+ **Producto obtenido de OpenFoodFacts o generado por el administrador**  
  Se muestran el nombre, marca, cantidad e imagen.  
  Estos productos no son editables por el usuario, garantizando la coherencia de los datos.  
  Los productos generados por el administrador no permiten añadir imagen propia.  
  En los productos de la API, se almacena la URL de la imagen en la base de datos y se renderiza en la vista.

  Vista en pantallas grandes:  
  <p align="center">
    <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProductoDetalle.PNG?raw=true"
        alt="Detalles del producto no editable" 
        width="75%">
  </p>

  Vista en móvil:  
  <p align="center">
    <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProductoDetalleMovil.PNG?raw=true"
        alt="Detalles del producto móvil" 
        width="30%">
  </p>

+ **Producto personalizado**  
  Permite `editar` o `eliminar`. Este producto fue añadido manualmente o tras escanear un código, y se considera personalizado.  
  Permite adaptarse a productos no disponibles en la base de datos o API.  
  <p align="center">
    <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProductoDetalleEditable.PNG?raw=true"
         alt="Detalle producto personalizado" 
         width="75%">
  </p>

+ **Edición de producto personalizado**  
  Aquí el usuario puede modificar características como nombre, categoría, marca o cantidad.  
  <p align="center">
    <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ProductoEdicion.PNG?raw=true"
         alt="Edición producto personalizado" 
         width="75%">
  </p>

**Escaneo de productos**  
Pantalla para escanear el código de barras o introducirlo manualmente.  
- Si se escanea: comprueba si existe en la base de datos; si no, consulta OpenFoodFacts; si no está allí, muestra el formulario de edición para que el usuario complete datos mínimos (nombre).  
- Si se introduce manualmente: es obligatorio código y nombre; se trata como producto personalizado y es privado al usuario.  
- Si otro usuario escanea el mismo código sin estar en la API, deberá introducir los datos de nuevo, creando un producto distinto asociado a su id.

Vista en pantallas grandes:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Escaner.PNG?raw=true"
       alt="Escaneo de productos" 
       width="75%">
</p>

Vista en móvil:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/EscanerMovil.PNG?raw=true"
       alt="Escaneo de productos móvil" 
       width="30%">
</p>

**Lista de la compra**  
Pantalla donde se pueden añadir productos a la despensa, eliminar de la lista o modificar cantidades:  
- Icono con símbolo de correo: envía la lista por email al usuario.  
- Al pulsar en el nombre del producto se accede a sus detalles.  
- Al añadir a la despensa, desaparece de la lista y se añade o incrementa en despensa.  
- Botón flotante inferior derecho para acceder a la despensa, pensado para móviles.

Vista en pantallas grandes:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ListaDeLaCompra.PNG?raw=true"
       alt="Lista de la compra" 
       width="75%">
</p>

Vista en móvil:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/ListaDeLaCompraMovil.PNG?raw=true"
       alt="Lista de la compra móvil" 
       width="30%">
</p>

**Correo recibido con la lista de la compra**  
Incluye casillas al lado de cada producto, útiles para marcar durante la compra desde el móvil.  
Nota: estas casillas no guardan su estado si se cierra o recarga el correo.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/CorreoListaDeLaCompra.PNG?raw=true"
       alt="Correo recibido con la lista de la compra" 
       width="50%">
</p>

**Vista de recetas**  
Muestra las diferentes recetas disponibles. Se pueden filtrar por categoría o nivel de dificultad, así como buscar por ingredientes con opción "contenga todos" o "alguno".  
El administrador tiene todas las funcionalidades en todas las recetas.  
Los usuarios con rol `user` ven sus recetas y las públicas de otros.  
*Ejemplo*: en la receta de brownie, aparece un botón de editar y eliminar porque fue creada por el usuario actual.

Vista en pantallas grandes:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Recetas.PNG?raw=true"
       alt="Vista de recetas" 
       width="75%">
</p>

Vista en móvil:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RecetasMovil.PNG?raw=true"
       alt="Recetas móvil" 
       width="30%">
</p>

**Detalle de la receta**  
Muestra todos los detalles de una receta seleccionada. Si fue creada por el usuario, muestra botones de editar y eliminar; si no, solo el botón para volver atrás.

Vista en pantallas grandes:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Receta.PNG?raw=true"
       alt="Detalle de la receta" 
       width="75%">
</p>

Vista móvil:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RecetaMovil.PNG?raw=true"
       alt="Detalle de la receta móvil" 
       width="30%">
</p>

**Editar receta**  
Permite modificar los datos de una receta creada por el usuario actual.

**Agregar nueva receta**  
Permite añadir una nueva receta. Los campos obligatorios están resaltados con un borde azul.

Vista en pantallas grandes:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RecetaAgregarNueva.PNG?raw=true"
       alt="Agregar nueva receta" 
       width="75%">
</p>

Vista móvil:  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/RecetaAgregarNuevaMovil.PNG?raw=true"
       alt="Agregar nueva receta móvil" 
       width="30%">
</p>

**Ajustes de usuario**  
Desde el nombre de usuario se accede a un menú desplegable con opciones de configuración y cierre de sesión. Las opciones disponibles son:

- **Cambio de correo electrónico**: permite modificar el correo asociado. Requiere nueva verificación, por lo que se envía un correo a la nueva dirección.
- **Cambio de contraseña**: permite actualizar la contraseña actual.
- **Vaciar despensa o lista de la compra**: permite eliminar todos los productos de esas secciones con un solo clic.
- **Dar de baja la cuenta**: elimina por completo los datos del usuario, incluyendo cuenta, despensa, lista de la compra y recetas personalizadas.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/Ajustes.PNG?raw=true"
       alt="Ajustes de usuario" 
       width="75%">
</p>

### Pantallas del rol administrador

A continuación se muestran las pantallas disponibles solo para usuarios con rol de administrador. Estas funcionalidades permiten una gestión avanzada de la aplicación, garantizando un control adecuado de los datos.

**Gestión de productos**  
El administrador puede visualizar todos los detalles de los productos en la base de datos. Hay un botón para actualizar los productos a partir de OpenFoodFacts: verifica cada código de barras y, si está disponible, actualiza los datos (excepto el nombre para no sobrescribir nombres personalizados).  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/AdminProductos.PNG?raw=true"
       alt="Gestión de productos" 
       width="75%">
</p>

**Productos personalizados**  
Muestra los productos personalizados añadidos por usuarios. El administrador puede convertirlos en públicos cambiando su `idUser` a 1. El producto personalizado se mantiene y se crea uno público.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/AdminProductosPersonalizados.PNG?raw=true"
       alt="Productos personalizados" 
       width="75%">
</p>

**Usuarios registrados**  
El administrador puede visualizar los usuarios registrados, incluyendo su última conexión. También puede eliminar usuarios, lo que implicará la eliminación de sus productos personalizados, despensa, lista de la compra y recetas asociadas.  
<p align="center">
  <img src="https://github.com/RubenToucedaPRO/springboot-mi-despensa-webapp-public/blob/main/images/AdminUsuarios.PNG?raw=true"
       alt="Usuarios registrados" 
       width="75%">
</p>