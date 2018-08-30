package org.tizzer.pdfreader.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.tizzer.pdfreader.util.callback.CountListener;
import org.tizzer.pdfreader.util.callback.LogListener;
import org.tizzer.pdfreader.util.callback.ProcessListener;

import java.io.File;
import java.io.IOException;

public class WPS2PDFHandler {
    //counter
    private int WORD_NUM = 0;
    private int PPT_NUM = 0;
    private int EXCEL_NUM = 0;
    private int PDF_NUM = 0;
    private int PAGE_NUM = 0;
    //listener
    private LogListener logListener;
    private CountListener countListener;
    private ProcessListener processListener;
    //signal of start
    private boolean signal;

    public boolean isStarted() {
        return signal;
    }

    public void start() {
        signal = true;
    }

    /**
     * console listener
     *
     * @param logListener
     */
    public void addLogListener(LogListener logListener) {
        this.logListener = logListener;
    }

    /**
     * counter listener
     *
     * @param countListener
     */
    public void addCountListener(CountListener countListener) {
        this.countListener = countListener;
    }

    /**
     * unsupported listener
     *
     * @param processListener
     */
    public void addProcessListener(ProcessListener processListener) {
        this.processListener = processListener;
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
            if (parent.isHidden() || !filenameWithSuffix.startsWith("~$")) {
                int dotIndex = filenameWithSuffix.lastIndexOf(".");
                String suffix = filenameWithSuffix.substring(dotIndex + 1);
                if (!suffix.equals("pdf")) {
                    String filename = filenameWithSuffix.substring(0, dotIndex + 1);
                    String pdfFilePath = parent.getParent() + File.separator + filename + "pdf";
                    switch (suffix) {
                        case "doc":
                        case "docx":
                        case "wps":
                        case "dot":
                        case "wpt":
                            WORD_NUM++;
                            countListener.countWORD(WORD_NUM);
                            office2pdf(pdfFilePath, parent, "word");
                            break;
                        case "ppt":
                        case "pptx":
                        case "dps":
                        case "dpt":
                        case "pot":
                        case "pps":
                            PPT_NUM++;
                            countListener.countPPT(PPT_NUM);
                            office2pdf(pdfFilePath, parent, "ppt");
                            break;
                        case "xls":
                        case "xlsx":
                        case "xlt":
                        case "et":
                        case "ett":
                            EXCEL_NUM++;
                            countListener.countExcel(EXCEL_NUM);
                            office2pdf(pdfFilePath, parent, "excel");
                            break;
                        default:
                            processListener.hasUnsupportedFile(parent.getParent());
                    }
                } else {
                    PDF_NUM++;
                    countListener.countPDF(PDF_NUM);
                    logListener.log("无需转换" + "\n");
                    count(parent);
                }
            }
        }
    }

    /**
     * 计算pdf总页数
     *
     * @param file
     */
    private void count(File file) {
        logListener.log("开始计数\n");
        logListener.log(file.getAbsolutePath() + "\n");
        try {
            PDDocument pdfReader = PDDocument.load(file);
            int page = pdfReader.getNumberOfPages();
            PAGE_NUM += page;
            countListener.countPages(file.getName(), page, PAGE_NUM);
            pdfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logListener.log("结束计数\n\n");
    }

    /**
     * 转换office为pdf
     *
     * @param PDFFile
     * @param parentFile
     * @param fileType
     */
    private void office2pdf(String PDFFile, File parentFile, String fileType) {
        if (!new File(PDFFile).exists()) {
            logListener.log("开始转换\n");
            switch (fileType) {
                case "word":
                    logListener.log("转换word: " + parentFile.getAbsolutePath() + "\n");
                    WPS2PDFCore.word2pdf(parentFile.getAbsolutePath(), PDFFile);
                    break;
                case "ppt":
                    logListener.log("转换ppt: " + parentFile.getAbsolutePath() + "\n");
                    WPS2PDFCore.ppt2pdf(parentFile.getAbsolutePath(), PDFFile);
                    break;
                case "excel":
                    logListener.log("转换excel: " + parentFile.getAbsolutePath() + "\n");
                    WPS2PDFCore.excel2pdf(parentFile.getAbsolutePath(), PDFFile);
                    break;
                default:
            }
            logListener.log("结束转换\n");
            count(new File(PDFFile));
        }
    }

    /**
     * 1.重置文件列表
     * 2.重置计数器
     */
    public void reset() {
        WORD_NUM = 0;
        PPT_NUM = 0;
        EXCEL_NUM = 0;
        PDF_NUM = 0;
        PAGE_NUM = 0;
    }

}
