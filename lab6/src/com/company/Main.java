package com.company;

public class Main {

    public static void main(String[] args) {

        int elemCount = 200;
        var tester = new ListPerformanceTester();
        for(int cost = 0; cost <= 5; cost++) {
            for (int threads = 10; threads <= 100; threads += 10) {
                tester.Test(threads, elemCount, new GranularLockingList(cost), cost);
                tester.Test(threads, elemCount, new WholeLockingList(cost), cost);
            }
        }
    }
}
