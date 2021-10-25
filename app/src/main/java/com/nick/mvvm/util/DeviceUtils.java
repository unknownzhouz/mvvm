package com.nick.mvvm.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by zhengz on 2017/8/30.
 */

public class DeviceUtils {

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public static void hideInputMethodManager(Context context, View view) {
        if (null == view || null == context) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     *
     * @param view
     */
    public static void showInputMethodManager(Context context, View view) {
        if (null == view || null == context) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

}
