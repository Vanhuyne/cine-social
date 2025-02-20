package com.huy.backend.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.List;

public class EmailVerify {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        // Initialize the ChromeDriver with the options
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testGetVerifyEmail() throws InterruptedException, IOException {
        driver.get("https://mail.google.com/mail/u/0/#inbox");

        // Enter Email
        WebElement emailField = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/c-wiz/div/div[2]/div/div/div[1]/form/span/section/div/div/div[1]/div/div[1]/div/div[1]/input"));
        emailField.sendKeys("huyv20100@gmail.com");
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/c-wiz/div/div[3]/div/div[1]/div/div/button")).click();
        Thread.sleep(5000); // Replace with WebDriverWait in a real scenario

        // Enter Password
        WebElement passwordField = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/c-wiz/div/div[2]/div/div/div/form/span/section[2]/div/div/div[1]/div[1]/div/div/div/div/div[1]/div/div[1]/input"));
        passwordField.sendKeys("Huy01203055232");
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/c-wiz/div/div[3]/div/div[1]/div/div/button")).click();
        Thread.sleep(5000);

        // Click on the inbox button
        WebElement inboxButton = driver.findElement(By.xpath("/html/body/div[6]/div[3]/div/div[2]/div[1]/div[2]/div/div/div/div/div/div[2]/div/div/div[1]/div[1]/div/div[1]/div/div"));
        inboxButton.click();

        // 3. Click on the first email in the inbox table
        //    Adjust this XPath to match your Gmail layout.
        WebElement firstEmail = driver.findElement(By.xpath("//table[@class='F cf zt']/tbody/tr[1]"));
        firstEmail.click();
        Thread.sleep(3000);

        //
        WebElement hiddenButton = driver.findElement(By.xpath("/html/body/div[6]/div[3]/div/div[2]/div[2]/div/div/div/div[2]/div/div[1]/div/div[2]/div/div[2]/div[2]/div/div[3]/div[3]/div/div/span"));
        hiddenButton.click();
        Thread.sleep(2000);

        List<WebElement> conversationItems = driver.findElements(
                By.xpath("//div[@role='list' and @class='bh']/div[@role='listitem']")
        );


        // 4) Print how many messages (list items) are found
        System.out.println("Number of messages in this thread: " + conversationItems.size());

        //  --- Step 4: Open a file for writing verification links ---
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("verification_links.txt"))) {
            int itemCount = conversationItems.size();
            for (int i = 0; i < itemCount; i++) {
                // Re-fetch conversation items to avoid stale element issues
                conversationItems = driver.findElements(
                        By.xpath("//div[@role='list' and contains(@class, 'bh')]/div[@role='listitem']")
                );
                WebElement currentItem = conversationItems.get(i);

                // Click the conversation item
                currentItem.click();
                Thread.sleep(2000); // Wait for the content of the message to load

                // --- Step 5: Extract the verification link ---
                try {
                    WebElement verifyLink = driver.findElement(By.xpath("//a[contains(@href,'verify')]"));
                    String href = verifyLink.getAttribute("href");
                    System.out.println("Verification URL " + (i + 1) + ": " + href);
                    writer.write("Verification URL  " + (i + 1) + ": " + href);
                    writer.newLine();
                } catch (NoSuchElementException e) {
                    System.out.println("No verification link found in message " + (i + 1));
                    writer.write("No verification link found in message " + (i + 1));
                    writer.newLine();
                }

                // Optional pause before moving to the next conversation item
                Thread.sleep(1000);
            }
        }

        // Get the first 20 emails
//        List<WebElement> emailRows = driver.findElements(By.xpath("//table[@class='F cf zt']/tbody/tr"));
//
//        // 6. Determine how many emails we can safely iterate through (up to 28)
//        int maxEmails = Math.min(emailRows.size(), 28);
//        System.out.println("Number of emails: " + maxEmails);
//        for (int i = 0; i < maxEmails; i++) {
//            // Click the ith email
//            emailRows.get(i).click();
//            Thread.sleep(3000); // Let the email body load
//
//            // 7. Try to find a "Verify" link in the email (href containing "verify")
//            try {
//                WebElement verifyEmailButton = driver.findElement(By.xpath("//a[contains(@href,'verify')]"));
//                String verifyUrl = verifyEmailButton.getAttribute("href");
//                System.out.println("Email " + (i + 1) + " - Verification URL: " + verifyUrl);
//            } catch (NoSuchElementException e) {
//                System.out.println("Email " + (i + 1) + " - No verification link found.");
//            }
//
//            // 8. Navigate back to inbox to process the next email
//            driver.navigate().back();
//            Thread.sleep(3000);
//
//            // 9. Because navigating back can refresh the DOM, re-locate the email rows
//            emailRows = driver.findElements(By.xpath("//table[@class='F cf zt']/tbody/tr"));
//        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
