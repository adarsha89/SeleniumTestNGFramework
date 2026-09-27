package com.framework.driver;

import org.openqa.selenium.WebDriver;

public class BrowserSession {

    public void start(Integer identifier){
        BrowserSessionManager browserSessionManager = BrowserSessionManagerProvider.get();
        browserSessionManager.acquire();
        try {
            WebDriver webDriver = DriverFactory.createDriver();
            DriverManager.addToDriverMap(webDriver, identifier);
        }catch (Exception ex){
            browserSessionManager.release();
            throw ex;
        }
    }
    public void stop(Integer identifier){
        try {
            DriverManager.quitDriver(identifier);
        } finally {
            BrowserSessionManagerProvider
                    .get()
                    .release();
        }
    }
}
