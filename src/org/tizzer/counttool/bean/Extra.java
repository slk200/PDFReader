package org.tizzer.counttool.bean;

import java.io.Serializable;

/**
 * Created by tizzer on 2019/1/21.
 */
public class Extra implements Serializable {
    private String name;
    private String price;

    public Extra() {
    }

    public Extra(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        if (name.length() > 10) {
            return name.substring(0, 10) + "...  ￥: " + price;
        }
        return name + "  ￥: " + price;
    }
}
