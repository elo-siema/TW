package com.company;

import java.util.concurrent.Semaphore;

class ReadersWritersSemQueue extends AbstractReadersWriters
{
    private int readCount = 0;
    private Semaphore resourceAccess = new Semaphore(1);
    private Semaphore readCountAccess = new Semaphore(1);
    private Semaphore serviceQueue = new Semaphore(1);

    public ReadersWritersSemQueue(int repeat, int sleep) {
        super(repeat, sleep);
    }

    Reader createReader(){
        return new ReaderSem();
    }
    Writer createWriter(){
        return new WriterSem();
    }

    class ReaderSem extends Reader {

        @Override
        void before() throws InterruptedException {
            serviceQueue.acquire();           // wait in line to be serviced
            readCountAccess.acquire();        // request exclusive access to readCount
            // <ENTER>
            if (readCount == 0)         // if there are no readers already reading:
                resourceAccess.acquire();     // request resource access for readers (writers blocked)
            readCount++;                // update count of active readers
            // </ENTER>
            serviceQueue.release();           // let next in line be serviced
            readCountAccess.release();        // release access to readCount
        }

        @Override
        void after() throws InterruptedException {
            readCountAccess.acquire();        // request exclusive access to readCount
            // <EXIT>
            readCount--;                // update count of active readers
            if (readCount == 0)         // if there are no readers left:
                resourceAccess.release();     // release resource access for all
            // </EXIT>
            readCountAccess.release();        // release access to readCount
        }
    }

    class WriterSem extends Writer {

        @Override
        void before() throws InterruptedException {
            serviceQueue.acquire();           // wait in line to be serviced
            // <ENTER>
            resourceAccess.acquire();         // request exclusive access to resource
            // </ENTER>
            serviceQueue.release();           // let next in line be serviced
        }

        @Override
        void after() throws InterruptedException {
            resourceAccess.release();
        }
    }
}