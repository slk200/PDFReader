package org.tizzer.counttool.util;

import org.tizzer.counttool.bean.PendedFile;
import org.tizzer.counttool.constant.FileType;
import org.tizzer.counttool.constant.Signal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tizzer on 2019/1/21.
 */
public class FileCountHandler {
    //counter
    private int wordNum;
    private int excelNum;
    private int pptNum;
    private int pdfNum;
    private int picNum;
    private int otherNum;
    //dataset
    private List<PendedFile> pendedFiles;

    public FileCountHandler() {
        pendedFiles = new ArrayList<>();
    }

    public int getWordNum() {
        return wordNum;
    }

    public int getExcelNum() {
        return excelNum;
    }

    public int getPptNum() {
        return pptNum;
    }

    public int getPdfNum() {
        return pdfNum;
    }

    public int getPicNum() {
        return picNum;
    }

    public int getOtherNum() {
        return otherNum;
    }

    public List<PendedFile> getPendedFiles() {
        return pendedFiles;
    }

    /**
     * 1.递归查找pdf文件
     * 2.计算office、pdf文件数
     * 3.转换office为pdf文件
     * 4.计算pdf文件总页数
     *
     * @param parent
     */
    public void process(File parent) {
        if (parent.isDirectory()) {
            File[] files = parent.listFiles();
            if (files != null) {
                for (File file : files) {
                    process(file);
                }
            }
        } else {
            String filenameWithSuffix = parent.getName().toLowerCase();
            //去掉隐藏文件以及office的缓存文件（一般以~$开头）
            if (!parent.isHidden() || !filenameWithSuffix.startsWith("~$")) {
                int dotIndex = filenameWithSuffix.lastIndexOf(".");
                String suffix = filenameWithSuffix.substring(dotIndex + 1);
                switch (suffix) {
                    case "pdf":
                        pdfNum++;
                        pendedFiles.add(new PendedFile(filenameWithSuffix, Signal.ONLYCOUNT,
                                "等待计数", parent.getAbsolutePath(), FileType.PDF));
                        break;
                    case "doc":
                    case "docx":
                    case "wps":
                    case "dot":
                    case "wpt":
                        wordNum++;
                        pendedFiles.add(new PendedFile(filenameWithSuffix, Signal.READY,
                                "等待计数", parent.getAbsolutePath(), FileType.WORD));
                        break;
                    case "jpg":
                    case "png":
                    case "bmp":
                    case "gif":
                    case "jpeg":
                        picNum++;
                        pendedFiles.add(new PendedFile(filenameWithSuffix, Signal.NO_NEED,
                                "/", parent.getAbsolutePath(), FileType.PIC));
                        break;
                    case "ppt":
                    case "pptx":
                    case "dps":
                    case "dpt":
                    case "pot":
                    case "pps":
                        pptNum++;
                        pendedFiles.add(new PendedFile(filenameWithSuffix, Signal.READY,
                                "等待计数", parent.getAbsolutePath(), FileType.PPT));
                        break;
                    case "xls":
                    case "xlsx":
                    case "csv":
                    case "xlt":
                    case "et":
                    case "ett":
                        excelNum++;
                        pendedFiles.add(new PendedFile(filenameWithSuffix, Signal.READY,
                                "等待计数", parent.getAbsolutePath(), FileType.EXCEL));
                        break;
                    default:
                        otherNum++;
                        pendedFiles.add(new PendedFile(filenameWithSuffix, Signal.NO_NEED,
                                "/", parent.getAbsolutePath(), FileType.OTHER));
                }
            }
        }
    }

}
