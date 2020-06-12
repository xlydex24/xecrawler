package com.xclb.crawler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtil {
    public static void inProperty(Map<String, Object> map, String name, Object pageVomodel) {
        List<String> list = StringUtil.split(name, "\\.");

        String key = list.get(list.size() - 1);
        Map<String, Object> model = new HashMap<>();
        model.put(key, pageVomodel);

        for (int i = list.size() - 2; i >= 0; i--) {
            Map<String, Object> itemVo = new HashMap<>();
            if (i == 0) {
                itemVo = map;
            }
            key = list.get(i);
            if (key.contains("[")) {
                int index = key.indexOf("[");
                key = key.substring(0, index);
                List<Map<String, Object>> maplist = new ArrayList<>();
                maplist.add(model);
                itemVo.put(key, maplist);
            } else {
                itemVo.put(key, model);
            }
            model = itemVo;
        }
    }

    public static void setProperty(Map<String, Object> map, String name, Object pageVomodel) {
        List<String> list = StringUtil.split(name, "\\.");

        Map<String, Object> model = map;
        for (int i = 0; i < list.size() - 1; i++) {
            String key = list.get(i);
            if (key.contains("[")) {
                int index = key.indexOf("[");
                String num = ReUtil.extractMulti("\\[(\\d+)\\]", key, "$1");
                key = key.substring(0, index);
                List<Map<String, Object>> o = (List<Map<String, Object>>) model.get(key);
                Map<String, Object> item = new HashMap<>();
                if (o == null) {
                    o = new ArrayList<>();
                }
                if (num != null && num.length() > 0) {
                    Integer j = Integer.valueOf(num);
                    if (o.size() > j) {
                        item = o.get(j);
                    } else {
                        o.add(item);
                    }
                } else {
                    o.add(item);
                }
                model.put(key, o);
                model = item;
            } else {
                Map<String, Object> o = (Map<String, Object>) model.get(key);
                if (o == null) {
                    Map<String, Object> item = new HashMap<>();
                    model.put(list.get(i), item);
                    model = item;
                } else {
                    model = o;
                }
            }

        }
        model.put(list.get(list.size() - 1), pageVomodel);


    }
}
