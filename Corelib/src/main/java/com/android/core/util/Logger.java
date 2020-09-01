package com.android.core.util;

import android.util.Log;

import com.android.core.BuildConfig;

import java.util.Locale;


public class Logger {

    public static final String TAG="Logger";
    /**
     * 是否允许输出日志
     */
    public static boolean loggable = BuildConfig.DEBUG;

    public static void v(String tag, String msg) {
        if (loggable) {
            Log.v(tag, buildMessage(msg));
        }
    }

    public static void d(String tag, String msg) {
        if (loggable) {
            Log.d(tag, buildMessage(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (loggable) {
            Log.i(tag, buildMessage(msg));
        }
    }

    /**
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (loggable) {
            Log.w(tag, buildMessage(msg));
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (loggable && throwable != null) {
            Log.e(tag, throwable.getMessage(), throwable);
        }
    }

    public static void e(Throwable throwable) {
        if (loggable && throwable != null) {
            throwable.printStackTrace();
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (loggable && throwable != null) {
            Log.e(tag, msg, throwable);
        }
    }

    /**
     * 长日志，以便显示全部的信息
     *
     * @param tag
     * @param tempData 日志内容
     */
    public static void longLog(String tag, String tempData) {
        if (!loggable) {
            return;
        }

        tempData = "Logcat: " + tempData;
        final int len = tempData.length();
        final int div = 2000;
        int count = len / div;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Log.i(tag, tempData.substring(i * div, (i + 1) * div));
            }
            int mode = len % div;
            if (mode > 0) {
                Log.i(tag, tempData.substring(div * count, len));
            }
        } else {
            Log.i(tag, tempData);
        }
    }

    public static String buildMessage(String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length < 5) {
            return msg;
        }

        int index = 4;
        StackTraceElement element = stackTrace[index];
        String className = element.getFileName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");

        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), stringBuilder.toString(), msg);
    }
}
