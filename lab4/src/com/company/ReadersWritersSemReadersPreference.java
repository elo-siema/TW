package com.company;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

class ReadersWritersSemReadersPreference extends AbstractReadersWriters
{
    private int readCount = 0;
    private Semaphore resource = new Semaphore(1);
    private Semaphore rmutex = new Semaphore(1);

    public ReadersWritersSemReadersPreference(int repeat, int sleep) {
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
            rmutex.acquire();
            readCount++;
            if(readCount == 1) {
                resource.acquire();
            }
            rmutex.release();
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
            resource.acquire();
        }

        @Override
        void after() throws InterruptedException {
            resource.release();
        }
    }
}