package com.mycompany.pruebaselenium;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

public class EjercicioSeleniumJava {

    static WebDriver driver;

    public static void main(String[] args) {

        driver = new ChromeDriver();

        // 🔹 Cambiar la ruta
        driver.get("http://localhost:3000/index.html");

        cp01_registroValido();

        cp02_correoInvalido();

        cp03_passwordCorta();

        cp04_passwordNoCoincide();

        cp05_correoDuplicado();

        cp06_camposVacios();

        driver.quit();

    }

// 🔹 CP01 - Registro válido
    public static void cp01_registroValido() {

        driver.navigate().refresh();

        driver.findElement(By.id("fullName")).sendKeys("Juan Perez");

        driver.findElement(By.id("email")).sendKeys("juan@gmail.com");

        driver.findElement(By.id("password")).sendKeys("Abc12345");

        driver.findElement(By.id("confirmPassword")).sendKeys("Abc12345");

        driver.findElement(By.cssSelector("button")).click();

        boolean usuarioCreado = driver.findElements(By.id("usersContainer")).size() > 0;

        if (usuarioCreado) {

            System.out.println("CP01 PASÓ");

        } else {

            System.out.println("CP01 FALLÓ");

        }

    }

// 🔹 CP02 - Correo invalido
    public static void cp02_correoInvalido() {

        driver.navigate().refresh();

        driver.findElement(By.id("fullName")).sendKeys("Juan");

        driver.findElement(By.id("email")).sendKeys("juanemail.com"); // ❌ sin @

        driver.findElement(By.id("password")).sendKeys("Abc12345");

        driver.findElement(By.id("confirmPassword")).sendKeys("Abc12345");

        driver.findElement(By.cssSelector("button")).click();

        String error = driver.findElement(By.id("emailError")).getText();

        if (!error.isEmpty()) {

            System.out.println("CP02 PASÓ");

        } else {

            System.out.println("CP02 FALLÓ");

        }

    }

// 🔹 CP03 - Password corta
    public static void cp03_passwordCorta() {

        driver.navigate().refresh();

        driver.findElement(By.id("fullName")).sendKeys("Juan");

        driver.findElement(By.id("email")).sendKeys("juan@gmail.com");

        driver.findElement(By.id("password")).sendKeys("abc");

        driver.findElement(By.id("confirmPassword")).sendKeys("abc");

        driver.findElement(By.cssSelector("button")).click();

        String error = driver.findElement(By.id("passwordError")).getText();

        if (!error.isEmpty()) {

            System.out.println("CP03 PASÓ");

        } else {

            System.out.println("CP03 FALLÓ");

        }

    }

// 🔹 CP04 - Contraseñas no coinciden
    public static void cp04_passwordNoCoincide() {

        driver.navigate().refresh();

        driver.findElement(By.id("fullName")).sendKeys("Juan");

        driver.findElement(By.id("email")).sendKeys("juan@gmail.com");

        driver.findElement(By.id("password")).sendKeys("Abc12345");

        driver.findElement(By.id("confirmPassword")).sendKeys("Abc99999");

        driver.findElement(By.cssSelector("button")).click();

        String error = driver.findElement(By.id("confirmPasswordError")).getText();

        if (!error.isEmpty()) {

            System.out.println("CP04 PASÓ");

        } else {

            System.out.println("CP04 FALLÓ");

        }

    }

// 🔹 CP05 - Campos vacíos
    public static void cp05_correoDuplicado() {

        driver.navigate().refresh();

// 🔹 Primer registro (válido)
        driver.findElement(By.id("fullName")).sendKeys("Juan");

        driver.findElement(By.id("email")).sendKeys("juan@gmail.com");

        driver.findElement(By.id("password")).sendKeys("Abc12345");

        driver.findElement(By.id("confirmPassword")).sendKeys("Abc12345");

        driver.findElement(By.cssSelector("button")).click();

        driver.navigate().refresh();

// 🔹 Segundo intento con el mismo correo
        driver.findElement(By.id("fullName")).sendKeys("Pedro");

        driver.findElement(By.id("email")).sendKeys("juan@gmail.com"); // mismo correo

        driver.findElement(By.id("password")).sendKeys("Abc12345");

        driver.findElement(By.id("confirmPassword")).sendKeys("Abc12345");

        driver.findElement(By.cssSelector("button")).click();

        String alert = driver.findElement(By.id("emailError")).getText();

        if (!alert.isEmpty()) {

            System.out.println("CP05 PASÓ");

        } else {

            System.out.println("CP05 FALLÓ");

        }

    }

// 🔹 CP06 - Campos vacíos
    public static void cp06_camposVacios() {

        driver.navigate().refresh();

        driver.findElement(By.cssSelector("button")).click();

        String error = driver.findElement(By.id("fullNameError")).getText();

        if (!error.isEmpty()) {

            System.out.println("CP06 PASÓ");

        } else {

            System.out.println("CP06 FALLÓ");

        }

    }

}
