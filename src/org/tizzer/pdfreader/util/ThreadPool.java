package org.tizzer.pdfreader.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static final int N_THREADS = 3;
    private static ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);

    public static void submit(Runnable task) {
        pool.submit(task);
    }

}
