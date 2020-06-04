package com.xclb.crawler.select;


import java.util.ArrayList;
import java.util.List;



public class SelectChild {
    private String name;
    private String pageSelect;
    private List<FieldSelect> crawlerSelectFieldList;

    public SelectChild() {
        this.name = "map";
        this.pageSelect = "html";
    }

    public SelectChild(String pageSelect) {
        this.name = "list";
        this.pageSelect = pageSelect;
    }

    public SelectChild(String name, String pageSelect) {
        this.name = name;
        this.pageSelect = pageSelect;
    }

    public SelectChild(String name, String pageSelect, List<FieldSelect> crawlerSelectFieldList) {
        this.name = name;
        this.pageSelect = pageSelect;
        this.crawlerSelectFieldList = crawlerSelectFieldList;
    }

    public void setFieldSelect(String parmname, String cssQuery, String selectType, String selectVal, String parmClass) {
        if (this.crawlerSelectFieldList == null) {
            this.crawlerSelectFieldList = new ArrayList<>();
        }
        this.crawlerSelectFieldList.add(new FieldSelect(parmname, cssQuery, selectType, selectVal, parmClass));
    }

    public void setFieldSelect(String parmname, String cssQuery, String selectType, String selectVal, String parmClass,String regex,String template) {
        if (this.crawlerSelectFieldList == null) {
            this.crawlerSelectFieldList = new ArrayList<>();
        }
        this.crawlerSelectFieldList.add(new FieldSelect(parmname, cssQuery, selectType, selectVal, parmClass,regex,template));
    }

    public void setFieldSelect(FieldSelect fieldSelect) {
        if (this.crawlerSelectFieldList == null) {
            this.crawlerSelectFieldList = new ArrayList<>();
        }
        this.crawlerSelectFieldList.add(fieldSelect);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageSelect() {
        return pageSelect;
    }

    public void setPageSelect(String pageSelect) {
        this.pageSelect = pageSelect;
    }

    public List<FieldSelect> getCrawlerSelectFieldList() {
        return crawlerSelectFieldList;
    }

    public void setCrawlerSelectFieldList(List<FieldSelect> crawlerSelectFieldList) {
        this.crawlerSelectFieldList = crawlerSelectFieldList;
    }
}
