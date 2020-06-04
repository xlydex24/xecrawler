package com.xclb.crawler.loader.strategy;

import com.xclb.crawler.loader.PageLoader;
import com.xclb.crawler.model.PageRequest;
import com.xclb.crawler.util.JsoupUtil;
import org.jsoup.nodes.Document;

/**
 * jsoup page loader
 *
 * @author xuxueli 2017-12-28 00:29:49
 */
public class JsoupPageLoader extends PageLoader {

    @Override
    public Document load(PageRequest pageRequest) {
        return JsoupUtil.load(pageRequest);
    }

}
