package com.xclb.crawler.parser.strategy;



import com.xclb.crawler.parser.PageParser;
import com.xclb.crawler.util.ModelUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用处理测试
 */

public class MapPageParser extends PageParser<Map<String, Object>> {

    private Map<String, Object> model;
    private List<Map<String, Object>> modelList = new ArrayList<>();

    @Override
    public void parse(Document document, Element element, Map<String, Object> t) {
        if (modelList.size() <= 0) {
            this.model = t;
            model.put("document",document);
        }else {
            ModelUtil.MapInMap(this.model,t);
        }
        modelList.add(t);
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public List<Map<String, Object>> getModelList() {
        return modelList;
    }

    public void setModelList(List<Map<String, Object>> modelList) {
        this.modelList = modelList;
    }
}
