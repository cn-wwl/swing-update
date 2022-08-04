package com.wwl.core.base;

import java.io.Serializable;

/**
 * @author wwl
 * @date 2022/7/27 14:44
 * @desc TODO
 */
public class PageHelper implements Serializable {
    private int pageIndex;

    private int pageSize;

    private int totalCount;

    public PageHelper() {
    }

    public PageHelper(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public PageHelper(int pageIndex, int pageSize, int totalCount) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
