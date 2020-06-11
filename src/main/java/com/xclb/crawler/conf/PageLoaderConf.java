package com.xclb.crawler.conf;

import com.xclb.crawler.loader.PageLoader;
import com.xclb.crawler.loader.strategy.HtmlUnitPageLoader;
import com.xclb.crawler.loader.strategy.JsoupPageLoader;
import com.xclb.crawler.loader.strategy.SeleniumChromePageLoader;
import com.xclb.crawler.loader.strategy.SeleniumPhantomjsPageLoader;
import com.xclb.crawler.util.PropertiesUtil;

import java.util.Map;
import java.util.Properties;

public class PageLoaderConf {
    private static Properties props = PropertiesUtil.getProp("crawler.properties");

    //不支持js，速度最快
    private static PageLoader jsoupPageLoader = new JsoupPageLoader();
    //支持动态js
    private static PageLoader htmlUnitPageLoader = new HtmlUnitPageLoader();
    //支持动态js，需要插件Phantomjs
    private static PageLoader seleniumPhantomjsPageLoader = new SeleniumPhantomjsPageLoader(PropertiesUtil.getProperty(props,"crawler.phantomjspath","C:/Development/plugins/phantomjs-2.1.1-windows/bin/phantomjs.exe"));
    //支持动态js，需要插件ChromeD
    private static PageLoader seleniumChromePageLoader = new SeleniumChromePageLoader(PropertiesUtil.getProperty(props,"crawler.chromepath","C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe"));

    public static PageLoader getJsoupPageLoader() {
        return jsoupPageLoader;
    }

    public static void setJsoupPageLoader(PageLoader jsoupPageLoader) {
        PageLoaderConf.jsoupPageLoader = jsoupPageLoader;
    }

    public static PageLoader getHtmlUnitPageLoader() {
        return htmlUnitPageLoader;
    }

    public static void setHtmlUnitPageLoader(PageLoader htmlUnitPageLoader) {
        PageLoaderConf.htmlUnitPageLoader = htmlUnitPageLoader;
    }

    public static PageLoader getSeleniumPhantomjsPageLoader() {
        return seleniumPhantomjsPageLoader;
    }

    public static void setSeleniumPhantomjsPageLoader(PageLoader seleniumPhantomjsPageLoader) {
        PageLoaderConf.seleniumPhantomjsPageLoader = seleniumPhantomjsPageLoader;
    }

    public static PageLoader getSeleniumChromePageLoader() {
        return seleniumChromePageLoader;
    }

    public static void setSeleniumChromePageLoader(PageLoader seleniumChromePageLoader) {
        PageLoaderConf.seleniumChromePageLoader = seleniumChromePageLoader;
    }

    public static void setSeleniumPhantomjsPageLoader(String seleniumPhantomjsPageLoader) {
        PageLoaderConf.seleniumPhantomjsPageLoader = new SeleniumPhantomjsPageLoader(seleniumPhantomjsPageLoader);
    }

    public static void setSeleniumChromePageLoader(String seleniumChromePageLoader) {
        PageLoaderConf.seleniumChromePageLoader = new SeleniumChromePageLoader(seleniumChromePageLoader);
    }

}
