package com.framework.driver;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DriverManager {

    private static final ThreadLocal<Map<Integer, WebDriver>> MAP_OF_DRIVERS =
            ThreadLocal.withInitial(HashMap::new);
    private static final ThreadLocal<Integer> CURRENT_IDENTIFIER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        Integer id = CURRENT_IDENTIFIER.get();
        return id == null ? null : MAP_OF_DRIVERS.get().get(id);
    }

    public static WebDriver getDriver(Integer identifier) {
        return MAP_OF_DRIVERS.get().get(identifier);
    }

    public static void addToDriverMap(WebDriver driver, Integer identifier) {
        MAP_OF_DRIVERS.get().put(identifier, driver);
        CURRENT_IDENTIFIER.set(identifier);
    }

    /** Lets a test act on a previously started session again after starting another one. */
    public static void switchTo(Integer identifier) {
        if (!MAP_OF_DRIVERS.get().containsKey(identifier)) {
            throw new IllegalStateException("No active driver session for identifier " + identifier);
        }
        CURRENT_IDENTIFIER.set(identifier);
    }

    /** Identifiers of sessions still open on the current thread, e.g. for end-of-test cleanup. */
    public static Set<Integer> activeIdentifiers() {
        return new HashMap<>(MAP_OF_DRIVERS.get()).keySet();
    }

    /** Quits the driver for this identifier specifically and removes it from the map atomically. */
    public static void quitDriver(Integer identifier) {
        Map<Integer, WebDriver> drivers = MAP_OF_DRIVERS.get();
        WebDriver driver = drivers.remove(identifier);
        if (driver != null) {
            driver.quit();
        }
        if (identifier.equals(CURRENT_IDENTIFIER.get())) {
            CURRENT_IDENTIFIER.remove();
        }
        if (drivers.isEmpty()) {
            MAP_OF_DRIVERS.remove();
        }
    }
}
