package com.forum.demo.utils;


import org.apache.commons.codec.digest.DigestUtils;

import javax.lang.model.element.NestingKind;

/*
* Md5加密
* */
public class Md5Utils {

    /*
    * 普通md5加密
    * str原始字符串
    * 返回一次md5加密后的密文
    * */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    /*
    *  原始字符串与key组合进⾏⼀次MD5加密
    *   return 组合字符串⼀次MD5加密后的密⽂
    * */
    public static String md5 (String str,String key) {
        return DigestUtils.md5Hex(str+key);
    }

    /**
     * 原始字符串加密后与扰动字符串组合再进⾏⼀次MD5加密
     *  str 原始字符串
     *  salt 扰动字符串 盐值
     *  return 加密后的密⽂
     */

    public static String md5Salt (String str , String salt) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(str) + salt);
    }

    /**
     * 校验原⽂与盐加密后是否与传⼊的密⽂相同
     *  original 原字符串
     *  salt 扰动字符串
     *  ciphertext 密⽂
     * return true 相同, false 不同
     */
    public static boolean verifyOriginalAndCiphertext (String original,String salt ,String ciphertext) {
        String md5text = md5Salt(original,salt);
        if(md5text.equalsIgnoreCase(ciphertext)) {
            return true;
        }
        return false;
    }
}
