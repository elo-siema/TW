package com.company;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
/*

public class Main {
    public static void main(String[] args) {
        Buffer b = new Buffer(10);
        Producer it = new Producer(b);
        Consumer dt = new Consumer(b);

        //it.setPriority(10);
        it.start();
        dt.start();

        try {
            it.join();
            dt.join();
        } catch(InterruptedException ie) { }
    }
}
*/

public class Main {
    public static void main(String[] args) {
        Buffer b = new BufferSem(10);

        int n1 = 3;
        int n2 = 3;

        Producer[] ap = new Producer[n1];
        Consumer[] ac = new Consumer[n2];

        Arrays.stream(ap).forEach(p -> {
            p=new Producer(b);
            p.start();
        });
        Arrays.stream(ac).forEach(c -> {
            c=new Consumer(b);
            c.start();
        });

        Arrays.stream(ap).forEach(p -> {
            try {
                p.join();
            } catch(Exception ie) { }
        });
        Arrays.stream(ac).forEach(c -> {
            try {
                c.join();
            } catch(Exception ie) { }
        });
    }
}
