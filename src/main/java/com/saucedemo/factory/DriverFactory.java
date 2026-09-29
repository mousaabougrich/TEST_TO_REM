package com.saucedemo.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import com.saucedemo.utils.ConfigReader;
import java.time.Duration;

public class DriverFactory {
    private static WebDriver driver;

    public static WebDriver initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();

        switch (browser) {
            case "chrome":                ChromeOptions chromeOptions = new ChromeOptions();
                if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":                driver = new FirefoxDriver();
                break;
            case "edge":                driver = new EdgeDriver();
                break;
            default:
                throw new RuntimeException("Browser not supported: " + browser);
        }

        getDriver().manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));
        getDriver().manage().window().maximize();

        return getDriver();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

