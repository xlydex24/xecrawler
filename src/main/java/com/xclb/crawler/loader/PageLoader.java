package com.xclb.crawler.loader;


import com.xclb.crawler.model.PageRequest;
import org.jsoup.nodes.Document;

/**
 * page loader
 *
 * @time 2017-12-28 00:27:30
 */
public abstract class PageLoader {

    /**
     * load page
     *
     * @param pageRequest
     * @return Document
     */
    public abstract Document load(PageRequest pageRequest);

}
