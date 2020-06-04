package com.xclb.crawler.parser.strategy;

import com.xclb.crawler.parser.PageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 该文件为处理模板
 * @param <T>
 */
public class ModelPageParser<T> extends PageParser<T> {

    private T model ;
    private List<T> modelList = new ArrayList<>();

    @Override
    public void parse(Document document, Element element, T t) {
        if (modelList.size()<=0){
            this.model=t;
        }
        modelList.add(t);
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public List<T> getModelList() {
        return modelList;
    }

    public void setModelList(List<T> modelList) {
        this.modelList = modelList;
    }
}
