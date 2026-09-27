package com.framework.driver;

import com.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Thread-safe WebDriver factory for parallel TestNG execution.
 *
 * Each test thread gets its own WebDriver instance via ThreadLocal, so tests can run
 * concurrently (parallel="tests"/"classes"/"methods" in testng.xml) without state leaking
 * across threads. Driver binaries are resolved automatically by Selenium Manager
 * (built into Selenium 4.6+) - no WebDriverManager dependency required.
 */
public final class DriverFactory {


    private DriverFactory() {
    }




    public static WebDriver createDriver() {
        String browserName = ConfigReader.browser();
        boolean headless = ConfigReader.headless();
        Browser browser = Browser.fromString(browserName);
        WebDriver webDriver = switch (browser) {
            case CHROME -> new ChromeDriver(chromeOptions(headless));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(headless));
            case EDGE -> new EdgeDriver(edgeOptions(headless));
        };

        webDriver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait.seconds", 0)));
        webDriver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds", 30)));
        webDriver.manage().window().maximize();
        return webDriver;
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }


    private enum Browser {
        CHROME, FIREFOX, EDGE;

        static Browser fromString(String value) {
            try {
                return Browser.valueOf(value.trim().toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Unsupported browser: " + value);
            }
        }
    }
}
