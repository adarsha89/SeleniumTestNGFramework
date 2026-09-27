package com.framework.assertions;

import org.testng.Assert;

/** Verifications for the cart page. */
public class CartAssertions {

    public void verifyItemCount(int actualCount, int expectedCount) {
        Assert.assertEquals(actualCount, expectedCount, "Cart should contain " + expectedCount + " item(s)");
    }
}
