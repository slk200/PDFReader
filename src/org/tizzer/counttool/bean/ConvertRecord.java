package org.tizzer.counttool.bean;

/**
 * 转换历史记录
 */
public class ConvertRecord {

    private final String time;
    private final String fileName;
    private final String filePath;
    private final String status;

    public ConvertRecord(String time, String fileName, String filePath, String status) {
        this.time = time;
        this.fileName = fileName;
        this.filePath = filePath;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getStatus() {
        return status;
    }
}
