package com.framework.pages;

import com.framework.driver.DriverManager;
import com.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

/** Common behavior shared by every Page Object: waits, safe interactions, PageFactory init. */
public interface BasePage {

    /** Fresh per call (not a shared field) so it always waits on the current thread's current driver. */
    default WaitUtils waitUtils() {
        return new WaitUtils();
    }

    default void click(By locator) {
        WaitUtils waitUtils = waitUtils();
        waitUtils.waitForClickable(locator);
        waitUtils.waitForStablePosition(locator).click();
    }

    default void type(By locator, String text) {
        WebElement element = waitUtils().waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    default String getText(By locator) {
        return waitUtils().waitForVisible(locator).getText();
    }

    default List<String> getTexts(By locator) {
        return waitUtils().waitForAllVisible(locator).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    default boolean isDisplayed(By locator) {
        try {
            return waitUtils().waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    default String getCurrentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    default String getTitle() {
        return DriverManager.getDriver().getTitle();
    }
}
