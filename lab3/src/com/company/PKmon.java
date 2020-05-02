package com.company;// PKmon.java

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer extends Thread {
    private Buffer _buf;

    public Producer(Buffer _buf) {
        this._buf = _buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            _buf.put(i);/*
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}

class Consumer extends Thread {
    private Buffer _buf;

    public Consumer(Buffer _buf) {
        this._buf = _buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            System.out.println(_buf.get());
            /*try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}
interface Buffer {
    void put(int i);
    int get();
}

class BufferCond implements Buffer {
    private Queue<Integer> q;
    private int size = 10;

    public BufferCond(int size) {
        this.q = new LinkedList<Integer>();
        this.size = size;
    }

    public void put(int i)  {
        synchronized (this) {
            while (q.size() >= size) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            q.add(i);
            notifyAll();
        }
    }

    public int get()  {
        synchronized (this) {
            while (q.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int value = q.poll();
            notifyAll();
            return value;
        }
    }
}

class BufferSem implements Buffer  {
    private Queue<Integer> q;
    private int size = 10;
    private Semafor fillCount;
    private Semafor emptyCount;

    public BufferSem(int size) {
        this.q = new LinkedList<Integer>();
        this.size = size;
        fillCount = new Semafor(0);
        emptyCount = new Semafor(size);
    }

    public void put(int i)  {
        emptyCount.P();
        q.add(i);
        fillCount.V();
    }

    public int get()  {
        fillCount.P();
        int value;
        try {
            value = q.poll();
        } catch (Exception ex) {
            value = -1;
        }
        emptyCount.V();
        return value;
    }
}