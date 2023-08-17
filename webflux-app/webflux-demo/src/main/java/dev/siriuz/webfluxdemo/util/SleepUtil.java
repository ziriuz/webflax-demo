package dev.siriuz.webfluxdemo.util;

public class SleepUtil {
    public static void sleepSeconds(int sec){
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
