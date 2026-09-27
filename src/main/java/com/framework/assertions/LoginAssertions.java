package com.framework.assertions;

import org.testng.Assert;

/** Verifications for the login/logout flow. Operates on values supplied by services, never on pages. */
public class LoginAssertions {

    public void verifyInventoryPageLoaded(boolean isLoaded) {
        Assert.assertTrue(isLoaded, "Inventory page should be displayed after login");
    }

    public void verifyErrorDisplayed(boolean isDisplayed) {
        Assert.assertTrue(isDisplayed, "Error message should be shown");
    }

    public void verifyErrorContains(String actualError, String expectedFragment) {
        Assert.assertTrue(actualError.contains(expectedFragment),
                "Expected error to contain: " + expectedFragment + " but was: " + actualError);
    }

    public void verifyOnLoginPage(String currentUrl) {
        Assert.assertTrue(currentUrl.endsWith("/"), "Should be back on the login page but was: " + currentUrl);
    }
}
