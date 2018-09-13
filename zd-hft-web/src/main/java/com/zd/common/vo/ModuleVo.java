package com.zd.common.vo;


import com.zd.domain.ModuleDO;

import java.util.List;

public class ModuleVo {
    private String id ;
    private String name ;
    private List<ModuleDO> moduleDOList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModuleDO> getModuleDOList() {
        return moduleDOList;
    }

    public void setModuleDOList(List<ModuleDO> moduleDOList) {
        this.moduleDOList = moduleDOList;
    }
}
