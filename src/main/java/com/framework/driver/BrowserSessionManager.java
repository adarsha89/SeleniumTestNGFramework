/* Limit number of browser sessions in use */

package com.framework.driver;

import java.util.concurrent.Semaphore;

public class BrowserSessionManager {

    private final Semaphore semaphore;

    public BrowserSessionManager(int maxSessions){
        this.semaphore = new Semaphore(maxSessions);
    }


    public void acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void release() {
        semaphore.release();
    }
}
