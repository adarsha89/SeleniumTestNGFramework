package com.framework.tests;

import com.framework.services.CartService;
import com.framework.services.CheckoutService;
import com.framework.services.InventoryService;
import com.framework.services.LoginService;
import com.framework.tests.base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    private final LoginService loginService = new LoginService();
    private final InventoryService inventoryService = new InventoryService();
    private final CartService cartService = new CartService();
    private final CheckoutService checkoutService = new CheckoutService();

    @BeforeMethod(alwaysRun = true)
    public void loginAndAddItems() {
        loginService.loginAsStandardUser();
        inventoryService.addFirstNItemsToCart(2);
    }

    @Test(description = "Full end-to-end checkout flow completes successfully")
    public void userCanCompleteCheckout() {
        inventoryService.openCart();
        cartService.verifyItemCount(2)
                .proceedToCheckout();
        checkoutService.enterSampleCustomerInfo()
                .verifyTotalIncludesTax()
                .finish()
                .verifyOrderConfirmed();
    }

    @Test(description = "Checkout requires first name, last name, and postal code")
    public void checkoutInfoIsRequired() {
        inventoryService.openCart();
        cartService.proceedToCheckout();
        checkoutService.continueWithoutCustomerInfo()
                .verifyErrorContains("First Name is required");
    }

    @Test(description = "Removing an item from the cart updates the item list")
    public void itemCanBeRemovedFromCart() {
        inventoryService.openCart();
        int before = cartService.getItemCount();
        cartService.removeFirstItem()
                .verifyItemCount(before - 1);
    }
}
