package com.company;

import java.util.concurrent.locks.ReentrantLock;

public class GranularLockingList implements IList {
    private Node first;
    private int singleOperationTimeCost;
    private ReentrantLock firstElementLock;

    public GranularLockingList(int singleOperationTimeCost) {
        this.singleOperationTimeCost = singleOperationTimeCost;
        firstElementLock = new ReentrantLock();
    }

    private void applyTimeCost(){
        try {
            Thread.sleep(singleOperationTimeCost);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(Object o) {
        //handle case of empty list
        if(first == null) return false;

        //Search the list
        Node current = first;
        current.lock();
        do {
            applyTimeCost();
            if(current.getValue() == o) {
                current.unlock();
                return true;
            }
            if(current.getNext() != null) current.getNext().lock();
            Node temp = current;
            current = current.getNext();
            temp.unlock();
        } while(current != null);

        return false;
    }

    public boolean remove(Object o) {
        Node previous = null, current = null;
        if(o == null) return false;

        //lock access to first element to prevent other threads
        //from removing the head
        synchronized (this) {

            //List empty? return
            if (first == null) {
                //System.out.println("List empty: " + o);
                return false;
            }

            //First element should be removed?
            first.lock();
            if (first.getValue() == o) {
                //System.out.println("Removing first element: " + o);
                applyTimeCost();
                var temp = first;
                this.first = temp.getNext();
                temp.unlock();
                return true;
            }

            //list 1-element long?
            if (first.getNext() == null) {
                first.unlock();
                //System.out.println("List is 1-element long: " + o);
                return false;
            }

            //2 locks acquired, go with the rest
            previous = first;
            current = first.getNext();

            //end lock on first element
        }

        while(current != null) {
            current.lock();
            applyTimeCost();
            if(current.getValue() == o) {
                //System.out.println("Removing nth element: " + o);
                previous.setNext(current.getNext());
                previous.unlock();
                current.unlock();
                return true;
            }
            previous.unlock();
            previous = current;
            current = current.getNext();
        }
        previous.unlock();

        return false;
    }

    public boolean add(Object o) {
        if(o == null) return false;

        Node newNode = new Node(o);

        //List empty? Insert as first element
        if(first == null) {
            first = newNode;
            return true;
        }

        //Search for the tail
        Node current = first;
        current.lock();
        while(current.getNext() != null){
            if(current.getNext() != null) current.getNext().lock();
            Node temp = current;
            current = current.getNext();
            temp.unlock();
        }
        //We have the tail of the list, now add new element
        applyTimeCost();
        current.setNext(newNode);
        //Release lock on the old tail
        current.unlock();

        return true;
    }

    private class Node {
        private Object value;
        private Node next;
        private ReentrantLock lock;

        public Node(Object value) {
            this.value = value;
            this.lock = new ReentrantLock();
        }

        public void lock(){
            this.lock.lock();
        }

        public void unlock(){
            this.lock.unlock();
        }

        public Object getValue(){
            return value;
        }

        public void setNext(Node nextNode){
            this.next = nextNode;
        }

        public Node getNext(){
            return next;
        }
    }
}
