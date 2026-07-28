package com.egemsoft;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Test Case 4: kullanıcının giriş yapıp güvenli şekilde çıkış yapmasını doğrular. */
public class TestCase4LogoutTest {

    private static final String BASE_URL = "https://automationexercise.com/";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    @Test
    public void testLogoutUser() {
        String email = System.getenv("AUTOMATION_EXERCISE_EMAIL");
        String password = System.getenv("AUTOMATION_EXERCISE_PASSWORD");
        Assumptions.assumeTrue(email != null && !email.isBlank() && password != null && !password.isBlank(),
                "AUTOMATION_EXERCISE_EMAIL ve AUTOMATION_EXERCISE_PASSWORD tanımlanmalıdır.");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        try {
            // 1-3: Tarayıcı başlatılır, ana sayfa açılır ve doğrulanır.
            driver.get(BASE_URL);
            Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#slider-carousel"))).isDisplayed(), "Ana sayfa görüntülenemedi.");

            // 4-5: Giriş ekranı açılır ve başlığı doğrulanır.
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/login']"))).click();
            Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[normalize-space()='Login to your account']"))).isDisplayed(),
                    "Giriş formu başlığı görünmüyor.");

            // 6-7: Bilgiler girilir ve login butonuna tıklanır.
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[data-qa='login-email']"))).sendKeys(email);
            driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys(password);
            driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

            // 8: Oturum açıldığı doğrulanır.
            WebElement loggedInMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(., 'Logged in as')]")));
            Assertions.assertTrue(loggedInMessage.getText().contains("Logged in as"),
                    "Kullanıcının oturum açtığı doğrulanamadı.");

            // 9-10: Çıkış yapılır ve login sayfasına dönüş doğrulanır.
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/logout']"))).click();
            wait.until(ExpectedConditions.urlContains("/login"));
            Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[normalize-space()='Login to your account']"))).isDisplayed(),
                    "Çıkış sonrası giriş sayfasına yönlendirme başarısız oldu.");
        } finally {
            driver.quit();
        }
    }
}
