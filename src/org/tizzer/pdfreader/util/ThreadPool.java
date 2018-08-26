package org.tizzer.pdfreader.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    //thread pool's max number of thread
    private static final int N_THREADS = 3;
    //create fixed thread
    private static ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);

    //submit task and execute
    public static void submit(Runnable task) {
        pool.submit(task);
    }

}
