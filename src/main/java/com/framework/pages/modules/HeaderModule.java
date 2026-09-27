package com.framework.pages.modules;

import com.framework.pages.BasePage;
import com.framework.pages.CartPage;
import org.openqa.selenium.By;

/** Primary header shown on every logged-in page: cart link, cart badge and burger-menu button. */
public class HeaderModule implements BasePage {

    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");

    /** Badge is only rendered when the cart is non-empty, so a missing badge means 0. */
    public int getCartCount() {
        if (!isDisplayed(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(getText(cartBadge));
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }

    public BurgerMenuModule openMenu() {
        click(burgerMenuButton);
        return new BurgerMenuModule();
    }
}
