package org.tizzer.pdfreader.entity;

import java.io.Serializable;

public class Prop implements Serializable {
    private String file;

    public Prop() {
    }

    public Prop(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
