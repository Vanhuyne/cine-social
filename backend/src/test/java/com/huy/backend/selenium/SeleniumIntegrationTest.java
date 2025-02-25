package com.huy.backend.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
        int repeatCount = 10;
        List<String> listEmail = generateDotTrickEmails("thanvanh.uy.157@gmail.com", repeatCount);
        for (int i = 0; i < listEmail.size(); i++) {
            System.out.println("Starting iteration: " + i);

            String url = "https://avail.openedu.net/en/signup?next=/en/courses/course-avail-36779?ref_by=";
            String code = "f8cDS8gZorzy4Dgu";
            driver.get(url + code);

            // Create an explicit wait to handle the dynamic loading of elements
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // For instance, if the Display Name field has an ID of "displayName"
            WebElement displayNameField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/div/input"));

            WebElement emailField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div/input"));
            WebElement passwordField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[3]/div/div/input"));
            WebElement confirmPasswordField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[4]/div/div/input"));

            String username = this.generateRandomUsername(7);
            String selectedEmail = listEmail.get(i);
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

            Thread.sleep(3000);
        }
        // Navigate to the specified URL in the incognito window

    }

    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static List<String> generateDotTrickEmails(String email, int count) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format. Must contain '@'");
        }

        String username = email.substring(0, email.indexOf('@'));
        String domain = email.substring(email.indexOf('@'));

        List<String> dotTrickEmails = new ArrayList<>();
        dotTrickEmails.add(email); // Add the original email

        if (count <= 1) return dotTrickEmails; // If count is 1 or less, return the original email

        int maxCombinations = (1 << (username.length() - 1)) - 1; // Exclude first char from dot combinations
        for (int i = 1; i <= Math.min(count - 1, maxCombinations); i++) { // Limit to possible combinations, count -1 because we already add original email
            StringBuilder sb = new StringBuilder();
            sb.append(username.charAt(0)); // Always include the first character

            for (int j = 1; j < username.length(); j++) { // Start from the second character
                if ((i & (1 << (j - 1))) > 0) { // Check if (j-1)th bit is set
                    sb.append('.');
                }
                sb.append(username.charAt(j));
            }
            dotTrickEmails.add(sb.toString() + domain);
        }
        //If user request more than possible combination, we can add number to the end of email
        int currentEmailGenerated = dotTrickEmails.size();
        if (currentEmailGenerated < count) {
            for (int i = 1; currentEmailGenerated < count; i++) {
                dotTrickEmails.add(username + "+" + i + domain);
                currentEmailGenerated++;
            }
        }

        return dotTrickEmails;
    }

    @Test
    public void generateDotTrickEmails() {
        String email = "thanvanhuyy@gmail.com";
        int count = 5;
        List<String> dotTrickEmails = generateDotTrickEmails(email, count);
        for (String dotTrickEmail : dotTrickEmails) {
            System.out.println(dotTrickEmail);
        }

    }

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
