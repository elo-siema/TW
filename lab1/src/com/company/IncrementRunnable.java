package com.company;

public class IncrementRunnable implements Runnable{
    int delta;
    int reps;
    IntHolder holder;

    public IncrementRunnable(int delta, int reps, IntHolder holder) {
        this.delta = delta;
        this.reps = reps;
        this.holder = holder;
    }

    @Override
    public void run() {
        for(int i = 0; i < reps; i++){
            holder.value += delta;
        }
    }
}
