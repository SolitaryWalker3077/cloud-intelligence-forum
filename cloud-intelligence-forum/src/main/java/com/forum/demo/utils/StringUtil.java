package com.forum.demo.utils;

/*
* 字符串工具类
* 判断字符串是否为空
* */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
