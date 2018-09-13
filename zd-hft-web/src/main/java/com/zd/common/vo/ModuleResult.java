package com.zd.common.vo;

import java.util.Map;

public class ModuleResult {

    private Map<String,Object> idList;
    private Map<String,Object> modMap;



    public Map<String, Object> getIdList() {
        return idList;
    }

    public void setIdList(Map<String, Object> idList) {
        this.idList = idList;
    }

    public Map<String, Object> getModMap() {
        return modMap;
    }

    public void setModMap(Map<String, Object> modMap) {
        this.modMap = modMap;
    }
}
