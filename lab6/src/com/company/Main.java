package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //GranularLockingList list =
        int cost = 50;
        IList list = new GranularLockingList(cost);
        System.out.println(list.remove(9)); //false
        System.out.println(list.contains(7)); //false

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        System.out.println(list.remove(9)); //false
        System.out.println(list.remove(3)); //true

        list.print();

        System.out.println(list.contains(2)); //true
        System.out.println(list.contains(7)); //false
    }
}
