# Spring AI Adventure

**Spring AI Adventure** es una aplicación web que permite generar y jugar una aventura interactiva "elige tu propia aventura" utilizando modelos de lenguaje (LLM) y generación de imágenes. Está construida con **Spring Boot**, integrando **Spring AI** y **Ollama** para la generación de texto, y opcionalmente **Stable Diffusion WebUI** para imágenes.

![spring ai adventure](img/Screenshot_1.jpg)

---

## Features

* Generación de historias de aventura personalizadas según:
  * Género (acción, comedia, fantasía, etc.)
  * Protagonista
  * Duración (corta, media, larga)
  * Complejidad (baja, media, alta)
* Sistema de **turnos**: la historia avanza según las decisiones del usuario.
* **Rastreo del estado del personaje**: físico, emocional, decisiones tomadas y resumen de la historia.
* **RAG (Retrieval-Augmented Generation) simple**: cada turno incorpora un feature del Megalodón para enriquecer la historia.
* **Mensaje final** personalizado según el desarrollo de la aventura:
  > "Gracias por abrirnos los caminos".
* **Generación de imágenes** asociadas a la historia usando Stable Diffusion WebUI (opcional).

---

## Requisitos

* Java 17+
* Maven 3+
* Ollama instalado y corriendo (localhost:11434)
* Modelo Mistral descargado: `ollama pull mistral`
* Navegador web moderno

---

## Instalación y ejecución rápida (speedrun)

1. Clonar o crear el proyecto con los archivos proporcionados.

2. Ejecutar:
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```

3. Instalar Ollama y descargar el modelo:
   ```bash
   ollama pull mistral
   ```

4. Asegurarse de que Ollama esté corriendo en `localhost:11434`.

5. Reemplazar la función `callLLM` por una llamada HTTP a Ollama si se desea personalizar la integración.

6. Abrir en el navegador:
   ```
   http://localhost:8080/
   ```
   Probar con inputs mínimos:
   * Género: Acción
   * Protagonista: "Sergio el valiente"
   * Duración: Corta
   * Complejidad: Baja

7. Para iniciar una aventura completa:
   ```
   http://localhost:8080/aventura/form
   ```
   * Completar los campos.
   * Al enviar, el backend llama a Mistral vía Spring AI.
   * La historia se renderiza en `scene.html`.

---

## Flujo de la Aventura

1. El usuario completa los datos iniciales.
2. Se genera el **inicio de la historia**.
3. Cada turno:
   * El usuario toma decisiones.
   * Se actualiza el estado físico/emocional del protagonista.
   * Se incorpora un feature del Megalodón (RAG simple).
4. Al final:
   * Se cuentan las menciones de elementos clave.
   * Se muestra el mensaje final:
     > "Gracias por abrirnos los caminos".

---

## Generación de imágenes (opcional)

Si deseas enriquecer la historia con imágenes:

* Instalar y correr **Stable Diffusion WebUI**.
* Configurar la integración en el backend.
* Cada turno puede generar imágenes del escenario o personaje.

---

MIT License © 2025