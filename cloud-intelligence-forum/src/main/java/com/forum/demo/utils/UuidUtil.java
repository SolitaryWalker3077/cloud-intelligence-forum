package com.forum.demo.utils;


import java.util.UUID;

/*
*  ⽣成UUID⼯具类
*
* */
public class UuidUtil {

    /*
    * 生成默认的UUID ,默认长度为36位
    * */
    public static String UUID_36() {
        return UUID.randomUUID().toString();
    }

    /*
    *
    * */
    public static String UUID_32() {
        return UUID.randomUUID().toString().replace("-","");
    }

}
