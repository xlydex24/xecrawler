package com.xclb.crawler.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static List<String> split (String str,String regex){
        String[] split = str.trim().split(regex);
        List<String> list = new ArrayList<>();
        for (String s : split) {
            list.add(s);
        }
        return list;
    }
}
