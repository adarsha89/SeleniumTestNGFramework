package com.framework.pages;

import com.framework.driver.DriverManager;
import com.framework.pages.modules.ErrorMessageModule;
import org.openqa.selenium.By;

public class LoginPage implements BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");


    public LoginPage open(String baseUrl) {
        DriverManager.getDriver().get(baseUrl);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return new InventoryPage();
    }

    /** Use when the login is expected to fail (e.g. locked_out_user) and inventory never loads. */
    public LoginPage loginExpectingFailure(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return this;
    }

    public ErrorMessageModule errorMessage() {
        return new ErrorMessageModule();
    }
}
