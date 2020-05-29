package com.company;

import java.util.concurrent.locks.ReentrantLock;

public interface IList {
    public abstract boolean contains(Object o);
    public abstract boolean remove(Object o);
    public abstract boolean add(Object o);
}
