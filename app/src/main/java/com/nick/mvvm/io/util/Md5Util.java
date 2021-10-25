package com.nick.mvvm.io.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * MD5加密
 */
public class Md5Util {

    /***
     * 以大写形式生成MD5串
     *
     * @param cleartext 明文
     * @return
     */
    public static String genUpperCase(String cleartext) {
        return gen(cleartext).toUpperCase(Locale.US);
    }

    /**
     * 生成MD5串
     *
     * @param cleartext 明文
     * @return
     */
    public static String gen(String cleartext) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        md.update(cleartext.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format(Locale.US, "%02x", b & 0xff));
        }
        return sb.toString();
    }
}
