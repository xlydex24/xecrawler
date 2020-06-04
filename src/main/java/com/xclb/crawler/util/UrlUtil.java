package com.xclb.crawler.util;

/**
 * url tool
 *
 * @author
 */
public class UrlUtil {

    /**
     * url格式校验
     */
    public static boolean isUrl(String url) {
        if (url!=null && url.trim().length()>0 && url.startsWith("http")) {
            return true;
        }
        return false;
    }

}
