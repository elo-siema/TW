package com.company;

public class WholeLockingList implements IList {

    private Node first;
    private int singleOperationTimeCost;

    public WholeLockingList(int singleOperationTimeCost) {
        this.singleOperationTimeCost = singleOperationTimeCost;
    }

    private void applyTimeCost(){
        try {
            Thread.sleep(singleOperationTimeCost);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean contains(Object o) {
        if (first == null) return false;

        //Search the list
        Node current = first;
        do {
            applyTimeCost();
            if (current.getValue() == o) {
                return true;
            }
            current = current.getNext();
        } while (current != null);
        return false;

    }

    @Override
    public synchronized boolean remove(Object o) {
        if(o == null) {
            return false;
        }

        //List empty? return
        if(first == null) {
            return false;
        }

        //First element should be removed?
        if(first.getValue() == o) {
            applyTimeCost();
            this.first = first.next;
            return true;
        }

        //search the rest
        Node previous = first;
        Node current = first.getNext();
        while(current != null) {
            applyTimeCost();
            if(current.getValue() == o) {
                previous.setNext(current.getNext());
                return true;
            }
            previous = current;
            current = current.getNext();
        }
        return false;
    }

    @Override
    public synchronized boolean add(Object o) {
        if(o == null) {
            return false;
        }

        Node newNode = new Node(o);

        //List empty? Insert as first element
        if(first == null) {
            first = newNode;
            return true;
        }

        //Search for the tail
        Node current = first;
        while(current.getNext() != null){
            current = current.getNext();
        }

        //We have the tail of the list, now add new element
        applyTimeCost();
        current.setNext(newNode);
        return true;
    }

    private class Node {
        private Object value;
        private Node next;

        public Node(Object value) {
            this.value = value;
        }

        public Object getValue(){
            return value;
        }

        public void setNext(Node nextNode){
            this.next = nextNode;
        }

        public Node getNext() {
            return next;
        }
    }
}
