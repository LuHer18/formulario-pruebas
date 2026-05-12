package com.mycompany.pruebaselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class EjercicioSeleniumJavaTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String URL = "http://localhost:3000/index.html";

    private static final By FULL_NAME = By.id("fullName");
    private static final By EMAIL = By.id("email");
    private static final By PASSWORD = By.id("password");
    private static final By CONFIRM_PASSWORD = By.id("confirmPassword");
    private static final By REGISTER_BUTTON = By.cssSelector("#registerForm button[type='submit']");

    private static final By ALERT_BOX = By.id("alertBox");
    private static final By FULL_NAME_ERROR = By.id("fullNameError");
    private static final By EMAIL_ERROR = By.id("emailError");
    private static final By PASSWORD_ERROR = By.id("passwordError");
    private static final By CONFIRM_PASSWORD_ERROR = By.id("confirmPasswordError");

    @BeforeAll
    static void iniciarDriver() {
        ChromeOptions options = new ChromeOptions();

        // Configuración necesaria para GitHub Actions o entornos sin interfaz gráfica.
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME));
    }

    @AfterAll
    static void cerrarDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static void limpiarDatos() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME));
    }

    private static void llenarFormulario(String nombre, String correo, String password, String confirmacion) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME)).clear();
        driver.findElement(FULL_NAME).sendKeys(nombre);

        driver.findElement(EMAIL).clear();
        driver.findElement(EMAIL).sendKeys(correo);

        driver.findElement(PASSWORD).clear();
        driver.findElement(PASSWORD).sendKeys(password);

        driver.findElement(CONFIRM_PASSWORD).clear();
        driver.findElement(CONFIRM_PASSWORD).sendKeys(confirmacion);
    }

    private static void registrar() {
        wait.until(ExpectedConditions.elementToBeClickable(REGISTER_BUTTON)).click();
    }

    private static String obtenerTexto(By elemento) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(elemento)).getText();
    }

    @Test
    @DisplayName("CP01 - Registro válido")
    void cp01_registroValido() {
        limpiarDatos();

        llenarFormulario("Juan Perez", "juan.cp01@gmail.com", "Abc12345", "Abc12345");
        registrar();

        String mensaje = obtenerTexto(ALERT_BOX);
        assertEquals("Usuario registrado correctamente.", mensaje);
    }

    @Test
    @DisplayName("CP02 - Correo inválido")
    void cp02_correoInvalido() {
        limpiarDatos();

        llenarFormulario("Juan", "juanemail.com", "Abc12345", "Abc12345");
        registrar();

        String error = obtenerTexto(EMAIL_ERROR);
        assertEquals("Ingresa un correo electrónico válido.", error);
    }

    @Test
    @DisplayName("CP03 - Contraseña débil o corta")
    void cp03_passwordCorta() {
        limpiarDatos();

        llenarFormulario("Juan", "juan.cp03@gmail.com", "abc", "abc");
        registrar();

        String error = obtenerTexto(PASSWORD_ERROR);
        assertEquals(
                "Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.",
                error
        );
    }

    @Test
    @DisplayName("CP04 - Contraseñas no coinciden")
    void cp04_passwordNoCoincide() {
        limpiarDatos();

        llenarFormulario("Juan", "juan.cp04@gmail.com", "Abc12345", "Abc99999");
        registrar();

        String error = obtenerTexto(CONFIRM_PASSWORD_ERROR);
        assertEquals("Las contraseñas no coinciden.", error);
    }

    @Test
    @DisplayName("CP05 - Correo duplicado")
    void cp05_correoDuplicado() {
        limpiarDatos();

        String correoDuplicado = "duplicado.cp05@gmail.com";

        // Primer registro válido.
        llenarFormulario("Juan", correoDuplicado, "Abc12345", "Abc12345");
        registrar();

        String mensajeRegistro = obtenerTexto(ALERT_BOX);
        assertEquals("Usuario registrado correctamente.", mensajeRegistro);

        // No se debe limpiar localStorage aquí, porque se perdería el usuario registrado.
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME));

        // Segundo intento con el mismo correo.
        llenarFormulario("Pedro", correoDuplicado, "Abc12345", "Abc12345");
        registrar();

        String error = obtenerTexto(EMAIL_ERROR);
        assertEquals("Este correo ya está registrado.", error);
    }

    @Test
    @DisplayName("CP06 - Campos vacíos")
    void cp06_camposVacios() {
        limpiarDatos();

        registrar();

        String error = obtenerTexto(FULL_NAME_ERROR);
        assertEquals("El nombre completo es obligatorio.", error);
    }
}