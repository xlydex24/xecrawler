package com.xclb.crawler.util;

import com.xclb.crawler.annotation.PageFieldSelect;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementUtil {
    public static String parseElement(Element fieldElement, String selectType, String selectVal) {
        String fieldElementOrigin = null;
        if ("HTML".equals(selectType)) {
            fieldElementOrigin = fieldElement.html();
        } else if ("VAL".equals(selectType)) {
            fieldElementOrigin = fieldElement.val();
        } else if ("TEXT".equals(selectType)) {
            fieldElementOrigin = fieldElement.text();
        } else if ("ATTR".equals(selectType)) {
            fieldElementOrigin = fieldElement.attr(selectVal);
        } else if ("HAS_CLASS".equals(selectType)) {
            fieldElementOrigin = String.valueOf(fieldElement.hasClass(selectVal));
        } else if ("OWN_TEXT".equals(selectType)) {
            fieldElementOrigin = fieldElement.ownText();
        } else if ("WHOLE_TEXT".equals(selectType)) {
            fieldElementOrigin = fieldElement.wholeText();
        } else if ("TOSTRING".equals(selectType)) {
            fieldElementOrigin = fieldElement.toString();
        } else {
            fieldElementOrigin = fieldElement.text();
        }

        return fieldElementOrigin;
    }

    public static String parseElement(Element fieldElement, String selectType, String selectVal, String regex, String template) {
        String fieldElementOrigin = parseElement(fieldElement, selectType, selectVal);
        if (regex == null || template == null || regex.equals("") || template.equals("")) {
            return fieldElementOrigin;
        }
        return ReUtil.extractMulti(regex, fieldElementOrigin, template);
    }

    //CrawlerThreadG转换类型用
    public static Object parseValue(Class c, String datePattern, String value, String... name) {

        Class<?> fieldType = c;

        if (value == null || value.trim().length() == 0)
            return null;
        value = value.trim();
        value = value.replaceAll("　", "");

        try {
            if (Byte.class.equals(fieldType) || Byte.TYPE.equals(fieldType)) {
                return Byte.valueOf(value);
            } else if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
                return parseBoolean(value);
            } else if (String.class.equals(fieldType)) {
                return value;
            } else if (List.class.equals(fieldType)) {
                return value;
            } else if (Map.class.equals(fieldType)) {
                return parseMap(name[0], value);
            } else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
                value = value.replaceAll(",", "");
                return Short.valueOf(value);
            } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
                value = value.replaceAll(",", "");
                return Integer.valueOf(value);
            } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
                value = value.replaceAll(",", "");
                return Long.valueOf(value);
            } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
                return Float.valueOf(value);
            } else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
                return Double.valueOf(value);
            } else if (Date.class.equals(fieldType)) {
                return parseDate(datePattern, value);
            } else {
                throw new RuntimeException("request illeagal type, type must be Integer not int Long not long etc, type=" + fieldType);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseValue but input illegal input=" + value, e);
        }
    }

    private static Map<String, String> parseMap(String name, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(name, value);
        return map;
    }

    private static Date parseDate(String date, String value) {
        try {
            String datePattern = "yyyy-MM-dd HH:mm:ss";
            if (datePattern != null) {
                datePattern = date;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("parseDate but input illegal input=" + value, e);
        }
    }

    public static Boolean parseBoolean(String value) {
        value = value.replaceAll("　", "");
        if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        } else if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        } else {
            throw new RuntimeException("parseBoolean but input illegal input=" + value);
        }
    }

    public static Object parseValue(Field field,String datePattern, String value) {
        Class<?> fieldType = field.getType();
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType fieldGenericType = (ParameterizedType) field.getGenericType();
            if (fieldGenericType.getRawType().equals(List.class)) {
                Type gtATA = fieldGenericType.getActualTypeArguments()[0];
                fieldType = (Class) gtATA;
            }
        }
        return parseValue(fieldType, datePattern, value);
    }
}
