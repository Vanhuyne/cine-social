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
        emailField.sendKeys("thanvanhuy157@gmail.com");
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
        try (BufferedWriter writerLinks = new BufferedWriter(new FileWriter("verification_links.txt"));
             BufferedWriter writerEmails = new BufferedWriter(new FileWriter("emails.txt"))) {
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
            // Retrieve verification links in the current conversation
            List<WebElement> verifyLinks = driver.findElements(By.xpath("//a[contains(@href,'verify')]"));
            // Retrieve email addresses from <span> elements with an 'email' attribute
            List<WebElement> emailElements = driver.findElements(By.xpath("//span[@class='g2' and @email]"));
            int linkCount = 0;
            for (WebElement link : verifyLinks) {
                String href = link.getAttribute("href");
                System.out.println("Verification URL " + linkCount++ + " " + href);
                writerLinks.write(href);
                writerLinks.newLine();
            }
            // Write all email addresses found
            if (!emailElements.isEmpty()) {
                for (WebElement emailElement : emailElements) {
                    String email = emailElement.getAttribute("email");
                    writerEmails.write(email);
                    writerEmails.newLine();
                    System.out.println("Email: " + email);
                }
            }

            // Optional pause before moving to the next conversation item
            Thread.sleep(1000);
        }

    }

//    AnhHo444137
//    Huy01203055232

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

                WebElement elementBackHome = driver.findElement(By.xpath("/html/body/div/div[1]/div/div/button"));
                String backHomeColor = elementBackHome.getCssValue("background-color");
                String backHomeHexColor = rgbaToHex(backHomeColor);
                if (backHomeHexColor.equalsIgnoreCase("#EF4444")) {
                    System.out.println("Verification failed for link: " + link);
                    iterator.remove();
                    updateVerificationLinksFile(links);
                    continue;
                } else {
                    elementBackHome.click();
                    Thread.sleep(2000);
                }


                WebElement elementRefresh = driver.findElement(By.xpath("/html/body/header/div[2]/ul/li[1]/a"));
                elementRefresh.click();

                WebElement course = driver.findElement(By.xpath("/html/body/div/div/div/a/div"));
                course.click();
                Thread.sleep(1500);

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

                // Perform actions in the new tab
//                WebElement elementGoToCourse = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div[3]/div[2]/div/button[1]"));
//                elementGoToCourse.click();
//                Thread.sleep(7000);
//
//                WebElement elementNext = driver.findElement(By.xpath("/html/body/div/div/div[1]/div[1]/div[2]/div/div/button[2]"));
//                elementNext.click();
//                Thread.sleep(3000);

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

    @Test
    public void loginToGmailAndSuccessSocialTask() throws InterruptedException, IOException {

        List<String> emails = readLinksFromFile("emails.txt");

        // Use an iterator so that we can remove processed links safely.
        Iterator<String> iterator = emails.iterator();
        while (iterator.hasNext()) {
            String email = iterator.next();
            if (email.trim().isEmpty()) {
                continue;
            }

            try {
                // Begin processing the link
                driver.get("https://avail.openedu.net/en/login?next=/en");
                WebElement emailField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/div/input"));
                emailField.sendKeys(email);
                WebElement passwordField = driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div/div/input"));
                passwordField.sendKeys("Huy01203055232");
                driver.findElement(By.xpath("/html/body/div/div[1]/div/form/button")).click();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                WebElement course = driver.findElement(By.xpath("/html/body/header/div[2]/ul/li[1]/a"));
                course.click();

                WebElement redirectCourse = driver.findElement(By.xpath("/html/body/div/div/div/a/div"));
                redirectCourse.click();

                String originalWindow = driver.getWindowHandle();
//
//                // Switch to the new tab
                Set<String> windowHandles = driver.getWindowHandles();
                for (String handle : windowHandles) {
                    if (!handle.equals(originalWindow)) {
                        driver.switchTo().window(handle);
                        break;
                    }
                }

                WebElement elementGoToCourse = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/button"));
                elementGoToCourse.click();

                // switch to the new tab (tab 3)
                Set<String> windowHandles2 = driver.getWindowHandles();
                for (String handle : windowHandles2) {
                    if (!handle.equals(originalWindow)) {
                        driver.switchTo().window(handle);
                        break;
                    }
                }

                WebElement startGame = driver.findElement(By.xpath("/html/body/div/div/div/div[3]/div"));
                startGame.click();

                Thread.sleep(10000);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Enter Email

        }

    }


    static String rgbaToHex(String rgba) {
        // Extract RGB values from rgba(r, g, b, a)
        String[] numbers = rgba.replace("rgba(", "").replace(")", "").split(", ");
        int r = Integer.parseInt(numbers[0]);
        int g = Integer.parseInt(numbers[1]);
        int b = Integer.parseInt(numbers[2]);

        // Convert to Hex
        return String.format("#%02X%02X%02X", r, g, b);
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
