package com.framework.services;

import com.framework.assertions.CheckoutAssertions;
import com.framework.config.TestData;
import com.framework.pages.CheckoutCompletePage;
import com.framework.pages.CheckoutStepOnePage;
import com.framework.pages.CheckoutStepTwoPage;

/** Checkout flow (information, overview, completion) actions and verifications. */
public class CheckoutService {

    private static final String ORDER_CONFIRMATION = "Thank you for your order!";

    private final CheckoutAssertions assertions = new CheckoutAssertions();

    public CheckoutService enterCustomerInfo(String firstName, String lastName, String postalCode) {
        new CheckoutStepOnePage().fillInfoAndContinue(firstName, lastName, postalCode);
        return this;
    }

    public CheckoutService enterSampleCustomerInfo() {
        return enterCustomerInfo(TestData.SAMPLE_FIRST_NAME, TestData.SAMPLE_LAST_NAME, TestData.SAMPLE_POSTAL_CODE);
    }

    public CheckoutService continueWithoutCustomerInfo() {
        new CheckoutStepOnePage().continueExpectingError();
        return this;
    }

    public CheckoutService finish() {
        new CheckoutStepTwoPage().finishCheckout();
        return this;
    }

    public CheckoutService verifyTotalIncludesTax() {
        CheckoutStepTwoPage overview = new CheckoutStepTwoPage();
        assertions.verifyTotalIncludesTax(overview.getItemTotal(), overview.getTotal());
        return this;
    }

    public CheckoutService verifyOrderConfirmed() {
        assertions.verifyConfirmationMessage(new CheckoutCompletePage().getConfirmationMessage(), ORDER_CONFIRMATION);
        return this;
    }

    public CheckoutService verifyErrorContains(String expectedFragment) {
        assertions.verifyErrorContains(new CheckoutStepOnePage().getErrorMessage(), expectedFragment);
        return this;
    }
}
