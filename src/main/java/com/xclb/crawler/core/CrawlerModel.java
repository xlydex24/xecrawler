package com.xclb.crawler.core;


import com.xclb.crawler.loader.PageLoader;
import com.xclb.crawler.model.RunConf;
import com.xclb.crawler.parser.PageParser;
import com.xclb.crawler.proxy.ProxyMaker;
import com.xclb.crawler.rundata.RunData;
import com.xclb.crawler.rundata.strategy.LocalRunData;
import com.xclb.crawler.select.Select;
import com.xclb.crawler.thread.CrawlerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * xxl crawler
 * <p>
 * Created by xuxueli on 2015-05-14 22:44:43
 */
public class CrawlerModel {
    private static Logger logger = LoggerFactory.getLogger(CrawlerModel.class);

    // run data
    private volatile RunData runData = new LocalRunData();                          // 运行时数据模型

    // run conf
    private volatile RunConf runConf = new RunConf();                               // 运行时配置

    // thread
    private int threadCount = 1;                                                    // 爬虫线程数量
    private ExecutorService crawlers = Executors.newCachedThreadPool();             // 爬虫线程池
    private List<CrawlerThread> crawlerThreads = new CopyOnWriteArrayList<CrawlerThread>();     // 爬虫线程引用镜像

    private Select select = new Select();

    // ---------------------- get set ----------------------

    public RunData getRunData() {
        return runData;
    }

    public RunConf getRunConf() {
        return runConf;
    }

    public Select getSelect() {
        return select;
    }

    public void setRunData(RunData runData) {
        this.runData = runData;
    }

