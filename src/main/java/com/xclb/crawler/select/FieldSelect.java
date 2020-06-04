package com.xclb.crawler.select;



import java.util.Date;
import java.util.List;
import java.util.Map;


public class FieldSelect {
    private String parmname;
    private String cssQuery;
    private String selectType;
    private String selectVal;
    private String datePattern;
    //level>2才使用设置参数类型，集合用List
    private String parmClass;
    //正则 注意：\需要\\
    private String regex;
    //$1
    private String template;

    public FieldSelect() {
        this.cssQuery = "";
        this.selectType = "TEXT";
        this.selectVal = "";
        this.datePattern = "yyyy-MM-dd HH:mm:ss";
        this.regex = "(.*)";
        this.template = "$1";
    }


    public FieldSelect(String parmname, String cssQuery, String selectType, String selectVal, String parmClass) {
        this.parmname = parmname;
        this.cssQuery = cssQuery;
        this.selectType = selectType;
        this.selectVal = selectVal;
        this.datePattern = "yyyy-MM-dd HH:mm:ss";
        this.parmClass = parmClass;
        this.regex = "(.*)";
        this.template = "$1";
    }

    public FieldSelect(String parmname, String cssQuery, String selectType, String selectVal, String parmClass,String regex,String template) {
        this.parmname = parmname;
        this.cssQuery = cssQuery;
        this.selectType = selectType;
        this.selectVal = selectVal;
        this.datePattern = "yyyy-MM-dd HH:mm:ss";
        this.parmClass = parmClass;
        this.regex = regex;
        this.template = template;
    }

    public String getParmname() {
        return parmname;
    }

    public void setParmname(String parmname) {
        this.parmname = parmname;
    }

    public String getCssQuery() {
        return cssQuery;
    }

    public void setCssQuery(String cssQuery) {
        this.cssQuery = cssQuery;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getSelectVal() {
        return selectVal;
    }

    public void setSelectVal(String selectVal) {
        this.selectVal = selectVal;
    }

    public Class getParmClass() {
        String name=parmClass;
        if ("String".equals(name)) {
            return String.class;
        } else if ("Byte".equals(name)) {
            return Byte.class;
        } else if ("List".equals(name)) {
            return List.class;
        }else if ("Map".equals(name)) {
            return Map.class;
        } else if ("Boolean".equals(name)) {
            return Boolean.class;
        } else if ("Short".equals(name)) {
            return Short.class;
        } else if ("Integer".equals(name)) {
            return Integer.class;
        } else if ("Long".equals(name)) {
            return Long.class;
        } else if ("Float".equals(name)) {
            return Float.class;
        } else if ("Double".equals(name)) {
            return Double.class;
        } else if ("Date".equals(name)) {
            return Date.class;
        } else {
            return String.class;
        }
    }

    public String getParmClassString(){
        return parmClass;
    }

    public void setParmClass(String parmClass) {
        this.parmClass = parmClass;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "FieldSelect{" +
                "parmname='" + parmname + '\'' +
                ", cssQuery='" + cssQuery + '\'' +
                ", selectType='" + selectType + '\'' +
                ", selectVal='" + selectVal + '\'' +
                '}';
    }


}
