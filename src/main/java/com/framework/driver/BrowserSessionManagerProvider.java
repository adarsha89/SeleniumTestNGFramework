package com.framework.driver;

import com.framework.config.ConfigReader;

public final class BrowserSessionManagerProvider {

    private static final int MAX_BROWSER_SESSIONS = Integer.parseInt(ConfigReader.get("MAX_BROWSER_SESSIONS"));

    private static final BrowserSessionManager MANAGER =
            new BrowserSessionManager(MAX_BROWSER_SESSIONS);

    private BrowserSessionManagerProvider() {
    }

    public static BrowserSessionManager get() {
        return MANAGER;
    }
}
