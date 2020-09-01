package com.noe.rxjava.base;

/**
 * Created by 58 on 2016/7/13.
 */
public class Constants {
    public static final String HOST = "http://videoapi.easou.com/";
    public static final String DOUBAN_HOST = "https://api.douban.com/";
   //http://223.99.225.67:8080/n/api/topic?version=58&os=android&c=test&pk=com.esvideo&pn=1&ps=10&cversion=0

    //https://api.douban.com/v2/movie/top250?start=0&count=10  豆瓣电影的Top250测试连接

    public static String buildUrl(String url) {
        return new StringBuilder().append(HOST).append(url).append("?version=" + 58)
                .append("&os=" + "android").append("&c= test")
                .append("&pk=" + "com.esvideo").toString();
    }

}
