package com.company;



import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;



public class Main {
    public static void main(String[] args) {

        int repeat = 100;
        int sleep = 1;

        Map<String, AbstractReadersWriters> solutions = new HashMap<>();
        solutions.put("RWSRP", new ReadersWritersSemReadersPreference(repeat, sleep));
        solutions.put("RWSWP", new ReadersWritersSemWritersPreference(repeat, sleep));
        solutions.put("RWSQ", new ReadersWritersSemQueue(repeat, sleep));
        solutions.put("RWC", new ReadersWritersCond(repeat, sleep));
        try {
            for(int nWriters = 1; nWriters <= 10; nWriters++){
                FileWriter myWriter = null;
                myWriter = new FileWriter(String.format("%d_writers.txt", nWriters));
                myWriter.write(String.format(
                        "%s %s %s %s %s\n",
                        "nReaders",
                        "RWSRP",
                        "RWSWP",
                        "RWSQ",
                        "RWC"
                ));

                for(int nReaders = 10; nReaders <= 100; nReaders+=10){
                    Map<String, Long> results = new HashMap<>();

                    int finalNReaders = nReaders;
                    int finalNWriters = nWriters;
                    solutions.forEach((name, solution) -> {

                        long start = System.currentTimeMillis();
                        solution.start(finalNReaders, finalNWriters);
                        long stop = System.currentTimeMillis();
                        results.put(name, stop - start);
                        System.out.printf("%s\t%d\t%d\t%d\n", name, finalNWriters, finalNReaders, stop - start);
                    });
                    myWriter.write(String.format(
                            "%d %d %d %d %d\n",
                            nReaders,
                            results.get("RWSRP"),
                            results.get("RWSWP"),
                            results.get("RWSQ"),
                            results.get("RWC")
                    ));
                }
                myWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
