package com.wiser.animationlistdemo.combin.bean;

import java.io.Serializable;
import java.util.List;

public class ChildrenOpers implements Serializable {

    private List<DiscoverOper> banner;  //Banner

    private List<DiscoverOper> container; // WebView

    public List<DiscoverOper> getBanner() {

        return banner;
    }

    public void setBanner(List<DiscoverOper> banner) {

        this.banner = banner;
    }

    public List<DiscoverOper> getContainer() {

        return container;
    }

    public void setContainer(List<DiscoverOper> container) {

        this.container = container;
    }

    @Override
    public String toString() {
        return "ChildrenOpers{" +
                "banner=" + banner +
                ", container=" + container +
                '}';
    }
}
