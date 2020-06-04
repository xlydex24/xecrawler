package com.xclb.crawler.thread;


import com.xclb.crawler.annotation.PageFieldSelect;
import com.xclb.crawler.annotation.PageSelect;
import com.xclb.crawler.conf.PageLoaderConf;
import com.xclb.crawler.core.CrawlerModel;
import com.xclb.crawler.exception.XxlCrawlerException;
import com.xclb.crawler.loader.PageLoader;
import com.xclb.crawler.model.PageRequest;
import com.xclb.crawler.parser.PageParser;
import com.xclb.crawler.parser.strategy.MapNonPageParser;
import com.xclb.crawler.parser.strategy.MapPageParser;
import com.xclb.crawler.parser.strategy.ModelPageParser;
import com.xclb.crawler.parser.strategy.NonPageParser;
import com.xclb.crawler.select.FieldSelect;
import com.xclb.crawler.select.Select;
import com.xclb.crawler.select.SelectChild;
import com.xclb.crawler.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * crawler thread
 *
 * @author xuxueli 2017-10-10 10:58:19
 */
public class CrawlerThread implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

    private CrawlerModel crawler;
    private boolean running;
    private boolean toStop;

    public CrawlerThread(CrawlerModel crawler) {
        this.crawler = crawler;
        this.running = true;
        this.toStop = false;
    }

    public void toStop() {
        this.toStop = true;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {

        while (!toStop) {
            try {

                // ------- url ----------
                running = false;
                crawler.tryFinish();
                String link = crawler.getRunData().getUrl();
                running = true;
                logger.info(">>>>>>>>>>> xxl crawler, process link : {}", link);
                if (!UrlUtil.isUrl(link)) {
                    continue;
                }

                // failover
                for (int i = 0; i < (1 + crawler.getRunConf().getFailRetryCount()); i++) {

                    boolean ret = false;
                    try {
                        // make request
                        PageRequest pageRequest = makePageRequest(link);

                        // pre parse
                        crawler.getRunConf().getPageParser().preParse(pageRequest);

                        PageParser pageParser = crawler.getRunConf().getPageParser();
                        // parse
                        if (pageParser instanceof NonPageParser) {
                            ret = processNonPage(pageRequest);
                        } else if (pageParser instanceof MapNonPageParser) {
                            ret = processMapNonPage(pageRequest);
                        } else if (pageParser instanceof MapPageParser) {
                            ret = processMapPage(pageRequest);
                        }else if (pageParser instanceof ModelPageParser) {
                            ret = processPage(pageRequest);
                        } else {
                            ret = processPage(pageRequest);
                        }
                    } catch (Throwable e) {
                        logger.info(">>>>>>>>>>> xxl crawler proocess error.", e);
                    }

                    if (crawler.getRunConf().getPauseMillis() > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(crawler.getRunConf().getPauseMillis());
                        } catch (InterruptedException e) {
                            logger.info(">>>>>>>>>>> xxl crawler thread is interrupted. 2{}", e.getMessage());
                        }
                    }
                    if (ret) {
                        break;
                    }
                }

            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    logger.info(">>>>>>>>>>> xxl crawler thread is interrupted. {}", e.getMessage());
                } else if (e instanceof XxlCrawlerException) {
                    logger.info(">>>>>>>>>>> xxl crawler thread {}", e.getMessage());
                } else {
                    logger.error(e.getMessage(), e);
                }
            }

        }
    }


    /**
     * make page request
     *
     * @param link
     * @return PageRequest
     */
    private PageRequest makePageRequest(String link) {
        String userAgent = crawler.getRunConf().getUserAgentList().size() > 1
                ? crawler.getRunConf().getUserAgentList().get(new Random().nextInt(crawler.getRunConf().getUserAgentList().size()))
                : crawler.getRunConf().getUserAgentList().size() == 1 ? crawler.getRunConf().getUserAgentList().get(0) : null;
        Proxy proxy = null;
        if (crawler.getRunConf().getProxyMaker() != null) {
            proxy = crawler.getRunConf().getProxyMaker().make();
        }

        PageRequest pageRequest = new PageRequest();
        pageRequest.setUrl(link);
        pageRequest.setParamMap(crawler.getRunConf().getParamMap());
        pageRequest.setCookieMap(crawler.getRunConf().getCookieMap());
        pageRequest.setHeaderMap(crawler.getRunConf().getHeaderMap());
        pageRequest.setUserAgent(userAgent);
        pageRequest.setReferrer(crawler.getRunConf().getReferrer());
        pageRequest.setIfPost(crawler.getRunConf().isIfPost());
        pageRequest.setTimeoutMillis(crawler.getRunConf().getTimeoutMillis());
        pageRequest.setProxy(proxy);
        pageRequest.setValidateTLSCertificates(crawler.getRunConf().isValidateTLSCertificates());

        return pageRequest;
    }

    private Document load(PageRequest pageRequest){
        Document html = crawler.getRunConf().getPageLoader().load(pageRequest);

        if (html == null) {
            Integer loader = crawler.getSelect().getLoader();
            PageLoader pageLoader = null;
            if (loader < 1) {
                pageLoader = PageLoaderConf.getHtmlUnitPageLoader();
                html = pageLoader.load(pageRequest);
            }
            if (html == null) {
                pageLoader = PageLoaderConf.getSeleniumChromePageLoader();
                html = pageLoader.load(pageRequest);
            }
        }
        return html;
    }

    /**
     * process non page
     *
     * @param pageRequest
     * @return boolean
     */
    private boolean processNonPage(PageRequest pageRequest) {
        NonPageParser nonPageParser = (NonPageParser) crawler.getRunConf().getPageParser();

        String pagesource = JsoupUtil.loadPageSource(pageRequest);
        if (pagesource == null) {
            return false;
        }
        nonPageParser.parse(pageRequest.getUrl(), pagesource);
        return true;
    }

    /**
     * process non page
     *
     * @param pageRequest
     * @return boolean
     */
    private boolean processMapNonPage(PageRequest pageRequest) {
        MapNonPageParser nonPageParser = (MapNonPageParser) crawler.getRunConf().getPageParser();

        String pagesource = JsoupUtil.loadPageSource(pageRequest);
        if (pagesource == null) {
            return false;
        }
        nonPageParser.parse(pageRequest.getUrl(), pagesource);
        return true;
    }

    private boolean processMapPage(PageRequest pageRequest)  {
        String baseUrl = pageRequest.getUrl();
        Document html=load(pageRequest);

        if (html == null) {
            return false;
        }

        // ------- child link list (FIFO队列,广度优先) ----------
        if (crawler.getRunConf().isAllowSpread()) {     // limit child spread
            Set<String> links = JsoupUtil.findLinks(html);
            if (links != null && links.size() > 0) {
                for (String item : links) {
                    if (crawler.getRunConf().validWhiteUrl(item)) {      // limit unvalid-child spread
                        crawler.getRunData().addUrl(item);
                    }
                }
            }
        }

        // ------- pagevo ----------
        if (!crawler.getRunConf().validWhiteUrl(pageRequest.getUrl())) {     // limit unvalid-page parse, only allow spread child, finish here
            return true;
        }


        //经过修改----动态赋值
        Select select = crawler.getSelect();

        Map<String, Object> map = new HashMap<>();
        Element Element = null;

        List<SelectChild> crawlerSelectChildList = select.getCrawlerSelectChildList();
        for (int i = 0; i < crawlerSelectChildList.size(); i++) {
            SelectChild selectChild = crawlerSelectChildList.get(i);
            String pageVoCssQuery = "html";
            String pageSelect = selectChild.getPageSelect();
            if (pageSelect != null && pageSelect != "" && pageSelect.trim().length() > 0) {
                pageVoCssQuery = pageSelect.replaceAll("&gt;", ">");
            }
            //结束

            // pagevo document 2 object
            Elements pageVoElements = html.select(pageVoCssQuery);

            Map<String, Object> pageVomodel = new HashMap<>();
            List<Map<String, Object>> pageVomodelList = new ArrayList<>();
            if (pageVoElements != null && pageVoElements.hasText()) {
                for (int j = 0; j < pageVoElements.size(); j++) {
                    org.jsoup.nodes.Element pageVoElement = pageVoElements.get(j);
                    Map<String, Object> pageVo = new HashMap<>();
                    List<FieldSelect> fieldSelectList = selectChild.getCrawlerSelectFieldList();

                    pageVo = getPageVo(fieldSelectList, pageVoElement);
                    Element = pageVoElement;
                    // pagevo output
                    pageVoElement.setBaseUri(pageRequest.getUrl());
//                    pageVo.put("baseUrl", pageRequest.getUrl());
                    if (pageVoCssQuery.equals("html")) {
                        pageVomodel = pageVo;
                    } else {
                        pageVomodelList.add(pageVo);
                    }
                }
            }
            if (selectChild.getName().contains(".")) {
                if (pageVoCssQuery.equals("html")) {
                    BeanUtil.setProperty(map, selectChild.getName(), pageVomodel);
                } else {
                    BeanUtil.setProperty(map, selectChild.getName(), pageVomodelList);
                }
            } else {
                if (pageVoCssQuery.equals("html")) {
                    Map<String, Object> mapModel = (Map<String, Object>) map.get(selectChild.getName());
                    if (mapModel != null) {
                        mapModel.putAll(pageVomodel);
                    } else {
                        map.put(selectChild.getName(), pageVomodel);
                    }
                } else {
                    map.put(selectChild.getName(), pageVomodelList);
                }
            }

        }

        if (baseUrl == null) {
            baseUrl = pageRequest.getUrl();
        }
        map.put("baseUrl", baseUrl);
        crawler.getRunConf().getPageParser().parse(html, Element, map);

        return true;
    }

    private Map<String, Object> getPageVo(List<FieldSelect> fieldSelectList, Element pageVoElement) {
        Map<String, Object> pageVo = new HashMap<>();
        if (fieldSelectList != null) {
            for (FieldSelect fieldSelect : fieldSelectList) {
                List<String> name = StringUtil.split(fieldSelect.getParmname(),"\\.");
                String cssQuery = fieldSelect.getCssQuery().replaceAll("&gt;", ">");
                String selectType = fieldSelect.getSelectType();
                String selectVal = fieldSelect.getSelectVal();
                String regex = fieldSelect.getRegex();
                String template = fieldSelect.getTemplate();
                if (cssQuery == null || cssQuery.trim().length() == 0) {
                    continue;
                }
                Object fieldValue = null;

                if (fieldSelect.getParmClass().equals(List.class)) {
                    Elements fieldElementList = pageVoElement.select(cssQuery);
                    if (fieldElementList != null && fieldElementList.size() > 0) {

                        List<Object> fieldValueTmp = new ArrayList<Object>();
                        for (Element fieldElement : fieldElementList) {

                            String fieldElementOrigin = ElementUtil.parseElement(fieldElement, selectType, selectVal, regex, template);
                            if (fieldElementOrigin == null || fieldElementOrigin.length() == 0) {
                                continue;
                            }
                            try {
                                fieldValueTmp.add(ElementUtil.parseValue(fieldSelect.getParmClass(), fieldSelect.getDatePattern(), fieldElementOrigin));
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }

                        if (fieldValueTmp.size() > 0) {
                            fieldValue = fieldValueTmp;
                        }
                    }
                } else if (fieldSelect.getParmClassString().equals("Size")) {
                    Elements fieldElementList = pageVoElement.select(cssQuery);
                    if (fieldElementList != null && fieldElementList.size() > 0) {
                        fieldValue = fieldElementList.size();
                    }
                } else if (fieldSelect.getParmClassString().equals("ListMap")) {
                    Elements fieldElementList = pageVoElement.select(cssQuery);
                    if (fieldElementList != null && fieldElementList.size() > 0) {

                        List<Object> fieldValueTmp = (List<Object>) pageVo.get(name.get(0));
                        if (fieldValueTmp == null) {
                            fieldValueTmp = new ArrayList<>();
                            for (Element fieldElement : fieldElementList) {

                                String fieldElementOrigin = ElementUtil.parseElement(fieldElement, selectType, selectVal, regex, template);
                                if (fieldElementOrigin == null || fieldElementOrigin.length() == 0) {
                                    continue;
                                }
                                try {
                                    fieldValueTmp.add(ElementUtil.parseValue(Map.class, fieldSelect.getDatePattern(), fieldElementOrigin, name.get(1)));
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                        } else {

                            for (int i = 0; i < fieldElementList.size(); i++) {
                                Map<String, String> map = (Map<String, String>) fieldValueTmp.get(i);
                                String fieldElementOrigin = ElementUtil.parseElement(fieldElementList.get(i), selectType, selectVal, regex, template);
                                if (fieldElementOrigin == null || fieldElementOrigin.length() == 0) {
                                    continue;
                                }
                                try {
                                    map.put(name.get(1), fieldElementOrigin);
//                                    fieldValueTmp.add(map);
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                }
                            }

                        }


                        if (fieldValueTmp.size() > 0) {
                            fieldValue = fieldValueTmp;
                        }
                    }
                } else if (fieldSelect.getParmClass().equals(Map.class)) {
                    Elements fieldElements = pageVoElement.select(cssQuery);
                    String fieldValueOrigin = null;
                    if (fieldElements != null && fieldElements.size() > 0) {
                        fieldValueOrigin = ElementUtil.parseElement(fieldElements.get(0), selectType, selectVal, regex, template);
                    }

                    if (fieldValueOrigin == null || fieldValueOrigin.length() == 0) {
                        continue;
                    }
                    try {
                        Map<String, Object> fieldValueTmp = (Map<String, Object>) pageVo.get(name.get(0));
                        if (fieldValueTmp == null) {
                            fieldValue = ElementUtil.parseValue(fieldSelect.getParmClass(), fieldSelect.getDatePattern(), fieldValueOrigin, name.get(1));
                        } else {
                            fieldValueTmp.putAll((Map<String, Object>) ElementUtil.parseValue(fieldSelect.getParmClass(), fieldSelect.getDatePattern(), fieldValueOrigin, name.get(1)));
                            fieldValue = fieldValueTmp;
                        }

                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                } else {
                    Elements fieldElements = pageVoElement.select(cssQuery);
                    String fieldValueOrigin = null;
                    if (fieldElements != null && fieldElements.size() > 0) {
                        fieldValueOrigin = ElementUtil.parseElement(fieldElements.get(0), selectType, selectVal, regex, template);
                    }

                    if (fieldValueOrigin == null || fieldValueOrigin.length() == 0) {
                        continue;
                    }

                    try {
                        fieldValue = ElementUtil.parseValue(fieldSelect.getParmClass(), fieldSelect.getDatePattern(), fieldValueOrigin);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                pageVo.put(name.get(0), fieldValue);
            }
        }
        return pageVo;
    }

    /**
     * process page
     *
     * @param pageRequest
     * @return boolean
     */
    private boolean processPage(PageRequest pageRequest) throws IllegalAccessException, InstantiationException {
        Document html = crawler.getRunConf().getPageLoader().load(pageRequest);

        if (html == null) {
            return false;
        }

        // ------- child link list (FIFO队列,广度优先) ----------
        if (crawler.getRunConf().isAllowSpread()) {     // limit child spread
            Set<String> links = JsoupUtil.findLinks(html);
            if (links != null && links.size() > 0) {
                for (String item : links) {
                    if (crawler.getRunConf().validWhiteUrl(item)) {      // limit unvalid-child spread
                        crawler.getRunData().addUrl(item);
                    }
                }
            }
        }

        // ------- pagevo ----------
        if (!crawler.getRunConf().validWhiteUrl(pageRequest.getUrl())) {     // limit unvalid-page parse, only allow spread child, finish here
            return true;
        }

        // pagevo class-field info
        Class pageVoClassType = Object.class;

        Type pageVoParserClass = crawler.getRunConf().getPageParser().getClass().getGenericSuperclass();
        if (pageVoParserClass instanceof ParameterizedType) {
            Type[] pageVoClassTypes = ((ParameterizedType) pageVoParserClass).getActualTypeArguments();
            pageVoClassType = (Class) pageVoClassTypes[0];
        }

        PageSelect pageVoSelect = (PageSelect) pageVoClassType.getAnnotation(PageSelect.class);
        String pageVoCssQuery = (pageVoSelect != null && pageVoSelect.cssQuery() != null && pageVoSelect.cssQuery().trim().length() > 0) ? pageVoSelect.cssQuery() : "html";

        // pagevo document 2 object
        Elements pageVoElements = html.select(pageVoCssQuery);

        if (pageVoElements != null && pageVoElements.hasText()) {
            for (Element pageVoElement : pageVoElements) {

                Object pageVo = pageVoClassType.newInstance();

                Field[] fields = pageVoClassType.getDeclaredFields();
                if (fields != null) {
                    for (Field field : fields) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            continue;
                        }


                        // field origin value
                        PageFieldSelect fieldSelect = field.getAnnotation(PageFieldSelect.class);
                        String cssQuery = null;
                        String selectType = null;
                        String selectVal = null;
                        String datePattern = null;
                        if (fieldSelect != null) {
                            cssQuery = fieldSelect.cssQuery();
                            selectType = fieldSelect.selectType();
                            selectVal = fieldSelect.selectVal();
                            datePattern = fieldSelect.datePattern();
                        }
                        if (cssQuery == null || cssQuery.trim().length() == 0) {
                            continue;
                        }

                        // field value
                        Object fieldValue = null;

                        if (field.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType fieldGenericType = (ParameterizedType) field.getGenericType();
                            if (fieldGenericType.getRawType().equals(List.class)) {

                                //Type gtATA = fieldGenericType.getActualTypeArguments()[0];
                                Elements fieldElementList = pageVoElement.select(cssQuery);
                                if (fieldElementList != null && fieldElementList.size() > 0) {

                                    List<Object> fieldValueTmp = new ArrayList<Object>();
                                    for (Element fieldElement : fieldElementList) {

                                        String fieldElementOrigin = ElementUtil.parseElement(fieldElement, selectType, selectVal);
                                        if (fieldElementOrigin == null || fieldElementOrigin.length() == 0) {
                                            continue;
                                        }
                                        try {
                                            fieldValueTmp.add(ElementUtil.parseValue(field,datePattern, fieldElementOrigin));
                                        } catch (Exception e) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }

                                    if (fieldValueTmp.size() > 0) {
                                        fieldValue = fieldValueTmp;
                                    }
                                }
                            }
                        } else {

                            Elements fieldElements = pageVoElement.select(cssQuery);
                            String fieldValueOrigin = null;
                            if (fieldElements != null && fieldElements.size() > 0) {
                                fieldValueOrigin = ElementUtil.parseElement(fieldElements.get(0), selectType, selectVal);
                            }

                            if (fieldValueOrigin == null || fieldValueOrigin.length() == 0) {
                                continue;
                            }

                            try {
                                fieldValue = ElementUtil.parseValue(field,datePattern,fieldValueOrigin);
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }

                        if (fieldValue != null) {
                            field.setAccessible(true);
                            field.set(pageVo, fieldValue);
                        }
                    }
                }

                crawler.getRunConf().getPageParser().parse(html, pageVoElement, pageVo);
            }
        }
        return true;
    }


}