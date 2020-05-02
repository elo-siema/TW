package com.company;
// Race2.java
// wyscig

class Counter {
    private int _val = 0;
    private Semafor _sem;

    public Counter(Semafor _sem) {
        this._sem = _sem;
    }

    public void inc(){
        _sem.P();
        ++_val;
        _sem.V();
    }
    public void dec(){
        _sem.P();
        --_val;
        _sem.V();
    }
    public int value() {
        return _val;
    }
}

class IThread extends Thread {
    private Counter _cnt;
    public IThread(Counter c) {
        _cnt = c;
    }
    public void run() {
        for (int i = 0; i < 1000000; ++i) {
            try {
                _cnt.inc();
            } catch (Exception ex) {

            }
        }
    }
}

class DThread extends Thread {
    private Counter _cnt;
    public DThread(Counter c) {
        _cnt = c;
    }
    public void run() {
        try {
            _cnt.dec();
        } catch (Exception ex) {

        }
    }
}
