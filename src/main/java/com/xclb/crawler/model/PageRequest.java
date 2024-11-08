package com.xclb.crawler.model;

import lombok.Getter;
import lombok.Setter;

import java.net.Proxy;
import java.util.Map;

/**
 * page load info
 *
 * @time 2017-11-10 17:12:55
 */
@Setter
@Getter
public class PageRequest {

    private String url;
    private Map<String, String> paramMap;
    private Map<String, String> cookieMap;
    private Map<String, String> headerMap;
    private String userAgent;
    private String referrer;
    private boolean ifPost;
    private int timeoutMillis;
    private boolean isValidateTLSCertificates;
    private Proxy proxy;
    private boolean ssl;
    private Integer loader;

    public PageRequest() {
    }

}