package com.xclb.crawler.parser.strategy;



import java.util.Map;

/**
 * non page parser
 *
 * @author xuxueli 2018-10-17
 */

public class MapNonPageParser extends NonPageParser {

    private Map<String,String> model ;
    private String data;
    /**
     * @param url
     * @param pageSource
     */
    public void parse(String url, String pageSource){
        model.put("url",url);
        model.put("pageSource",pageSource);
        data=pageSource;
    }

    public Map<String, String> getModel() {
        return model;
    }

    public void setModel(Map<String, String> model) {
        this.model = model;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
