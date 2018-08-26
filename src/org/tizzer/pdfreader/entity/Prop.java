package org.tizzer.pdfreader.entity;

import org.tizzer.pdfreader.view.Theme;

import java.io.Serializable;

public class Prop implements Serializable {
    private String file;
    private Theme theme;

    public Prop() {
    }

    public Prop(String file, Theme theme) {
        this.file = file;
        this.theme = theme;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
