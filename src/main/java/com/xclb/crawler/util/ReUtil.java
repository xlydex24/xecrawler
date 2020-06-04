package com.xclb.crawler.util;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReUtil {
    /** 分组 */
    public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
    /**
     * 从content中匹配出多个值并根据template生成新的字符串<br>
     * 匹配结束后会删除匹配内容之前的内容（包括匹配内容）<br>
     * 例如：<br>
     * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
     *
     * @param regex    匹配正则字符串
     * @param content  被匹配的内容
     * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
     * @return 按照template拼接后的字符串
     */
    public static String extractMulti(String regex, CharSequence content, String template) {
        if (null == content || null == regex || null == template) {
            return null;
        }

        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return extractMulti(pattern, content, template);
    }
    /**
     * 从content中匹配出多个值并根据template生成新的字符串<br>
     * 例如：<br>
     * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
     *
     * @param pattern 匹配正则
     * @param content 被匹配的内容
     * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
     * @return 新字符串
     */
    public static String extractMulti(Pattern pattern, CharSequence content, String template) {
        if (null == content || null == pattern || null == template) {
            return null;
        }

        TreeSet<Integer> varNums = new TreeSet<>();
        Matcher matcherForTemplate = GROUP_VAR.matcher(template);
        while (matcherForTemplate.find()) {
            String group = matcherForTemplate.group(1);
            int i = Integer.parseInt(group);
            varNums.add(i);
        }

        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            for (Integer group : varNums) {
                template = template.replace("$" + group, matcher.group(group));
            }
            return template;
        }
        return null;
    }

    /**
     * 正则匹配
     * @param regex	: 正则表达式
     * @param str	: 待匹配字符串
     * @return boolean
     */
    public static boolean matches(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private static final String URL_REGEX = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * url格式校验
     *
     * @param str
     * @return boolean
     */
    public static boolean isUrl(String str) {
        if (str==null || str.trim().length()==0) {
            return false;
        }
        return matches(URL_REGEX, str);
    }

}
