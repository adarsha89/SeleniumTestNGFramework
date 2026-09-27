package com.framework.tests.base;

import com.framework.driver.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Every test class extends this. A new WebDriver is created per test *method* (not per class),
 * which keeps tests isolated and safe under any TestNG parallel mode - "methods", "classes",
 * or "tests" - since DriverFactory hands each thread its own instance via ThreadLocal.
 *
 * A test can open more than one session (e.g. BROWSER_SESSION.FIRST.start(), then
 * BROWSER_SESSION.SECOND.start(), switching between them via DriverManager.switchTo(id)).
 * tearDown() closes whichever sessions are still open on this thread, so a test doesn't
 * leak a browser session (and its BrowserSessionManager permit) just by forgetting to
 * call .stop() on every session it opened.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        BROWSER_SESSION.FIRST.start();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        for (Integer identifier : DriverManager.activeIdentifiers()) {
            new BrowserSession().stop(identifier);
        }
    }

    enum BROWSER_SESSION {

        FIRST(1),
        SECOND(2),
        THIRD(3);

        private final int sessionNumber;

        BROWSER_SESSION(int sessionNumber) {
            this.sessionNumber = sessionNumber;
        }

        public int getSessionNumber() {
            return sessionNumber;
        }

        public void start() {
            new BrowserSession().start(sessionNumber);
        }

        public void stop() {
            new BrowserSession().stop(sessionNumber);
        }
    }
}
