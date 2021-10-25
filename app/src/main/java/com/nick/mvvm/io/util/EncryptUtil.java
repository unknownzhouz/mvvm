package com.nick.mvvm.io.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author : zhengz
 * time   : 2018/6/23
 * desc   :
 */
public class EncryptUtil {


    public static String sha256Encrypt(String strSrc) {
        MessageDigest md;
        String strDes;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        int length = bts.length;
        for (int i = 0; i < length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }


    public static String sign(String timestamp, String uuid, String token) {
        return sha256Encrypt(new StringBuilder("v!(aaaaaaaaaaa")
                .append(timestamp)
                .append(uuid)
                .append(token)
                .append("bbbbbbbbbbbbb)i^").toString());
    }


}
