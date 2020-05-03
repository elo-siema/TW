package com.company;

import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        var buffer = new Buffer(100);
        var generator = new Random();

        var pThread = new Producer(buffer, generator);
        var cThread = new Consumer(buffer, generator);

        pThread.start();
        cThread.start();

        try {
            pThread.join();
            cThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

