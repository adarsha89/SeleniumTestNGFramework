package com.framework.services;

import com.framework.assertions.CartAssertions;
import com.framework.pages.CartPage;

/** Cart page actions and verifications. */
public class CartService {

    private final CartAssertions assertions = new CartAssertions();

    public int getItemCount() {
        return new CartPage().getCartItemNames().size();
    }

    public CartService removeFirstItem() {
        new CartPage().removeFirstItem();
        return this;
    }

    public CartService continueShopping() {
        new CartPage().continueShopping();
        return this;
    }

    public CartService proceedToCheckout() {
        new CartPage().proceedToCheckout();
        return this;
    }

    public CartService verifyItemCount(int expectedCount) {
        assertions.verifyItemCount(getItemCount(), expectedCount);
        return this;
    }
}
