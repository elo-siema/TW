package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

abstract class AbstractReadersWriters
{
    protected int repeat;
    protected int sleep;

    public AbstractReadersWriters(int repeat, int sleep) {
        this.repeat = repeat;
        this.sleep = sleep;
    }


    void start(int readers, int writers)
    {
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= readers; i++) {
            Thread t = new Thread(createReader());
            t.setName(String.format("#%03d", i));
            threads.add(t);
        }
        for (int i = 1; i <= writers; i++) {
            Thread t = new Thread(createWriter());
            t.setName(String.format("#%03d", i));
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join(); // wait for all to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    abstract Reader createReader();

    abstract Writer createWriter();

    abstract class Reader implements Runnable
    {
        @Override
        public void run()
        {
            for (int i = 0; i < repeat; i++) {
                try {
                    before();
                    read();
                    after();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        void read() throws InterruptedException
        {
            Thread.sleep(sleep);
        }

        abstract void before() throws InterruptedException;

        abstract void after() throws InterruptedException;
    }

    abstract class Writer implements Runnable
    {
        @Override
        public void run()
        {
            for (int i = 0; i < repeat; i++) {
                try {
                    before();
                    write();
                    after();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        void write() throws InterruptedException
        {
            Thread.sleep(sleep);
        }

        abstract void before() throws InterruptedException;

        abstract void after() throws InterruptedException;
    }
}