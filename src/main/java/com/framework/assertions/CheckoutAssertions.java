package com.framework.assertions;

import org.testng.Assert;

/** Verifications for the checkout steps and order confirmation. */
public class CheckoutAssertions {

    public void verifyTotalIncludesTax(double itemTotal, double total) {
        Assert.assertTrue(total > itemTotal,
                "Total (" + total + ") should include tax on top of item subtotal (" + itemTotal + ")");
    }

    public void verifyConfirmationMessage(String actualMessage, String expectedMessage) {
        Assert.assertEquals(actualMessage, expectedMessage, "Order confirmation message mismatch");
    }

    public void verifyErrorContains(String actualError, String expectedFragment) {
        Assert.assertTrue(actualError.contains(expectedFragment),
                "Expected checkout error to contain: " + expectedFragment + " but was: " + actualError);
    }
}
