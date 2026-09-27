package com.framework.services;

import com.framework.assertions.LoginAssertions;
import com.framework.config.ConfigReader;
import com.framework.config.TestData;
import com.framework.pages.InventoryPage;
import com.framework.pages.LoginPage;

/**
 * Login/logout flow. Tests use this instead of touching LoginPage or LoginAssertions directly.
 * Pages are created per call, so the service always acts on the current thread's current driver.
 */
public class LoginService {

    private final LoginAssertions assertions = new LoginAssertions();

    public LoginService open() {
        new LoginPage().open(ConfigReader.baseUrl());
        return this;
    }

    public LoginService loginAs(String username, String password) {
        new LoginPage().loginAs(username, password);
        return this;
    }

    public LoginService loginAsStandardUser() {
        return open().loginAs(TestData.STANDARD_USER, TestData.PASSWORD);
    }

    /** Use when the login is expected to fail and the user stays on the login page. */
    public LoginService attemptLogin(String username, String password) {
        new LoginPage().loginExpectingFailure(username, password);
        return this;
    }

    public LoginService logout() {
        new InventoryPage().logout();
        return this;
    }

    public LoginService verifyLoggedIn() {
        assertions.verifyInventoryPageLoaded(new InventoryPage().isLoaded());
        return this;
    }

    public LoginService verifyLoginErrorContains(String expectedFragment) {
        LoginPage loginPage = new LoginPage();
        assertions.verifyErrorDisplayed(loginPage.isErrorDisplayed());
        assertions.verifyErrorContains(loginPage.getErrorMessage(), expectedFragment);
        return this;
    }

    public LoginService verifyOnLoginPage() {
        assertions.verifyOnLoginPage(new LoginPage().getCurrentUrl());
        return this;
    }
}
