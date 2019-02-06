package org.tizzer.counttool.util;

import com.lowagie.text.pdf.PdfReader;
import org.tizzer.counttool.constant.FileType;

import java.io.File;
import java.io.IOException;

/**
 * Created by tizzer on 2019/1/21.
 */
public class PDFCountHandler {

    private int page;
    private int totalPage;

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 计算pdf总页数
     *
     * @param file
     */
    public void count(File file) {
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(file.getAbsolutePath());
            page = pdfReader.getNumberOfPages();
            totalPage += page;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }
        }
    }

    /**
     * 转换office为pdf
     *
     * @param inFile
     * @param fileType
     */
    public void office2pdf(String inFile, FileType fileType) {
        File file = new File(inFile);
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf('.');
        String filenameWithoutSuffix = fileName.substring(0, lastIndex);
        String outFile = file.getParent() + File.separator + filenameWithoutSuffix + ".pdf";

        //因为以下为耗时操作，且不再使用这些变量，所以赋值null加速资源的回收
        filenameWithoutSuffix = null;
        fileName = null;
        file = null;

        //先判断是不是有重名文件文件存在了，否则不转换也不计数
        File TransedFile = new File(outFile);
        if (!TransedFile.exists()) {
            switch (fileType) {
                case WORD:
                    Office2PDFHandler.word2pdf(inFile, outFile);
                    break;
                case PPT:
                    Office2PDFHandler.ppt2pdf(inFile, outFile);
                    break;
                case EXCEL:
                    Office2PDFHandler.excel2pdf(inFile, outFile);
                    break;
                default:
            }
            count(TransedFile);
        }
    }
}
