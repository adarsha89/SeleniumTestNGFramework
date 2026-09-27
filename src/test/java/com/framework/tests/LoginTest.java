package com.framework.tests;

import com.framework.config.TestData;
import com.framework.services.LoginService;
import com.framework.tests.base.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private final LoginService loginService = new LoginService();

    @Test(description = "Standard user can log in and lands on the inventory page")
    public void standardUserCanLogIn() {
        loginService.loginAsStandardUser()
                .verifyLoggedIn();
    }

    @Test(description = "Locked out user sees an error and stays on the login page")
    public void lockedOutUserCannotLogIn() {
        loginService.open()
                .attemptLogin(TestData.LOCKED_OUT_USER, TestData.PASSWORD)
                .verifyLoginErrorContains("locked out");
    }

    @Test(dataProvider = "invalidCredentials", description = "Invalid credential combinations are rejected")
    public void invalidCredentialsAreRejected(String username, String password, String expectedErrorFragment) {
        loginService.open()
                .attemptLogin(username, password)
                .verifyLoginErrorContains(expectedErrorFragment);
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"", "", "Username is required"},
                {TestData.STANDARD_USER, "", "Password is required"},
                {"invalid_user", "invalid_pass", "do not match"}
        };
    }

    @Test(description = "User can log out back to the login page")
    public void userCanLogOut() {
        loginService.loginAsStandardUser()
                .logout()
                .verifyOnLoginPage();
    }
}
