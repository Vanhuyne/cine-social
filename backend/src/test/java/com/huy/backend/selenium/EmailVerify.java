package com.huy.backend.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmailVerify {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Initialize the ChromeDriver with the options
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testGetVerifyEmail() throws InterruptedException, IOException {
        driver.get("https://mail.google.com/mail/u/0/#inbox");

        // Enter Email
        WebElement emailField = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/c-wiz/div/div[2]/div/div/div[1]/form/span/section/div/div/div[1]/div/div[1]/div/div[1]/input"));
        emailField.sendKeys("thanvanhuyy1@gmail.com");
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


            }
            List<WebElement> verifyLinks = driver.findElements(By.xpath("//a[contains(@href,'verify')]"));
            int linkCount = 0;
            for (WebElement link : verifyLinks) {
                String href = link.getAttribute("href");
                System.out.println("Verification URL " + linkCount++ + " " + href);
                writer.write(href);
                writer.newLine();
            }
//
            // Optional pause before moving to the next conversation item
            Thread.sleep(1000);
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

    @Test
    public void processVerificationLinks() throws IOException, InterruptedException {
        // Read all links from the file into a list.
        List<String> links = readLinksFromFile("verification_links.txt");

        // Use an iterator so that we can remove processed links safely.
        Iterator<String> iterator = links.iterator();
        while (iterator.hasNext()) {
            String link = iterator.next();
            if (link.trim().isEmpty()) {
                continue;
            }

            try {
                // Begin processing the link
                driver.get(link);
                Thread.sleep(1500);

                List<WebElement> verificationFailedElements = driver.findElements(
                        By.xpath("//*[text()='Verification Failed']")
                );
                if (!verificationFailedElements.isEmpty()) {
                    // If the "Verification Failed" message is found, skip this link.
                    System.out.println("Verification failed for link: " + link);
                    iterator.remove();
                    updateVerificationLinksFile(links);
                    continue;
                }

                WebElement elementBackHome = driver.findElement(By.xpath("/html/body/div/div[1]/div/div/button"));
                elementBackHome.click();
                Thread.sleep(2000);

                WebElement elementRefresh = driver.findElement(By.xpath("/html/body/header/div[2]/ul/li[1]/a"));
                elementRefresh.click();

                WebElement course = driver.findElement(By.xpath("/html/body/div/div/div/a/div"));
                course.click();
                Thread.sleep(2000);

                // Save the original window handle before opening a new tab
                String originalWindow = driver.getWindowHandle();

                // Wait for the new tab to open (expecting 2 windows)
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.numberOfWindowsToBe(2));

                // Switch to the new tab
                Set<String> windowHandles = driver.getWindowHandles();
                for (String handle : windowHandles) {
                    if (!handle.equals(originalWindow)) {
                        driver.switchTo().window(handle);
                        break;
                    }
                }
                Thread.sleep(3000);

                // Perform actions in the new tab
                WebElement elementGoToCourse = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div[3]/div[2]/div/button[1]"));
                elementGoToCourse.click();
                Thread.sleep(3000);

                WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement iframeContainer = wait1.until(
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.aspect-video iframe"))
                );
                Thread.sleep(3000);

                WebElement elementNext = driver.findElement(By.xpath("/html/body/div/div/div[1]/div[1]/div[2]/div/div/button[2]"));
                elementNext.click();
                Thread.sleep(5000);

                // Close the new tab and switch back to the original
                driver.close();
                driver.switchTo().window(originalWindow);

                // If all actions are successful, remove the link from the list.
                iterator.remove();
                // Update the file immediately with the remaining links.
                updateVerificationLinksFile(links);

            } catch (Exception e) {
                // If there is an error processing this link, log it and keep the link in the file.
                System.err.println("Error processing link: " + link + " - " + e.getMessage());
            }
        }
    }

    /**
     * Updates the verification_links.txt file with the remaining unprocessed links.
     */
    private void updateVerificationLinksFile(List<String> remainingLinks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("verification_links.txt"))) {
            for (String link : remainingLinks) {
                writer.write(link);
                writer.newLine();
            }
        }
    }

    public static List<String> readLinksFromFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .map(String::trim)
                .filter(line -> !line.isEmpty()) // Remove empty lines
                .collect(Collectors.toList());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
