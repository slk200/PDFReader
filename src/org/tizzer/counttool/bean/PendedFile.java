package org.tizzer.counttool.bean;

import org.tizzer.counttool.constant.FileType;
import org.tizzer.counttool.constant.Signal;

/**
 * Created by tizzer on 2019/1/21.
 */
public class PendedFile {
    private String name;
    private Signal state;
    private String page;
    private String path;
    private FileType fileType;

    public PendedFile() {
    }

    public PendedFile(String name, Signal state, String page, String path, FileType fileType) {
        this.name = name;
        this.state = state;
        this.page = page;
        this.path = path;
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public Signal getState() {
        return state;
    }

    public void setState(Signal state) {
        this.state = state;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPath() {
        return path;
    }

    public FileType getFileType() {
        return fileType;
    }
}
