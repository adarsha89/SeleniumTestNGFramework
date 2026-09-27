package com.framework.utils;

import com.framework.config.ConfigReader;
import com.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/** Centralized explicit-wait helpers built on Selenium 4's FluentWait. */
public class WaitUtils {

    private final Wait<WebDriver> wait;

    public WaitUtils() {
        this.wait = new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds", 15)))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }





    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Guards against clicking an element mid CSS-transition (e.g. a sliding menu):
     * elementToBeClickable only checks displayed+enabled, so a click can land before
     * the element settles at its final position. Polls the element's rect until it is
     * unchanged across two consecutive checks.
     */
    public WebElement waitForStablePosition(By locator) {
        org.openqa.selenium.Rectangle[] previous = new org.openqa.selenium.Rectangle[1];
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            org.openqa.selenium.Rectangle current = element.getRect();
            boolean stable = current.equals(previous[0]);
            previous[0] = current;
            return stable ? element : null;
        });
    }

    public boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    public <T> T waitFor(Function<WebDriver, T> condition) {
        return wait.until(condition);
    }
}
