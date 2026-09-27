package com.framework.pages.modules;

import com.framework.pages.BasePage;
import org.openqa.selenium.By;

/** Inline form-validation error banner shared by the login and checkout-information forms. */
public class ErrorMessageModule implements BasePage {

    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public String getMessage() {
        return getText(errorMessage);
    }

    public boolean isShown() {
        return isDisplayed(errorMessage);
    }
}
