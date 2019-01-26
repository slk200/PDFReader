package org.tizzer.counttool.bean;

public class ExtraItem {
    private String extra;
    private String price;
    private int num;

    public ExtraItem() {
    }

    public ExtraItem(String extra, String price, int num) {
        this.extra = extra;
        this.price = price;
        this.num = num;
    }

    public String getExtra() {
        return extra;
    }

    public String getPrice() {
        return price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
