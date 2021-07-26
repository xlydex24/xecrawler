package com.xclb.crawler.select;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Select {

    private List<SelectChild> crawlerSelectChildList;
    // 0:定制化全静态
    // 1:半动态只要cssQuery，剩下留给model
    // 2:半动态全设置PageFieldSelect，剩下留给model
    // 3:全动态一定设置MapPageParser
    // 4:失败自增loader
    private Integer level;
    private Integer loader;
    private String url;
    private Integer threadCount;
    private Integer timeout;
    private Integer pause;
    private Map<String, String> cookieMap;

    public Select() {
        this.level = 3;
        this.loader = 0;
    }

    public Select(List<SelectChild> crawlerSelectChildList, Integer level, Integer loader) {
        this.crawlerSelectChildList = crawlerSelectChildList;
        this.level = level;
        this.loader = loader;
    }

    public Select(List<SelectChild> crawlerSelectChildList, Integer loader) {
        this.crawlerSelectChildList = crawlerSelectChildList;
        this.level = 3;
        this.loader = loader;
    }

    public Select(Integer loader) {
        this.level = 3;
        this.loader = loader;
    }

    public Select(String pageSelect, Integer loader) {
        setSelectChild(new SelectChild(pageSelect));
        this.level = 3;
        this.loader = loader;
    }

    public void setPageSelect(String pageSelect) {
        setSelectChild(new SelectChild(pageSelect));
    }

    public void setSelectChild(SelectChild... selectChilds) {
        if (this.crawlerSelectChildList == null) {
            this.crawlerSelectChildList = new ArrayList<>();
        }
        if (selectChilds != null && selectChilds.length > 0) {
            for (SelectChild selectChild : selectChilds) {
                this.crawlerSelectChildList.add(selectChild);
            }
        }
    }

    public void setFieldSelect(String parmname, String cssQuery, String selectType, String selectVal, String parmClass) {
        setFieldSelect(parmname, cssQuery, selectType, selectVal, parmClass, 0);
    }

    public void setFieldSelect(String parmname, String cssQuery, String selectType, String selectVal, String parmClass, int index) {
        SelectChild selectChild = getSelectChild(index);
        selectChild.setFieldSelect(parmname, cssQuery, selectType, selectVal, parmClass);
    }

    public void setFieldSelectList(List<FieldSelect> fieldSelectList) {
        SelectChild selectChild = getSelectChild(0);
        selectChild.setCrawlerSelectFieldList(fieldSelectList);
    }

    private SelectChild getSelectChild(int index) {
        if (this.crawlerSelectChildList == null) {
            this.crawlerSelectChildList = new ArrayList<>();
        }
        SelectChild selectChild = null;
        if (this.crawlerSelectChildList.size() == index) {
            this.crawlerSelectChildList.add(new SelectChild());
        } else if (this.crawlerSelectChildList.size() < index) {
            index = this.crawlerSelectChildList.size();
            this.crawlerSelectChildList.add(new SelectChild());
        }
        selectChild = this.crawlerSelectChildList.get(index);
        return selectChild;
    }

    public List<SelectChild> getCrawlerSelectChildList() {
        return crawlerSelectChildList;
    }

    public void setCrawlerSelectChildList(List<SelectChild> crawlerSelectChildList) {
        this.crawlerSelectChildList = crawlerSelectChildList;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLoader() {
        return loader;
    }

    public void setLoader(Integer loader) {
        this.loader = loader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getPause() {
        return pause;
    }

    public void setPause(Integer pause) {
        this.pause = pause;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }
}
