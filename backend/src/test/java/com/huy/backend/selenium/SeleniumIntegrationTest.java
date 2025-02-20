package com.huy.backend.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Random;

public class SeleniumIntegrationTest {

    private WebDriver driver;
    private static final String GMAIL_INBOX_URL = "https://mail.google.com/mail/u/8/#inbox";
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        // Initialize the ChromeDriver with the options
        driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    @Test
    public void openGoogle() throws InterruptedException {
        int repeatCount = 5;
        for (int i = 0; i < EMAIL_POOL.length; i++) {
            System.out.println("Starting iteration: " + i);

            String url = "https://avail.openedu.net/en/signup?next=/en/courses/course-avail-36779?ref_by=";
            String code = "2fNRcZSemAIcf5b9";
            driver.get(url + code);

            // Create an explicit wait to handle the dynamic loading of elements
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // For instance, if the Display Name field has an ID of "displayName"
            WebElement displayNameField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/div/input"));

            WebElement emailField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div/input"));
            WebElement passwordField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[3]/div/div/input"));
            WebElement confirmPasswordField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[4]/div/div/input"));

            String username = this.generateRandomUsername(7);
            String selectedEmail = this.EMAIL_POOL[i];
            // 3. Fill in the fields
            displayNameField.sendKeys(username);
            emailField.sendKeys(selectedEmail);
            passwordField.sendKeys("Huy01203055232");
            confirmPasswordField.sendKeys("Huy01203055232");

            // wait 3 seconds
            Thread.sleep(3000);

            // 4. Click the Sign Up button
            // Example: If the sign-up button has an ID of "signUpBtn"
            WebElement signUpButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/button"));
            signUpButton.click();

            Thread.sleep(15000);
        }
        // Navigate to the specified URL in the incognito window

    }

    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Example pool of emails
    private static final String[] EMAIL_POOL = {

    };

    /**
     * Generate a random alphanumeric string of a given length.
     *
     * @param length number of characters in the generated username
     * @return random alphanumeric string
     */
    public static String generateRandomUsername(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARS.length());
            sb.append(ALPHANUMERIC_CHARS.charAt(index));
        }

        return sb.toString();
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
