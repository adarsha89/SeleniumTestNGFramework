package com.framework.pages;

import org.openqa.selenium.By;

public class CheckoutStepTwoPage implements LoggedInPage {

    private final By itemTotalLabel = By.className("summary_subtotal_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");


    public double getItemTotal() {
        return parseCurrency(getText(itemTotalLabel));
    }

    public double getTotal() {
        return parseCurrency(getText(totalLabel));
    }

    private double parseCurrency(String label) {
        return Double.parseDouble(label.replaceAll("[^0-9.]", ""));
    }

    public CheckoutCompletePage finishCheckout() {
        click(finishButton);
        return new CheckoutCompletePage();
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage();
    }
}
