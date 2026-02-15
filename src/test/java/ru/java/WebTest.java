package ru.java;

import junit.framework.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static junit.framework.Assert.assertEquals;


public class WebTest {

    private static WebDriver driver;

    @BeforeAll
    @DisplayName("1. инициализации браузера и переходе по ссылке")
    public static void addAndOpenBrowserExplorer() {

        driver = new ChromeDriver();
        driver.get("https://pikabu.ru/");
    }

    @AfterAll
    public static void closeBrowserExplorer() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Авторизация - не успешна")
    public void checkPageTitle() {
        //2.Убедиться, что заголовок сайта: «Горячее – самые интересные и обсуждаемые посты | Пикабу».
        assertEquals(driver.getTitle(), "Горячее – самые интересные и обсуждаемые посты | Пикабу");

        //3. Кликнуть на кнопку «Войти»")
        driver.findElement(By.cssSelector(".pkb-normal-btn.header-right-menu__login-button")).click(); //  $('.pkb-normal-btn.header-right-menu__login-button') .header__right-menu .header__signin

        // 4. Убедиться, что отображается модальное окно «Авторизация», отображаются поля «Логин» и «Пароль», отображается кнопка «Войти».")
        assertEquals(driver.findElement(By.cssSelector(".auth__header")).getText(), "Войти");
        assertEquals(driver.findElement(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='username']")).getAttribute("placeholder"), "Логин");
        assertEquals(driver.findElement(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='password']")).getAttribute("placeholder"), "Пароль");
        assertEquals(driver.findElement(By.cssSelector(".auth__submit, button[type='submit']")).isDisplayed(), true);

        //5. Ввести в поля данные в формате логин/пароль – Qwerty/Qwerty и нажать «Войти»")
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement loginField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='username']")));
        loginField.sendKeys("Qwerty");

        WebElement passwordField = driver.findElement(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='password']"));
        passwordField.sendKeys("Qwerty");
        passwordField.sendKeys(Keys.ENTER);

        //6. Убедиться, что появилось сообщение об ошибке, и его текст: «Ошибка. Вы ввели неверные данные авторизации»")


        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='auth__error auth__error_top'])[2]")));
        assertEquals("Ошибка. Вы ввели неверные данные авторизации", error.getText());
    }
}