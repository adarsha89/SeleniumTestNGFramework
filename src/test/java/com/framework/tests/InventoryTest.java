package com.framework.tests;

import com.framework.services.InventoryService;
import com.framework.services.LoginService;
import com.framework.tests.base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InventoryTest extends BaseTest {

    private final LoginService loginService = new LoginService();
    private final InventoryService inventoryService = new InventoryService();

    @BeforeMethod(alwaysRun = true)
    public void loginAsStandardUser() {
        loginService.loginAsStandardUser();
    }

    @Test(description = "Products can be sorted by price low to high")
    public void productsCanBeSortedByPriceLowToHigh() {
        inventoryService.sortByPriceLowToHigh()
                .verifyPricesSortedAscending();
    }

    @Test(description = "Products can be sorted by name Z to A")
    public void productsCanBeSortedByNameDescending() {
        inventoryService.sortByNameDescending()
                .verifyNamesSortedDescending();
    }

    @Test(description = "Adding items updates the cart badge count")
    public void addingItemsUpdatesCartBadge() {
        inventoryService.verifyCartBadgeCount(0)
                .addFirstNItemsToCart(3)
                .verifyCartBadgeCount(3);
    }
}
