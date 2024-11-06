package com.xclb.crawler.core;

import com.xclb.crawler.loader.PageLoader;
import com.xclb.crawler.model.RunConf;
import com.xclb.crawler.parser.PageParser;
import com.xclb.crawler.proxy.ProxyMaker;
import com.xclb.crawler.rundata.RunData;
import com.xclb.crawler.rundata.strategy.LocalRunData;
import com.xclb.crawler.select.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrawlerBuilder {
    private CrawlerModel crawlerModel = new CrawlerModel();

    private volatile RunData runData = new LocalRunData();                          // 运行时数据模型

    private volatile RunConf runConf = new RunConf();                          // 运行时配置


    private List<String> urlList = new ArrayList<>();


    /**
     * 设置运行数据类型
     *
     * @param runData
     * @return Builder
     */
    public CrawlerBuilder setRunData(RunData runData) {
        this.runData = runData;
        return this;
    }

    /**
     * 待爬的URL列表
     *
     * @param urls
     * @return Builder
     */
    public CrawlerBuilder setUrls(String... urls) {
        if (urls != null && urls.length > 0) {
            for (String url : urls) {
                urlList.add(url);
            }
        }
        return this;
    }

    // run conf

    /**
     * 允许扩散爬取，将会以现有URL为起点扩散爬取整站
     *
     * @param allowSpread
     * @return Builder
     */
    public CrawlerBuilder setAllowSpread(boolean allowSpread) {
        this.runConf.setAllowSpread(allowSpread);
        return this;
    }

    /**
     * URL白名单正则，非空时进行URL白名单过滤页面
     *
     * @param whiteUrlRegexs
     * @return Builder
     */
    public CrawlerBuilder setWhiteUrlRegexs(String... whiteUrlRegexs) {
        if (whiteUrlRegexs != null && whiteUrlRegexs.length > 0) {
            for (String whiteUrlRegex : whiteUrlRegexs) {
                this.runConf.getWhiteUrlRegexs().add(whiteUrlRegex);
            }
        }
        return this;
    }

    /**
     * 页面解析器
     *
     * @param pageParser
     * @return Builder
     */
    public CrawlerBuilder setPageParser(PageParser pageParser) {
        this.runConf.setPageParser(pageParser);
        return this;
    }

    /**
     * 页面下载器
     *
     * @param pageLoader
     * @return Builder
     */
    public CrawlerBuilder setPageLoader(PageLoader pageLoader) {
        this.runConf.setPageLoader(pageLoader);
        return this;
    }

    // site

    /**
     * 请求参数
     *
     * @param paramMap
     * @return Builder
     */
    public CrawlerBuilder setParamMap(Map<String, String> paramMap) {
        this.runConf.setParamMap(paramMap);
        return this;
    }

    /**
     * 请求Cookie
     *
     * @param cookieMap
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setCookieMap(Map<String, String> cookieMap) {
        this.runConf.setCookieMap(cookieMap);
        return this;
    }

    /**
     * 请求Header
     *
     * @param headerMap
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setHeaderMap(Map<String, String> headerMap) {
        this.runConf.setHeaderMap(headerMap);
        return this;
    }

    /**
     * 请求UserAgent
     *
     * @param userAgents
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setUserAgent(String... userAgents) {
        if (userAgents != null && userAgents.length > 0) {
            for (String userAgent : userAgents) {
                if (!this.runConf.getUserAgentList().contains(userAgent)) {
                    this.runConf.getUserAgentList().add(userAgent);
                }
            }
        }
        return this;
    }

    /**
     * 请求Referrer
     *
     * @param referrer
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setReferrer(String referrer) {
        this.runConf.setReferrer(referrer);
        return this;
    }

    /**
     * 请求方式：true=POST请求、false=GET请求
     *
     * @param ifPost
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setIfPost(boolean ifPost) {
        this.runConf.setIfPost(ifPost);
        return this;
    }

    /**
     * 超时时间，毫秒
     *
     * @param timeoutMillis
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setTimeoutMillis(int timeoutMillis) {
        this.runConf.setTimeoutMillis(timeoutMillis);
        return this;
    }

    /**
     * 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
     *
     * @param pauseMillis
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setPauseMillis(int pauseMillis) {
        this.runConf.setPauseMillis(pauseMillis);
        return this;
    }

    /**
     * 代理生成器
     *
     * @param proxyMaker
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setProxyMaker(ProxyMaker proxyMaker) {
        this.runConf.setProxyMaker(proxyMaker);
        return this;
    }

    /**
     * 失败重试次数，大于零时生效
     *
     * @param failRetryCount
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setFailRetryCount(int failRetryCount) {
        if (failRetryCount > 0) {
            this.runConf.setFailRetryCount(failRetryCount);
        }
        return this;
    }

    // thread

    /**
     * 爬虫并发线程数
     *
     * @param threadCount
     * @return CrawlerBuilder
     */
    public CrawlerBuilder setThreadCount(int threadCount) {
        crawlerModel.setThreadCount(threadCount);
        return this;
    }

    public CrawlerBuilder setSelect(Select select) {
        crawlerModel.setSelect(select);
        return this;
    }

    public CrawlerModel build() {
        crawlerModel.setRunConf(runConf);
        crawlerModel.setRunData(runData);
        return crawlerModel;
    }

    public CrawlerModel reset() {
        LocalRunData localRunData = new LocalRunData();
        for (String url : urlList) {
            localRunData.addUrl(url);
        }
        crawlerModel.setRunConf(runConf);
        crawlerModel.setRunData(localRunData);
        return crawlerModel;
    }
}
