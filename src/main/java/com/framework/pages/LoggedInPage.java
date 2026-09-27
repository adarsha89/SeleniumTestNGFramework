package com.framework.pages;

import com.framework.pages.modules.HeaderModule;

/** A page reachable only after login, and therefore carrying the shared header module. */
public interface LoggedInPage extends BasePage {

    default HeaderModule header() {
        return new HeaderModule();
    }
}
