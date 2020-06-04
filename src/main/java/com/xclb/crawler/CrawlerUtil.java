package com.xclb.crawler;

import com.xclb.crawler.conf.PageLoaderConf;
import com.xclb.crawler.core.CrawlerBuilder;
import com.xclb.crawler.core.CrawlerModel;
import com.xclb.crawler.loader.PageLoader;
import com.xclb.crawler.parser.PageParser;
import com.xclb.crawler.parser.strategy.MapPageParser;
import com.xclb.crawler.proxy.ProxyMaker;
import com.xclb.crawler.select.Select;

import java.util.Map;

public class CrawlerUtil {

    public static Map<String, Object> crawlerApi(Select select, String... urls) {
        MapPageParser mapPageParser = new MapPageParser();
        PageLoader pageLoader = getPageLoader(select.getLoader());
        crawle(true, false, 1, select, 5000, 0, null, 0, null, pageLoader, mapPageParser, urls);
        return mapPageParser.getModel();
    }

    public static Map<String, Object> crawlerApi(Select select,
                                                 int threadCount,
                                                 int timeout,
                                                 int pause,
                                                 Map<String, String> cookieMap,
                                                 int retryCount,
                                                 ProxyMaker proxyMaker,
                                                 String... urls) {
        MapPageParser mapPageParser = new MapPageParser();
        PageLoader pageLoader = getPageLoader(select.getLoader());
        crawle(true, false, threadCount, select, timeout, pause, cookieMap, retryCount, proxyMaker, pageLoader, mapPageParser, urls);
        return mapPageParser.getModel();
    }

    public static Map<String, Object> crawlerApi(Select select, CrawlerBuilder crawlerBuilder) {
        MapPageParser mapPageParser = new MapPageParser();
        PageLoader pageLoader = getPageLoader(select.getLoader());
        crawlerBuilder.setPageLoader(pageLoader);
        crawlerBuilder.setPageParser(mapPageParser);
        CrawlerModel crawlerModel = crawlerBuilder.build();
        crawlerModel.start(true);
        return mapPageParser.getModel();
    }


    private static PageLoader getPageLoader(Integer loader) {
        PageLoader pageLoader = null;
        if (loader == 0) {
            pageLoader = PageLoaderConf.getJsoupPageLoader();
        } else if (loader == 1) {
            pageLoader = PageLoaderConf.getHtmlUnitPageLoader();
        } else if (loader == 2) {
            pageLoader = PageLoaderConf.getSeleniumPhantomjsPageLoader();
        } else if (loader == 3) {
            pageLoader = PageLoaderConf.getSeleniumChromePageLoader();
        } else {
            pageLoader = PageLoaderConf.getJsoupPageLoader();
        }
        return pageLoader;
    }


    /**
     * CrawlerModel.Builder
     *
     * @param sync        同步开关
     * @param allowSpread 扩散开关
     * @param threadCount 线程数
     * @param select      select规则
     * @param timeout     超时时间，毫秒
     * @param pause       主动停顿时间
     * @param cookieMap   cookie
     * @param retryCount  失败重试次数，大于零时生效
     * @param proxyMaker  代理
     * @param pageLoader  引擎
     * @param pageParser  数据模型
     * @param urls        地址
     */
    public static void crawle(boolean sync,
                              boolean allowSpread,
                              int threadCount,
                              Select select,
                              int timeout,
                              int pause,
                              Map<String, String> cookieMap,
                              int retryCount,
                              ProxyMaker proxyMaker,
                              PageLoader pageLoader,
                              PageParser pageParser,
                              String... urls) {
        // 构造爬虫
        CrawlerModel crawler = new CrawlerModel.Builder()
                .setUrls(urls)
                .setAllowSpread(allowSpread)
                .setThreadCount(threadCount)
                .setSelect(select)
                .setTimeoutMillis(timeout)
                .setPauseMillis(pause)
                .setCookieMap(cookieMap)
                .setFailRetryCount(retryCount)
                .setProxyMaker(proxyMaker)
                .setPageLoader(pageLoader)
                .setPageParser(pageParser)
                .build();
        // 启动
        crawler.start(sync);
    }

    /**
     * crawleBuilder(优化)
     *
     * @param sync        同步开关
     * @param allowSpread 扩散开关
     * @param threadCount 线程数
     * @param select      select规则
     * @param timeout     超时时间，毫秒
     * @param pause       主动停顿时间
     * @param cookieMap   cookie
     * @param retryCount  失败重试次数，大于零时生效
     * @param proxyMaker  代理
     * @param pageLoader  引擎
     * @param pageParser  数据模型
     * @param urls        地址
     */
    public static void crawleBuilder(boolean sync,
                                     boolean allowSpread,
                                     int threadCount,
                                     Select select,
                                     int timeout,
                                     int pause,
                                     Map<String, String> cookieMap,
                                     int retryCount,
                                     ProxyMaker proxyMaker,
                                     PageLoader pageLoader,
                                     PageParser pageParser,
                                     String... urls) {
        CrawlerBuilder crawlerBuilder = new CrawlerBuilder();
        crawlerBuilder.setUrls(urls);
        crawlerBuilder.setAllowSpread(allowSpread);
        crawlerBuilder.setThreadCount(threadCount);
        crawlerBuilder.setSelect(select);
        crawlerBuilder.setTimeoutMillis(timeout);
        crawlerBuilder.setPauseMillis(pause);
        crawlerBuilder.setCookieMap(cookieMap);
        crawlerBuilder.setFailRetryCount(retryCount);
        crawlerBuilder.setProxyMaker(proxyMaker);
        crawlerBuilder.setPageLoader(pageLoader);
        crawlerBuilder.setPageParser(pageParser);
        CrawlerModel crawler = crawlerBuilder.build();
        crawler.start(sync);
    }


}
