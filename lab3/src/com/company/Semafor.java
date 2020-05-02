package com.company;

public class Semafor {
    private int _liczba = 0;

    public Semafor(int initial) {
        _liczba = initial;
    }

    public synchronized void P() {
        while (_liczba == 0) {
            try{
                wait();
            } catch (InterruptedException ex) {

            }
        }
        _liczba--;
    }

    public synchronized void V() {
        _liczba++;
        notify();
    }
}

