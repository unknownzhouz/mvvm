package com.nick.mvvm.util;

import android.util.Log;


public class Log3m {
    private static final String TAG = "M3Log";

    public static boolean DEBUG = true;


    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, buildMessage(msg));
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, buildMessage(msg));
        }
    }


    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, buildMessage(msg));
        }

    }

    public static void e(String msg, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, buildMessage(msg), e);
        }
    }


    public static void v(String format) {
        if (DEBUG) {
            Log.v(TAG, buildMessage(format));
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, buildMessage(msg));
        }
    }

    public static void w(String msg, Throwable e) {
        if (DEBUG) {
            Log.w(TAG, buildMessage(msg), e);
        }
    }

    /**
     * Building Message
     *
     * @param msg The message you would like logged.
     * @return Message String
     */
    private static String buildMessage(String msg) {
        return new StringBuilder()
                .append("HTTPS")
                .append(" >>> ")
                .append(msg).toString();
    }
}

