package com.fwhyn.app.deandro.more;

import android.util.Log;

import org.junit.Test;

public class CoroutineTestJava {
    void threadForJava() throws InterruptedException {
        Thread thread = new Thread(() -> {
            Log.d("Test", "Running on thread: " + Thread.currentThread().getName());
        });

        thread.start();
        thread.join();
    }

    @Test
    public void testThread() throws InterruptedException {
        threadForJava();
    }
}
