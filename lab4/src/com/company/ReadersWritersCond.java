package com.company;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReadersWritersCond extends AbstractReadersWriters
{
    private final Lock m = new ReentrantLock();
    private final Condition turn = m.newCondition();
    private int writers = 0;
    private int writing = 0;
    private int reading = 0;

    public ReadersWritersCond(int repeat, int sleep) {
        super(repeat, sleep);
    }

    Reader createReader(){
        return new ReaderCond();
    }

    Writer createWriter(){
        return new WriterCond();
    }

    class ReaderCond extends Reader {

        @Override
        void before() throws InterruptedException {
            m.lock();
            while (0 < writers) {
                turn.await();
            }
            reading++;
            m.unlock();
        }

        @Override
        void after() throws InterruptedException {
            m.lock();
            reading--;
            turn.signalAll();
            m.unlock();
        }
    }

    class WriterCond extends Writer {

        @Override
        void before() throws InterruptedException {
            m.lock();
            writers++;
            while (0 < reading || 0 < writing) {
                turn.await();
            }
            writing++;
            m.unlock();
        }

        @Override
        void after() throws InterruptedException {
            m.lock();
            writing--;
            writers--;
            turn.signalAll();
            m.unlock();
        }
    }
}