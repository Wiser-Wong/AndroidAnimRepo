package com.wiser.animationlistdemo.combin.bean;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class DiscoverIndexResult {

    private List<DiscoverOper> discoverList;
    private List<DiscoverOper> localLevel2List;

    public List<DiscoverOper> getLocalLevel2List() {

        return localLevel2List;
    }

    public void setLocalLevel2List(List<DiscoverOper> localLevel2List) {

        this.localLevel2List = localLevel2List;
    }

    public List<DiscoverOper> getDiscoverList() {

        return discoverList;
    }

    @JSONField(name = "discover_list")
    public void setDiscoverList(List<DiscoverOper> discoverList) {

        this.discoverList = discoverList;
    }

    @Override
    public String toString() {
        return "DiscoverIndexResult{" +
                "discoverList=" + discoverList +
                '}';
    }
}
