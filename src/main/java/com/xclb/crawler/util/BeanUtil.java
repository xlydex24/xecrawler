package com.xclb.crawler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtil {
    public static void setProperty(Map<String, Object> map, String name, Object pageVomodel) {
        List<String> list = StringUtil.split(name, "\\.");

        String key = list.get(list.size() - 1);
        Map<String, Object> model = new HashMap<>();
        model.put(key, pageVomodel);

        for (int i = list.size() - 2; i >= 0; i--) {
            Map<String, Object> itemVo = new HashMap<>();
            if (i==0){
                itemVo=map;
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
}
