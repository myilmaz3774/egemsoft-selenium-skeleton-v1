package com.egemsoft;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        try {
            // 1-3: Tarayıcı başlatılır, ana sayfa açılır ve doğrulanır.
            driver.get(BASE_URL);
            Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#slider-carousel"))).isDisplayed(), "Ana sayfa görüntülenemedi.");

            Credentials credentials = hasCredentials(email, password)
                    ? new Credentials(email, password)
                    : createTemporaryUser(driver, wait);

            // Geçici kullanıcı oluşturulduysa site oturumu otomatik açar.
            // Ödev senaryosundaki giriş adımlarını çalıştırmak için oturum kapatılır.
            if (!hasCredentials(email, password)) {
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/logout']"))).click();
                wait.until(ExpectedConditions.urlContains("/login"));
            }

            // 4-5: Giriş ekranı açılır ve başlığı doğrulanır.
            if (!driver.getCurrentUrl().contains("/login")) {
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/login']"))).click();
            }
            Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[normalize-space()='Login to your account']"))).isDisplayed(),
                    "Giriş formu başlığı görünmüyor.");

            // 6-7: Bilgiler girilir ve login butonuna tıklanır.
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[data-qa='login-email']"))).sendKeys(credentials.email());
            driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys(credentials.password());
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

    private boolean hasCredentials(String email, String password) {
        return email != null && !email.isBlank() && password != null && !password.isBlank();
    }

    /** Ortam değişkeni verilmediğinde testin bağımsız çalışması için geçici hesap oluşturur. */
    private Credentials createTemporaryUser(WebDriver driver, WebDriverWait wait) {
        String email = "selenium." + System.currentTimeMillis() + "@example.com";
        String password = "Test12345";

        driver.get(BASE_URL + "login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[data-qa='signup-name']"))).sendKeys("Selenium Test");
        driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(email);
        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[data-qa='password']"))).sendKeys(password);
        driver.findElement(By.cssSelector("select[id='days']")).sendKeys("1");
        driver.findElement(By.cssSelector("select[id='months']")).sendKeys("January");
        driver.findElement(By.cssSelector("select[id='years']")).sendKeys("2000");
        driver.findElement(By.cssSelector("input[data-qa='first_name']")).sendKeys("Selenium");
        driver.findElement(By.cssSelector("input[data-qa='last_name']")).sendKeys("Test");
        driver.findElement(By.cssSelector("input[data-qa='address']")).sendKeys("Test Adresi 1");
        driver.findElement(By.cssSelector("select[data-qa='country']")).sendKeys("Canada");
        driver.findElement(By.cssSelector("input[data-qa='state']")).sendKeys("Ontario");
        driver.findElement(By.cssSelector("input[data-qa='city']")).sendKeys("Toronto");
        driver.findElement(By.cssSelector("input[data-qa='zipcode']")).sendKeys("M5V 1E3");
        driver.findElement(By.cssSelector("input[data-qa='mobile_number']")).sendKeys("5551234567");
        // Sayfadaki reklam katmanı normal tıklamayı engelleyebildiği için buton DOM üzerinden tıklanır.
        WebElement createAccountButton = driver.findElement(By.cssSelector("button[data-qa='create-account']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", createAccountButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createAccountButton);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-qa='continue-button']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(., 'Logged in as')]")));

        return new Credentials(email, password);
    }

    private record Credentials(String email, String password) {
    }
}
