package com.framework.pages.modules;

import com.framework.pages.BasePage;
import com.framework.pages.LoginPage;
import org.openqa.selenium.By;

/** Slide-out navigation menu opened from {@link HeaderModule#openMenu()}. */
public class BurgerMenuModule implements BasePage {

    private final By logoutLink = By.id("logout_sidebar_link");

    public LoginPage logout() {
        click(logoutLink);
        return new LoginPage();
    }
}
