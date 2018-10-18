package com.pinyougou.vo;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {
    //总记录数
    private long total;
    //列表；?表示占位符与泛型一样；只是在初始化值以后该属性的值不再可以修改
    private List<?> rows;

    public PageResult() {
    }

    public PageResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
