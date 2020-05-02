package com.company;

import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        Counter cnt = new Counter(new Semafor(true));
        IThread it = new IThread(cnt);
        DThread dt = new DThread(cnt);

        //it.setPriority(10);
        it.start();
        dt.start();

        try {
            it.join();
            dt.join();
        } catch(InterruptedException ie) { }

        System.out.println("value=" + cnt.value());
    }
}
