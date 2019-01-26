package org.tizzer.counttool.bean;

import javafx.collections.ObservableList;
import org.tizzer.counttool.constant.ChangeType;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tizzer on 2019/1/21.
 */
public class Define implements Serializable {
    private File defaultDirectory;
    private List<Extra> extras;
    private ChangeType changeType;

    public Define() {
        extras = new ArrayList<>();
        this.changeType = ChangeType.NONE;
    }

    public File getDefaultDirectory() {
        return defaultDirectory;
    }

    public void setDefaultDirectory(File defaultDirectory) {
        this.defaultDirectory = defaultDirectory;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public void setExtra(ObservableList<Extra> items) {
        extras.clear();
        extras.addAll(items);
    }

    @Override
    public String toString() {
        return "Define{" +
                "defaultDirectory=" + defaultDirectory +
                ", extras=" + extras +
                ", changeType=" + changeType +
                '}';
    }
}
