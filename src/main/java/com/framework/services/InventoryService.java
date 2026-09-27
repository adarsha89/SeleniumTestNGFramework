package com.framework.services;

import com.framework.assertions.InventoryAssertions;
import com.framework.pages.InventoryPage;

/** Inventory (products) page actions and verifications. */
public class InventoryService {

    private final InventoryAssertions assertions = new InventoryAssertions();

    public InventoryService sortByNameAscending() {
        new InventoryPage().sortBy(InventoryPage.SortOption.NAME_ASC);
        return this;
    }

    public InventoryService sortByNameDescending() {
        new InventoryPage().sortBy(InventoryPage.SortOption.NAME_DESC);
        return this;
    }

    public InventoryService sortByPriceLowToHigh() {
        new InventoryPage().sortBy(InventoryPage.SortOption.PRICE_LOW_HIGH);
        return this;
    }

    public InventoryService sortByPriceHighToLow() {
        new InventoryPage().sortBy(InventoryPage.SortOption.PRICE_HIGH_LOW);
        return this;
    }

    public InventoryService addItemToCart(String productName) {
        new InventoryPage().addItemToCartByName(productName);
        return this;
    }

    public InventoryService addFirstNItemsToCart(int n) {
        new InventoryPage().addFirstNItemsToCart(n);
        return this;
    }

    public InventoryService openCart() {
        new InventoryPage().header().openCart();
        return this;
    }

    public InventoryService verifyPricesSortedAscending() {
        assertions.verifyPricesSortedAscending(new InventoryPage().getItemPrices());
        return this;
    }

    public InventoryService verifyNamesSortedDescending() {
        assertions.verifyNamesSortedDescending(new InventoryPage().getItemNames());
        return this;
    }

    public InventoryService verifyCartBadgeCount(int expectedCount) {
        assertions.verifyCartBadgeCount(new InventoryPage().header().getCartCount(), expectedCount);
        return this;
    }
}
