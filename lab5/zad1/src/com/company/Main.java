package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int bufferSize = 1000;
        Buffer buffer = new Buffer(bufferSize);
        int noOfThreads = 100;

        //create producer
        Thread tp = new Thread(new Producer(buffer, 0));
        tp.start();

        //create converters
        List<Thread> threadList = new ArrayList<>();
        for (int i = 1; i < noOfThreads - 1; i++) {
            Thread t = new Thread(new Converter(buffer, i));
            threadList.add(t);
            t.start();
        }

        //create consumer
        Thread tc = new Thread(new Consumer(buffer, noOfThreads - 1));
        tc.start();

        //join threads
        try {
            tp.join();
            for (Thread t : threadList) t.join();
            tc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("czas: " + time);
    }
}

class Buffer {
    private int _size;
    private Object[] bufferLocks;
    private String[] content;
    private AtomicIntegerArray currentAllowedUser; //id of current allowed thread

    public interface BufferModifier {
        String modify(String before);
    }

    public int get_size() {
        return _size;
    }

    public Buffer(int size) {
        this._size = size;

        bufferLocks = new Object[size];
        content = new String[size];
        currentAllowedUser = new AtomicIntegerArray(size);
        for (int i = 0; i < size; i++) {
            bufferLocks[i] = new Object();
        }
    }

    //Used by producer
    public void insert(int threadId, int i, String value) {
        synchronized (bufferLocks[i]){
            while (threadId != currentAllowedUser.get(i)) {
                try {
                    bufferLocks[i].wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            content[i] = value;
            currentAllowedUser.incrementAndGet(i);
            bufferLocks[i].notifyAll();
        }
    }

    //Made in order to keep control over locks within Buffer class
    public void modifyInPlace(int threadId, int i, BufferModifier lambda) {
        synchronized (bufferLocks[i]){
            while (threadId != currentAllowedUser.get(i)) {
                try {
                    bufferLocks[i].wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String previous = content[i];
            content[i] = lambda.modify(previous);
            currentAllowedUser.incrementAndGet(i);
            bufferLocks[i].notifyAll();
        }
    }

    //Used by consumer
    public String retrieve(int threadId, int i) {
        synchronized (bufferLocks[i]){
            while (threadId != currentAllowedUser.get(i)) {
                try {
                    bufferLocks[i].wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            var result = content[i];
            currentAllowedUser.set(i, 0);
            bufferLocks[i].notifyAll();
            return result;
        }
    }
}

class Consumer extends Thread {
    private Buffer _buf;
    private int id;

    public Consumer(Buffer buffer, int id) {
        this._buf = buffer;
        this.id = id;
    }

    public void run() {
        for (int i = 0; i < _buf.get_size(); i++) {
            System.out.println(_buf.retrieve(id, i));
        }
    }
}

class Converter extends Thread {
    private Buffer _buf;
    private int id;

    public Converter(Buffer buffer, int id) {
        this._buf = buffer;
        this.id = id;
    }

    public void run() {
        for (int i = 0; i < _buf.get_size(); i++) {
            _buf.modifyInPlace(id, i, (str) -> {
                return str + "thr" + id;
            });
        }
    }
}

class Producer extends Thread {
    private Buffer _buf;
    private int id;

    public Producer(Buffer buffer, int id) {
        this._buf = buffer;
        this.id = id;
    }

    public void run() {
        for (int i = 0; i < _buf.get_size(); i++) {
            _buf.insert(id, i, "ind" + i);
        }
    }
}
