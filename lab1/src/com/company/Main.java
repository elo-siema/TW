package com.company;

import java.io.FileWriter;

public class Main {

    public static void main(String[] args) {
        try{
            FileWriter output = new FileWriter("out.txt");
            for(int i = 0; i < 10000; i++) {
                output.write(run() + "\r\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String run() throws InterruptedException{
        IntHolder holder = new IntHolder(0);

        Thread incrementer = new Thread(new IncrementRunnable(1, 1000000, holder));
        Thread decrementer = new Thread(new IncrementRunnable(-1, 1000000, holder));

        incrementer.start();
        decrementer.start();

        incrementer.join();
        decrementer.join();

        return holder.toString();
    }
}
