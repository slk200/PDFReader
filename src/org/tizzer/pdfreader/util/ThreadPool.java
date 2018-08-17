package org.tizzer.pdfreader.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
    private static ExecutorService pool = Executors.newSingleThreadExecutor();
    private static Future future;

    public static void submit(Runnable task) {
        future = pool.submit(task);
    }

    public static void shutdown() {
        future.cancel(true);
    }

}
