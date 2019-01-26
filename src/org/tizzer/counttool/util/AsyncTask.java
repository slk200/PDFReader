package org.tizzer.counttool.util;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.tizzer.counttool.constant.Signal;

/**
 * Created by tizzer on 2019/01/22.
 */
public abstract class AsyncTask<V> extends Task<V> {

    private Signal signal;

    protected void sendSignal(Signal signal) {
        this.signal = signal;
        Platform.runLater(this::doInFXThread);
    }

    protected Signal getSignal() {
        return signal;
    }

    public void doInFXThread() {
    }

}
