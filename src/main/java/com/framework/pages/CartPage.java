package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage implements BasePage {

    private final By cartItemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By removeButtonPrefix = By.cssSelector("button[data-test^='remove']");


    public List<String> getCartItemNames() {
        return getTexts(cartItemNames);
    }

    public CheckoutStepOnePage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutStepOnePage();
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage();
    }

    public CartPage removeFirstItem() {
        click(removeButtonPrefix);
        return this;
    }
}
