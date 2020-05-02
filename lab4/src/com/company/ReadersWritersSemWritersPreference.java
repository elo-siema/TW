package com.company;

import java.util.concurrent.Semaphore;

class ReadersWritersSemWritersPreference extends AbstractReadersWriters
{
    private int readCount = 0;
    private int writeCount = 0;
    private Semaphore resource = new Semaphore(1);
    private Semaphore rmutex = new Semaphore(1);
    private Semaphore wmutex = new Semaphore(1);
    private Semaphore readTry = new Semaphore(1);

    public ReadersWritersSemWritersPreference(int repeat, int sleep) {
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
            readTry.acquire();
            rmutex.acquire();
            readCount++;
            if(readCount == 1) {
                resource.acquire();
            }
            rmutex.release();
            readTry.release();
        }

        @Override
        void after() throws InterruptedException {
            rmutex.acquire();
            readCount--;
            if(readCount == 0){
                resource.release();
            }
            rmutex.release();
        }
    }

    class WriterSem extends Writer {

        @Override
        void before() throws InterruptedException {
            wmutex.acquire();
            writeCount++;
            if(writeCount == 1) {
                readTry.acquire();
            }
            wmutex.release();
            resource.acquire();
        }

        @Override
        void after() throws InterruptedException {
            resource.release();
            wmutex.acquire();
            writeCount--;
            if(writeCount == 0) {
                readTry.release();
            }
            wmutex.release();
        }
    }
}