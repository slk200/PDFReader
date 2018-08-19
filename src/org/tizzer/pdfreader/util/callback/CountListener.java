package org.tizzer.pdfreader.util.callback;

public interface CountListener {
    void countWORD(int num);

    void countPPT(int num);

    void countExcel(int num);

    void countPDF(int num);

    void countPages(String filename, int page, int num);
}
