# Operación fuego de Quásar

Operación Quasar es una aplicación diseñada para calcular la posición de un emisor y reconstruir un mensaje secreto a partir de datos recibidos en 3 satélites. Este proyecto forma parte de un desafío técnico y utiliza tecnologías como Java, Spring Boot y Swagger para documentar la API.

## Descripción

La aplicación recibe datos de tres satélites (por ejemplo, Kenobi, Skywalker y Sato) a través de endpoints REST.  
Utiliza algoritmos de trilateración para calcular la posición y luego reconstruye el mensaje secreto a partir de fragmentos enviados por cada satélite.

## Tecnologias usadas en el proyecto.

- **Java 21:**
  Lenguaje base en el el que construyo el proyecto. 

- **Spring Boot:**
  Framework para adaptar el proyecto a la tecnologia de microserivicios.

- **Swagger:**
  Herramienta para documentar la API.

## Librerias usadas en el proyecto.

- **Lombok:**
  Reduce código repetitivo mediante anotaciones para generación automática de métodos.

- **com.lemmingapex.trilateration:trilateration:1.0.2:**
  Implementación de algoritmos de trilateración.

- **org.apache.commons:commons-math3:3.6.1:**
  Implementación de algoritmos de calculos avanzados.

## Características

- **Calcular posición:**  
  Utiliza la distancia a cada satélite para calcular la ubicación exacta del emisor.

- **Reconstrucción del mensaje:**  
  Combina fragmentos de mensaje para generar el mensaje completo.

- **Documentación Swagger:**  
  La API está documentada con Swagger para facilitar su uso y pruebas.

- **Documentación JavaDoc:**  
  La aplicación está documentada mediante JavaDoc, lo que permite generar documentación HTML que explica el propósito y el uso de clases, métodos y parámetros, facilitando el mantenimiento y la comprensión del código.

## Estructura del proyecto

- **config:**  
  Contiene la clase de la configuración necesaria para Swagger, el cual se está utilizando para documentar el consumo de la API.

- **controller:**  
  Agrupa las clases que exponen los endpoints REST. Estas clases se encargan de recibir y procesar las peticiones HTTP, delegando en la capa de servicio para ejecutar la lógica de negocio y retornar las respuestas correspondientes.

- **exception:**  
  Contiene las clases que definen y manejan excepciones personalizadas para controlar errores y devolver respuestas consistentes en caso de fallos.

- **model:**  
  Aquí se definen los modelos de datos, DTOs y entidades que representan la información con la que trabaja la aplicación, como `Position`, `Satellite`, `RebelRequest`, `RebelResponse`, etc.

- **service:**  
  Contiene la lógica de negocio de la aplicación, abstraída mediante interfaces para facilitar la prueba y el mantenimiento.

  - **impl:**  
    Este paquete agrupa las implementaciones concretas de las interfaces de servicio. Aquí se encuentran las clases que realizan el cálculo de la ubicación a partir de las distancias (por ejemplo, mediante trilateración usando Levenberg-Marquardt) y el ensamblaje del mensaje a partir de fragmentos, permitiendo que el resto de la aplicación dependa únicamente de los contratos definidos en los servicios.

- **validation:**  
  Contiene utilidades para validar y extraer la información de las solicitudes. Por ejemplo, se verifica que la petición incluya los tres satélites requeridos ("kenobi", "skywalker" y "sato") y se extraen sus datos de forma consistente. Esto centraliza la lógica de validación y extracción, facilitando el mantenimiento y respetando el principio de responsabilidad única.

- **main:**  
  Es el punto de entrada de la aplicación, generalmente una clase anotada con `@SpringBootApplication`. Este archivo se encarga de arrancar el servidor, cargar las configuraciones y poner en marcha todos los componentes definidos en el resto de la aplicación.

## Uso de la API

- **Mediante PostMan / Thunder Client:**
  Ingresar la siguiente URL http://QuasarMeLi-env.eba-hw4cs9mu.us-east-2.elasticbeanstalk.com en el aplicativo de preferencia y dependiendo del servicio que se quiera consumir se ajusta el endPoint, por ejemplo:

- **/topsecret/**
  ```json
  {
    "satellites": [
      {
        "name": "kenobi",
        "distance": 100.0,
        "message": ["este", "", "", "mensaje", ""]
      },
      {
        "name": "skywalker",
        "distance": 115.5,
        "message": ["", "es", "", "", "secreto"]
      },
      {
        "name": "sato",
        "distance": 142.7,
        "message": ["", "", "un", "", ""]
      }
    ]
  }
---
  ![Diagrama del sistema](multimedia/topsecretPostman.png)
  - **/topsecret_split/kenobi**
    ```json
    {
    "distance": 100.0,
    "message": ["hay", "", "", "el", ""]
    }
---
  ![Diagrama del sistema](multimedia/topsecret_splitKenobi.png)

- **Mediante Swagger:**
  Ingresar la siguiente URL en el navegador (Importante: NO estar en modo incognito) http://quasarmeli-env.eba-hw4cs9mu.us-east-2.elasticbeanstalk.com/swagger-ui/index.html, se encontrará con una interfaz grafica que proporciona Swagger la cual permite realizar los consumos directamente desde esa interfaz, al seguir estos pasos se puede consumir el servicio que se desee de la API REST:  

  **1.** Dar click en el boton "Try it out" del servicio que se quiera consumir:
  ![Diagrama del sistema](multimedia/exampleTry.png)
  **2.** Rellenar el request de manera adecuada y luego oprimir el boton de "Execute", por ejemplo:

  - **/topsecret/**  
    ![Diagrama del sistema](multimedia/topsecretSwagger.png)
  - **/topsecret_split/{satellite_name}**  
  En este servicio aparte de enviar el JSON request, hay que enviar el parametro del nombre del satelite (Señalado en el recuadro verde) al cual se le quiere actualizar el array de strings que contiene el mensaje.
    ![Diagrama del sistema](multimedia/topsecret_splitSwaggerKenobi.png)

  **3.** Verificar la respuesta que nos da el servicio:
  - **/topsecret/**  
  ![Diagrama del sistema](multimedia/topsecretSwaggerResponse.png)
  - **/topsecret_split/{satellite_name}**  
  ![Diagrama del sistema](multimedia/topsecret_splitSwaggerResponse.png)