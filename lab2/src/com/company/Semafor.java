package com.company;

public class Semafor {
    private boolean _stan = true;
    //private int _czeka = 0;

    public Semafor(boolean initial) {
        _stan = initial;
    }

    public synchronized void P() {
        while (!_stan) {
            try{
                wait();
            } catch (InterruptedException ex) {

            }
        }
        _stan = false;
    }

    public synchronized void V() {
        _stan = true;
        notify();
    }
}

