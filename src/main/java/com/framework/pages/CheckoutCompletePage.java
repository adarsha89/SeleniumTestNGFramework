package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage implements BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By backHomeButton = By.id("back-to-products");


    public String getConfirmationMessage() {
        return getText(completeHeader);
    }

    public InventoryPage backToProducts() {
        click(backHomeButton);
        return new InventoryPage();
    }
}
