package com.mycompany.pruebaselenium;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EjercicioSeleniumJava {

    static WebDriver driver;
    static WebDriverWait wait;

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

    public static void main(String[] args) {
        try {
            inicializarDriver();

            cp01_registroValido();
            cp02_correoInvalido();
            cp03_passwordCorta();
            cp04_passwordNoCoincide();
            cp05_correoDuplicado();
            cp06_camposVacios();

        } catch (Exception e) {
            System.out.println("Error durante la ejecución de las pruebas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    public static void inicializarDriver() {
        ChromeOptions options = new ChromeOptions();

        // Configuración necesaria para ejecución en GitHub Actions o entornos sin interfaz gráfica.
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(URL);
    }

    public static void limpiarDatos() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME));
    }

    public static void llenarFormulario(String nombre, String correo, String password, String confirmPassword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME)).clear();
        driver.findElement(FULL_NAME).sendKeys(nombre);

        driver.findElement(EMAIL).clear();
        driver.findElement(EMAIL).sendKeys(correo);

        driver.findElement(PASSWORD).clear();
        driver.findElement(PASSWORD).sendKeys(password);

        driver.findElement(CONFIRM_PASSWORD).clear();
        driver.findElement(CONFIRM_PASSWORD).sendKeys(confirmPassword);
    }

    public static void clickRegistrar() {
        wait.until(ExpectedConditions.elementToBeClickable(REGISTER_BUTTON)).click();
    }

    public static boolean validarTexto(By elemento, String textoEsperado) {
        try {
            wait.until(ExpectedConditions.textToBe(elemento, textoEsperado));
            return driver.findElement(elemento).getText().equals(textoEsperado);
        } catch (Exception e) {
            return false;
        }
    }

    public static void imprimirResultado(String caso, boolean resultado) {
        if (resultado) {
            System.out.println(caso + " PASÓ");
        } else {
            System.out.println(caso + " FALLÓ");
        }
    }

    // CP01 - Registro válido
    public static void cp01_registroValido() {
        limpiarDatos();

        llenarFormulario("Juan Perez", "juan.cp01@gmail.com", "Abc12345", "Abc12345");
        clickRegistrar();

        boolean resultado = validarTexto(ALERT_BOX, "Usuario registrado correctamente.");
        imprimirResultado("CP01", resultado);
    }

    // CP02 - Correo inválido
    public static void cp02_correoInvalido() {
        limpiarDatos();

        llenarFormulario("Juan", "juanemail.com", "Abc12345", "Abc12345");
        clickRegistrar();

        boolean resultado = validarTexto(EMAIL_ERROR, "Ingresa un correo electrónico válido.");
        imprimirResultado("CP02", resultado);
    }

    // CP03 - Contraseña débil o corta
    public static void cp03_passwordCorta() {
        limpiarDatos();

        llenarFormulario("Juan", "juan.cp03@gmail.com", "abc", "abc");
        clickRegistrar();

        boolean resultado = validarTexto(
                PASSWORD_ERROR,
                "Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número."
        );

        imprimirResultado("CP03", resultado);
    }

    // CP04 - Contraseñas no coinciden
    public static void cp04_passwordNoCoincide() {
        limpiarDatos();

        llenarFormulario("Juan", "juan.cp04@gmail.com", "Abc12345", "Abc99999");
        clickRegistrar();

        boolean resultado = validarTexto(CONFIRM_PASSWORD_ERROR, "Las contraseñas no coinciden.");
        imprimirResultado("CP04", resultado);
    }

    // CP05 - Correo duplicado
    public static void cp05_correoDuplicado() {
        limpiarDatos();

        String correoDuplicado = "duplicado.cp05@gmail.com";

        // Primer registro válido
        llenarFormulario("Juan", correoDuplicado, "Abc12345", "Abc12345");
        clickRegistrar();

        boolean primerRegistro = validarTexto(ALERT_BOX, "Usuario registrado correctamente.");

        // Importante: aquí NO se debe usar limpiarDatos(), porque eso borraría el usuario registrado.
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FULL_NAME));

        // Segundo intento con el mismo correo
        llenarFormulario("Pedro", correoDuplicado, "Abc12345", "Abc12345");
        clickRegistrar();

        boolean correoDuplicadoDetectado = validarTexto(EMAIL_ERROR, "Este correo ya está registrado.");

        imprimirResultado("CP05", primerRegistro && correoDuplicadoDetectado);
    }

    // CP06 - Campos vacíos
    public static void cp06_camposVacios() {
        limpiarDatos();

        clickRegistrar();

        boolean resultado = validarTexto(FULL_NAME_ERROR, "El nombre completo es obligatorio.");
        imprimirResultado("CP06", resultado);
    }
}