package com.xclb.crawler.util;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//通用参数操作工具
public class ModelUtil {

    public static Map<String,Object> MapInMap(Map<String,Object> map,Map<String,Object> inMap){
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getValue() instanceof List){
                List value = (List) entry.getValue();
                value.addAll((List)inMap.get(entry.getKey()));
            }
        }
        return map;
    }


}
