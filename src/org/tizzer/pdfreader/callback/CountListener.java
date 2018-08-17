package org.tizzer.pdfreader.callback;

public interface CountListener {
    void countWord(int num);

    void countPpt(int num);

    void countExcel(int num);

    void countPDF(int num);

    void countPages(String filename, int page, int num);
}
