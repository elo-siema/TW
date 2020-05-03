package com.company;

import java.util.Random;

class Producer extends Thread {
    private Buffer _buf;
    private Random generator;

    public Producer(Buffer buffer, Random generator) {
        this._buf = buffer;
        this.generator = generator;
    }

    public void run() {
        System.out.println("Started producing");
        int elementsToAdd = _buf.get_size();
        int maxBatchSize = elementsToAdd / 2;

        while(elementsToAdd > 0) {
            //generate between 1 and maxBatchSize
            var currentSize = generator.nextInt(maxBatchSize-1) + 1;
            if(currentSize > elementsToAdd) currentSize = elementsToAdd;
            int[] arr = new int[currentSize];
            for(int i = 0; i < currentSize; i++) {
                arr[i] = i;
            }
            _buf.insert(arr);
            elementsToAdd -= currentSize;
        }
        System.out.println("Finished producing");
    }
}