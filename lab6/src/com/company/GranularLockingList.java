package com.company;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MyList implements IList {
    private Node first;

    public void print(){
        Node current = first;
        while(current != null){
            current.printValue();
            current = current.next;
        }
    }

    public boolean contains(Object o){
        try {
            //handle case of empty list
            if(first == null) return false;

            //Search the list
            Node current = first;
            current.lock();
            do {
                if(current.getValue() == o) {
                    current.unlock();
                    return true;
                }
                if(current.getNext() != null) current.getNext().lock();
                Node temp = current;
                current = current.getNext();
                temp.unlock();
            } while(current != null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean remove(Object o){
        try {
            if(o == null) return false;

            //List empty? return
            if(first == null) {
                return false;
            }

            //First element should be removed?
            first.lock();
            if(first.getValue() == o) {
                this.first = first.next;
                return true;
            }

            //search the rest
            Node previous = first;
            Node current = first.getNext();
            previous.lock();
            while(current != null) {
                current.lock();
                if(current.getValue() == o) {
                    previous.setNext(current.getNext());
                    previous.unlock();
                    current.unlock();
                    return true;
                }
                previous.unlock();
                previous = current;
                current = current.getNext();
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean add(Object o){
        try {
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
            current.setNext(newNode);
            //Release lock on the old tail
            current.unlock();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
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

        public Object getValue() throws IllegalAccessException {
            if(this.lock.isHeldByCurrentThread()){
                return value;
            }
            else throw new IllegalAccessException("Lock the node first");
        }

        public void setNext(Node nextNode) throws IllegalAccessException {
            if(this.lock.isHeldByCurrentThread()){
                this.next = nextNode;
            }
            else throw new IllegalAccessException("Lock the node first");
        }

        public Node getNext() throws IllegalAccessException {
            if(this.lock.isHeldByCurrentThread()){
                return next;
            }
            else throw new IllegalAccessException("Lock the node first");
        }

        public void printValue(){
            System.out.println(value);
        }
    }
}
