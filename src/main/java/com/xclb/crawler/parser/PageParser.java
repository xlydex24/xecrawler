package com.xclb.crawler.parser;


import com.xclb.crawler.model.PageRequest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * page parser
 *
 * @time 2017-10-17 18:50:40
 *
 * @param <T>   PageVo
 */
public abstract class PageParser<T> {

    /**
     * pre parse page, before page load
     *
     * @param pageRequest  page request params
     */
    public void preParse(PageRequest pageRequest) {
        // TODO
    }

    /**
     * parse pageVo
     *
     * @param html              page html data
     * @param pageVoElement     pageVo html data
     * @param pageVo            pageVo object
     */
    public abstract void parse(Document html, Element pageVoElement, T pageVo);

}
