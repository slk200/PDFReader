package org.tizzer.counttool.bean;

/**
 * Created by tizzer on 2019/1/21.
 */
public class SumRecord {
    private Object page;
    private Object price;
    private Object num;
    private Object spec;
    private Object sum;

    public SumRecord() {
    }

    public SumRecord(Object page, Object price, Object num, Object spec, Object sum) {
        this.page = page;
        this.price = price;
        this.num = num;
        this.spec = spec;
        this.sum = sum;
    }

    public Object getPage() {
        return page;
    }

    public Object getPrice() {
        return price;
    }

    public Object getNum() {
        return num;
    }

    public Object getSpec() {
        return spec;
    }

    public Object getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return page + " x " + price + " x " + num + " + " + spec + " = " + sum;
    }
}
