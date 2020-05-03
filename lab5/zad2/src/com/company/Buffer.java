package com.company;


import java.util.Arrays;

public class Buffer {
    private int _size;
    private int[] content;
    private int readHead = 0;
    private Integer writeHead = 0;
    private Object writeLock;

    public int get_size() {
        return _size;
    }

    public Buffer(int size) {
        this._size = size;
        content = new int[size];
        writeLock = new Object();
    }

    //Used by producer
    public void insert(int elems[]) {
        int i = 0;
        //take writeLock in order to ensure write is atomic
        //ie. writeHead actually corresponds to
        //the number of elems in array
        synchronized (writeLock) {
            while (writeHead < _size && i < elems.length)
            {
                content[writeHead] = elems[i];
                i++;
                writeHead++;
            }

            //notify readers waiting for new elements
            writeLock.notifyAll();
        }
    }

    //Used by consumer
    public int[] retrieve(int requestedSize) {
        int truncatedRequestedSize =
                requestedSize + readHead > _size ? //requested more than in buffer?
                        _size - readHead : // TRUE: return remaining;
                        requestedSize; // FALSE : return requested;

        synchronized (writeLock) {
            while(truncatedRequestedSize + readHead > writeHead) {
                try {
                    writeLock.wait(); //wait until enough elements are in the buffer
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //enough elements in buffer, return:
        var result = Arrays.copyOfRange(content, readHead, readHead + truncatedRequestedSize);
        readHead += truncatedRequestedSize;
        return result;
    }
}