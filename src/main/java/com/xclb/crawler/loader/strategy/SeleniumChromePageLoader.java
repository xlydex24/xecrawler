package com.xclb.crawler.loader.strategy;


import com.xclb.crawler.loader.PageLoader;
import com.xclb.crawler.model.PageRequest;
import com.xclb.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * "selenisum + Chrome" page loader
 *
 * // TODO, selenium not support feature like : paramMap、headerMap、userAgent、referrer、ifPost
 *
 * @time 2018-10-16
 */

/**
 * 更新到4.6已经不需要驱动，当前还是3.x版本代码
 */
public class SeleniumChromePageLoader extends PageLoader {
    private static Logger logger = LoggerFactory.getLogger(SeleniumChromePageLoader.class);

    private String driverPath;

    public SeleniumChromePageLoader(String driverPath) {
        this.driverPath = driverPath;
    }

    @Override
    public Document load(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }

        if (driverPath!=null && driverPath.trim().length()>0) {
            System.setProperty("webdriver.chrome.driver", driverPath);
        }else {
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        }



//        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

//        chromeOptions.addArguments("--disable-gpu");
//        chromeOptions.addArguments("--user-agent=Mozilla/5.0 HAHA");
        if (pageRequest.getProxy() != null) {
            chromeOptions.addArguments("--proxy-server="+pageRequest.getProxy());
        }
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        try {
            // driver run
            webDriver.get(pageRequest.getUrl());

            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                for (Map.Entry<String, String> item: pageRequest.getCookieMap().entrySet()) {
                    webDriver.manage().addCookie(new Cookie(item.getKey(), item.getValue()));
                }
            }
            /*if (pageRequest.getHeaderMap() != null && !pageRequest.getHeaderMap().isEmpty()) {
                webDriver.setAdditionalHeaders(pageRequest.getHeaderMap());
            }

            if (pageRequest.getUserAgent() != null) {
                webDriver.setAdditionalHeader("User-Agent", pageRequest.getUserAgent());
            }

            if (pageRequest.getReferrer() != null) {
                webDriver.setAdditionalHeader("Referer", pageRequest.getReferrer());
            }*/

            webDriver.manage().timeouts().implicitlyWait(pageRequest.getTimeoutMillis(), TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().pageLoadTimeout(pageRequest.getTimeoutMillis(), TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().setScriptTimeout(pageRequest.getTimeoutMillis(), TimeUnit.MILLISECONDS);

            String pageSource = webDriver.getPageSource();
            if (pageSource != null) {
                Document html = Jsoup.parse(pageSource);
                return html;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
        return null;
    }

}
