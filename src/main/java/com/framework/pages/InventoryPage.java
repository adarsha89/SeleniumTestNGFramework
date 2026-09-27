package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage implements LoggedInPage {

    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By addToCartButton = By.cssSelector("button[data-test^='add-to-cart']");
    private final By sortDropdown = By.className("product_sort_container");


    public boolean isLoaded() {
        return isDisplayed(pageTitle) && "Products".equals(getText(pageTitle));
    }

    public List<String> getItemNames() {
        return getTexts(itemName);
    }

    public List<Double> getItemPrices() {
        return waitUtils().waitForAllVisible(itemPrice).stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .toList();
    }

    public InventoryPage addItemToCartByName(String productName) {
        WebElement item = findItemContainer(productName);
        item.findElement(addToCartButton).click();
        return this;
    }

    public InventoryPage addFirstNItemsToCart(int n) {
        int added = 0;
        while (added < n) {
            List<WebElement> buttons = waitUtils().waitForAllVisible(addToCartButton);
            if (buttons.isEmpty()) {
                break;
            }
            waitUtils().waitForClickable(addToCartButton);
            buttons.get(0).click();
            added++;
        }
        return this;
    }

    private WebElement findItemContainer(String productName) {
        return waitUtils().waitForAllVisible(inventoryItems).stream()
                .filter(el -> el.findElement(itemName).getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementFoundException(productName));
    }

    public InventoryPage sortBy(SortOption option) {
        Select select = new Select(waitUtils().waitForVisible(sortDropdown));
        select.selectByValue(option.value);
        return this;
    }

    public enum SortOption {
        NAME_ASC("az"),
        NAME_DESC("za"),
        PRICE_LOW_HIGH("lohi"),
        PRICE_HIGH_LOW("hilo");

        final String value;

        SortOption(String value) {
            this.value = value;
        }
    }

    static class NoSuchElementFoundException extends RuntimeException {
        NoSuchElementFoundException(String productName) {
            super("No inventory item found with name: " + productName);
        }
    }
}
