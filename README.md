# Cuestionario Pruebas

Proyecto web estático compuesto por `index.html`, `app.js` y `styles.css`, con automatización Selenium en Java.

## Requisitos

- Node.js / npm
- Java JDK
- Maven
- Google Chrome

## Ejecutar la aplicación web

En una terminal:

```bash
npm install
npm start
```

La aplicación queda disponible en:

```text
http://localhost:3000/index.html
```

## Ejecutar los scripts de Selenium

Deja la aplicación corriendo con `npm start` y abre otra terminal en la raíz del proyecto.

Puedes ejecutar Selenium con npm:

```bash
npm run selenium
```

O directamente con Maven:

```bash
mvn compile exec:java
```

La clase principal es:

```text
com.mycompany.pruebaselenium.EjercicioSeleniumJava
```

## Estructura relevante

```text
index.html
app.js
styles.css
pom.xml
src/main/java/com/mycompany/pruebaselenium/EjercicioSeleniumJava.java
```

## Nota

Si ejecutas solo `mvn exec:java` y ves `ClassNotFoundException`, usa `mvn compile exec:java`, porque `exec:java` no compila la clase automáticamente.

Selenium 4 usa Selenium Manager para gestionar el driver del navegador automáticamente en la mayoría de casos, por lo que normalmente no necesitas descargar `chromedriver` manualmente.
