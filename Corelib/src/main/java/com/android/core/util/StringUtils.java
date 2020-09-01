package com.android.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lijie24 on 2017/7/6.
 * String 工具类
 */

public class StringUtils {
    /**
     * 使用 MD5 算法加密字符串
     */
    public static String MD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(data.getBytes());
            return bytesToHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
        }
        return data;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 判断是否是中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        String regEx = "^[\u4e00-\u9fa5]+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        return matcher.find();
    }

    /**
     * 判断是否是英文
     * 允许空格和.
     *
     * @param str
     * @return
     */
    public static boolean isEnglish(String str) {
        String regEx = "^[a-zA-Z.\\s]+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        return matcher.find();
    }

    /**
     * 字符串拼接
     */
    public static String join(Iterable<?> iterable, String separator) {
        return iterable == null ? null : join(iterable.iterator(), separator);
    }

    private static String join(Iterator<?> iterator, String separator) {
        if (iterator == null || !iterator.hasNext()) {
            return null;
        }

        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return toString(first);
        } else {
            StringBuilder buf = new StringBuilder();
            if (first != null) {
                buf.append(first);
            }

            while (iterator.hasNext()) {
                if (separator != null) {
                    buf.append(separator);
                }

                Object obj = iterator.next();
                if (obj != null) {
                    buf.append(obj);
                }
            }

            return toString(buf);
        }
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
