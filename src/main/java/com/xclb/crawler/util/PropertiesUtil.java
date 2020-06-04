package com.xclb.crawler.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties getProp(String basePath) {

        Properties prop = new Properties();
        try {
            InputStream in = null;
            File file = new File(basePath);
            if (file.exists()) {
                in = new BufferedInputStream(new FileInputStream(file));
            } else {
                ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
                in = classLoader.getResource(basePath).openStream();
            }
            prop.load(in);
        } catch (FileNotFoundException e) {
            System.out.println("properties文件路径书写有误，请检查！");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static Map<String, String> getProperties(String basePath) {
        Properties prop = getProp(basePath);
        Map<String, String> properties = new HashMap<String, String>((Map) prop);
        return properties;
    }

    public static String getProperty(Properties prop, String key, String defaultValue) {
        String property = prop.getProperty(key);
        if (property == null || property.trim().equals("")) {
            property = defaultValue;
        }
        return property;
    }


}
