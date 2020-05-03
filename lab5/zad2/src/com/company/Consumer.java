package com.company;

import java.util.Arrays;
import java.util.Random;

class Consumer extends Thread {
    private Buffer _buf;
    private Random generator;

    public Consumer(Buffer buffer, Random generator) {
        this._buf = buffer;
        this.generator = generator;
    }

    public void run() {
        System.out.println("Started consuming");
        int elementsToConsume = _buf.get_size();
        int maxBatchSize = elementsToConsume / 2;

        while(elementsToConsume > 0) {
            //generate between 1 and maxBatchSize
            var currentSize = generator.nextInt(maxBatchSize-1) + 1;
            if(currentSize > elementsToConsume) currentSize = elementsToConsume;
            System.out.println(Arrays.toString(_buf.retrieve(currentSize)));
            elementsToConsume -= currentSize;
        }
        System.out.println("Finished consuming");
    }
}