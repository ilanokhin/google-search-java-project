package org.myproject.ui.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");
        String headless = System.getProperty("headless", "false");
        String images = System.getProperty("images", "true");

        System.out.printf("\nЗапускается браузер %s...\n", getBrowserName(browser));

        if (browser.equals("firefox")) driver = new FirefoxDriver(getFirefoxOptions(headless, images));
        else driver = new ChromeDriver(getChromeOptions(headless, images));

        System.out.println("\nПроводятся тесты...");
    }

    @AfterEach
    public void tearDown() {
        String browser = System.getProperty("browser", "chrome");
        System.out.printf("\nБраузер %s закрывается...\n", getBrowserName(browser));

        if (driver != null) driver.quit();
    }

    private String getBrowserName(String browser) {
        if (browser.equals("firefox")) return "Mozilla Firefox";
        else return "Google Chrome";
    }

    private ChromeOptions getChromeOptions(String headless, String images) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-blink-features=AutomationControlled");

        if (headless.equals("true")) options.addArguments("--headless");
        if (images.equals("false")) options.addArguments("--blink-settings=imagesEnabled=false");

        return options;
    }

    private FirefoxOptions getFirefoxOptions(String headless, String images) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--no-sandbox");

        if (headless.equals("true")) options.addArguments("--headless");
        if (images.equals("false")) options.addArguments("--blink-settings=imagesEnabled=false");

        return options;
    }
}