    public void setRunConf(RunConf runConf) {
        this.runConf = runConf;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void setSelect(Select select) {
        this.select = select;
    }


    // ---------------------- builder ----------------------
    public static class Builder {
        private CrawlerModel crawler = new CrawlerModel();

        // run data

        /**
         * 设置运行数据类型
         *
         * @param runData
         * @return Builder
         */
        public Builder setRunData(RunData runData) {
            crawler.runData = runData;
            return this;
        }

        /**
         * 待爬的URL列表
         *
         * @param urls
         * @return Builder
         */
        public Builder setUrls(String... urls) {
            if (urls != null && urls.length > 0) {
                for (String url : urls) {
                    crawler.runData.addUrl(url);
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
        public Builder setAllowSpread(boolean allowSpread) {
            crawler.runConf.setAllowSpread(allowSpread);
            return this;
        }

        /**
         * URL白名单正则，非空时进行URL白名单过滤页面
         *
         * @param whiteUrlRegexs
         * @return Builder
         */
        public Builder setWhiteUrlRegexs(String... whiteUrlRegexs) {
            if (whiteUrlRegexs != null && whiteUrlRegexs.length > 0) {
                for (String whiteUrlRegex : whiteUrlRegexs) {
                    crawler.runConf.getWhiteUrlRegexs().add(whiteUrlRegex);
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
        public Builder setPageParser(PageParser pageParser) {
            crawler.runConf.setPageParser(pageParser);
            return this;
        }

        /**
         * 页面下载器
         *
         * @param pageLoader
         * @return Builder
         */
        public Builder setPageLoader(PageLoader pageLoader) {
            crawler.runConf.setPageLoader(pageLoader);
            return this;
        }

        // site

        /**
         * 请求参数
         *
         * @param paramMap
         * @return Builder
         */
        public Builder setParamMap(Map<String, String> paramMap) {
            crawler.runConf.setParamMap(paramMap);
            return this;
        }

        /**
         * 请求Cookie
         *
         * @param cookieMap
         * @return Builder
         */
        public Builder setCookieMap(Map<String, String> cookieMap) {
            crawler.runConf.setCookieMap(cookieMap);
            return this;
        }

        /**
         * 请求Header
         *
         * @param headerMap
         * @return Builder
         */
        public Builder setHeaderMap(Map<String, String> headerMap) {
            crawler.runConf.setHeaderMap(headerMap);
            return this;
        }

        /**
         * 请求UserAgent
         *
         * @param userAgents
         * @return Builder
         */
        public Builder setUserAgent(String... userAgents) {
            if (userAgents != null && userAgents.length > 0) {
                for (String userAgent : userAgents) {
                    if (!crawler.runConf.getUserAgentList().contains(userAgent)) {
                        crawler.runConf.getUserAgentList().add(userAgent);
                    }
                }
            }
            return this;
        }

        /**
         * 请求Referrer
         *
         * @param referrer
         * @return Builder
         */
        public Builder setReferrer(String referrer) {
            crawler.runConf.setReferrer(referrer);
            return this;
        }

        /**
         * 请求方式：true=POST请求、false=GET请求
         *
         * @param ifPost
         * @return Builder
         */
        public Builder setIfPost(boolean ifPost) {
            crawler.runConf.setIfPost(ifPost);
            return this;
        }

        /**
         * 超时时间，毫秒
         *
         * @param timeoutMillis
         * @return Builder
         */
        public Builder setTimeoutMillis(int timeoutMillis) {
            crawler.runConf.setTimeoutMillis(timeoutMillis);
            return this;
        }

        /**
         * 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
         *
         * @param pauseMillis
         * @return Builder
         */
        public Builder setPauseMillis(int pauseMillis) {
            crawler.runConf.setPauseMillis(pauseMillis);
            return this;
        }

        /**
         * 代理生成器
         *
         * @param proxyMaker
         * @return Builder
         */
        public Builder setProxyMaker(ProxyMaker proxyMaker) {
            crawler.runConf.setProxyMaker(proxyMaker);
            return this;
        }

        /**
         * 失败重试次数，大于零时生效
         *
         * @param failRetryCount
         * @return Builder
         */
        public Builder setFailRetryCount(int failRetryCount) {
            if (failRetryCount > 0) {
                crawler.runConf.setFailRetryCount(failRetryCount);
            }
            return this;
        }

        // thread

        /**
         * 爬虫并发线程数
         *
         * @param threadCount
         * @return Builder
         */
        public Builder setThreadCount(int threadCount) {
            crawler.threadCount = threadCount;
            return this;
        }

        public Builder setSelect(Select select) {
            crawler.select = select;
            return this;
        }

        public CrawlerModel build() {
            return crawler;
        }
    }

    // ---------------------- crawler thread ----------------------

    /**
     * 启动
     *
     * @param sync true=同步方式、false=异步方式
     */
    public void start(boolean sync) {
        if (runData == null) {
            throw new RuntimeException("xxl crawler runData can not be null.");
        }
        if (runData.getUrlNum() <= 0) {
            throw new RuntimeException("xxl crawler indexUrl can not be empty.");
        }
        if (runConf == null) {
            throw new RuntimeException("xxl crawler runConf can not be empty.");
        }
        if (threadCount < 0 || threadCount > 1000) {
            throw new RuntimeException("xxl crawler threadCount invalid, threadCount : " + threadCount);
        }
        if (runConf.getPageLoader() == null) {
            throw new RuntimeException("xxl crawler pageLoader can not be null.");
        }
        if (runConf.getPageParser() == null) {
            throw new RuntimeException("xxl crawler pageParser can not be null.");
        }

        logger.debug(">>>>>>>>>>> xxl crawler start ...");

        if (threadCount == 0) {
            CrawlerThread crawlerThread = new CrawlerThread(this);
            if (sync) {
                crawlerThread.run();
            }else {
                crawlers.execute(crawlerThread);
            }
        } else {
            for (int i = 0; i < threadCount; i++) {
                CrawlerThread crawlerThread = new CrawlerThread(this);
                crawlerThreads.add(crawlerThread);
            }
            for (CrawlerThread crawlerThread : crawlerThreads) {
                crawlers.execute(crawlerThread);
            }
            crawlers.shutdown();

            if (sync) {
                try {
                    while (!crawlers.awaitTermination(1, TimeUnit.SECONDS)) {
                        logger.debug(">>>>>>>>>>> xxl crawler still running ...");
                    }
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }


    }

    /**
     * 尝试终止
     */
    public void tryFinish() {
        boolean isRunning = false;
        for (CrawlerThread crawlerThread : crawlerThreads) {
            if (crawlerThread.isRunning()) {
                isRunning = true;
                break;
            }
        }
        boolean isEnd = runData.getUrlNum() == 0 && !isRunning;
        if (isEnd) {
            logger.debug(">>>>>>>>>>> xxl crawler is finished.");
            stop();
        }
    }

    /**
     * 终止
     */
    public void stop() {
        for (CrawlerThread crawlerThread : crawlerThreads) {
            crawlerThread.toStop();
        }
        crawlers.shutdownNow();
        logger.debug(">>>>>>>>>>> xxl crawler stop.");
    }

}
