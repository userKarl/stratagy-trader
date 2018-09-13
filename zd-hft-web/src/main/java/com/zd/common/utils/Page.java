package com.zd.common.utils;

import com.github.pagehelper.PageHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class Page extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    //
    private int page;
    // 每页条数
    private int pageSize;

    public Page(Map<String, Object> params) {
        this.putAll(params);
        // 分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageHelper.startPage(page,pageSize);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
