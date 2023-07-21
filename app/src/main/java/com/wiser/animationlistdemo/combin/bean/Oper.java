package com.wiser.animationlistdemo.combin.bean;


import com.wiser.animationlistdemo.combin.TextUtil;

public class Oper {

    public static final String TYPE_CONTAINER = "container";
    public static final String TYPE_BANNER = "banner";
    private int element_id;
    private String element_type = TextUtil.TEXT_EMPTY;
    private String title = TextUtil.TEXT_EMPTY;
    private String pic = TextUtil.TEXT_EMPTY;

    private ChildrenOpers children;

    public int getElement_id() {
        return element_id;
    }

    public void setElement_id(int element_id) {
        this.element_id = element_id;
    }

    public ChildrenOpers getChildren() {
        return children;
    }

    public void setChildren(ChildrenOpers children) {
        this.children = children;
    }

    public String getElement_type() {
        return element_type;
    }

    public void setElement_type(String element_type) {
        this.element_type = element_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    public boolean isTypeContainer() {

        return TYPE_CONTAINER.equalsIgnoreCase(element_type);
    }

    public boolean isTypeBanner() {

        return TYPE_BANNER.equals(element_type);
    }

    @Override
    public String toString() {

        return "Oper{" +
                "element_type='" + element_type + '\'' +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
