package com.company;

import java.util.Random;

public class ListPerformanceTester {
    public void Test(int threadCount, int elemCount, IList list, int cost){
        var randomAddTime = randomAdd(threadCount,elemCount,list);
        //System.out.println("Adding finished");
        //list.print();
        var randomContainsTime = randomContains(threadCount,elemCount,list);
        //System.out.println("Contains finished");
        //list.print();
        var randomRemoveTime = randomRemove(threadCount,elemCount,list);
        //System.out.println("Removing finished");
        System.out.println(
                "list: "+
                        list.getClass().getSimpleName() +
                        ", elements: "+
                        elemCount+
                        ", threadCount: "+
                        threadCount +
                        ", cost[ms]: "+
                        cost +
                        ", randomAdd[ms]: " +
                        randomAddTime +
                        ", randomContains[ms]: " +
                        randomContainsTime +
                        ", randomRemove[ms]: " +
                        randomRemoveTime
        );
    }

    private long randomAdd(int threadCount, int elemCount, IList list){
        Random random = new Random();
        //setup worker threads
        Thread[] threads = new Thread[threadCount];

        //random add
        var beforeAdd = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for(int j = 0; j < elemCount/threadCount; j++) {
                    list.add(random.nextInt(elemCount));
                }
            });
        }
        for(var t : threads) t.start();
        for(var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        var afterAdd = System.currentTimeMillis();
        return afterAdd-beforeAdd;
    }
    private long randomContains(int threadCount, int elemCount, IList list){
        Random random = new Random();
        //setup worker threads
        Thread[] threads = new Thread[threadCount];

        var before = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for(int j = 0; j < elemCount/threadCount; j++) {
                    list.contains(random.nextInt(elemCount)); //50% chance the list contains
                }
            });
        }
        for(var t : threads) t.start();
        for(var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        var after = System.currentTimeMillis();
        return after-before;
    }
    private long randomRemove(int threadCount, int elemCount, IList list){
        Random random = new Random();
        //setup worker threads
        Thread[] threads = new Thread[threadCount];

        var before = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for(int j = 0; j < elemCount/threadCount; j++) {
                    list.remove(random.nextInt(elemCount));
                }
            });
        }
        for(var t : threads) t.start();
        for(var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        var after = System.currentTimeMillis();
        return after-before;
    }
}
