package com.framework.pages;

import com.framework.pages.modules.ErrorMessageModule;
import org.openqa.selenium.By;

public class CheckoutStepOnePage implements LoggedInPage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");

    public CheckoutStepTwoPage fillInfoAndContinue(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        click(continueButton);
        return new CheckoutStepTwoPage();
    }

    public CheckoutStepOnePage continueExpectingError() {
        click(continueButton);
        return this;
    }

    public ErrorMessageModule errorMessage() {
        return new ErrorMessageModule();
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage();
    }
}
