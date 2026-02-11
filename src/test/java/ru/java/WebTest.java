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


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    @DisplayName("2. Убедиться, что заголовок сайта: «Горячее – самые интересные и обсуждаемые посты | Пикабу».")
    public void checkPageTitle() {
        //  Assert.assertEquals(driver.getCurrentUrl(), "https://pikabu.ru/");
        assertEquals(driver.getTitle(), "Горячее – самые интересные и обсуждаемые посты | Пикабу");
    }

    @Test
    @Order(2)
    @DisplayName("3. Кликнуть на кнопку «Войти»")
    public void clickLoginButton() {
        driver.findElement(By.cssSelector(".pkb-normal-btn.header-right-menu__login-button")).click(); //  $('.pkb-normal-btn.header-right-menu__login-button') .header__right-menu .header__signin
    }

    @Test
    @Order(3)
    @DisplayName("4. Убедиться, что отображается модальное окно «Авторизация», отображаются поля «Логин» и «Пароль», отображается кнопка «Войти».")
    public void authorizationWindowRight() {

        assertEquals(driver.findElement(By.cssSelector(".auth__header")).getText(), "Войти");
        assertEquals(driver.findElement(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='username']")).getAttribute("placeholder"), "Логин");
        assertEquals(driver.findElement(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='password']")).getAttribute("placeholder"), "Пароль");
        assertEquals(driver.findElement(By.cssSelector(".auth__submit, button[type='submit']")).isDisplayed(), true);
    }


    @Test
    @Order(4)
    @DisplayName("5. Ввести в поля данные в формате логин/пароль – Qwerty/Qwerty и нажать «Войти»")
    public void inputLoginPassword() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement loginField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='username']")));
        loginField.sendKeys("Qwerty");

        WebElement passwordField = driver.findElement(By.cssSelector(".popup_animate.popup_oldstyle.popup_show input[name='password']"));
        passwordField.sendKeys("Qwerty");
        passwordField.sendKeys(Keys.ENTER);
    }

    @Test
    @Order(5)
    @DisplayName("6. Убедиться, что появилось сообщение об ошибке, и его текст: «Ошибка. Вы ввели неверные данные авторизации»")
    public void errorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));


        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[contains(@class, 'auth__error_top')])[2]")));
        assertEquals("Ошибка. Вы ввели неверные данные авторизации", error.getText());
    }

}