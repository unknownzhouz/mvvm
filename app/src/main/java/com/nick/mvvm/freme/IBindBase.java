package com.nick.mvvm.freme;

import android.view.View;

import androidx.annotation.NonNull;

import com.nick.mvvm.eventbus.IMessageEvent;

import java.util.ArrayList;

/**
 * author : zhengz
 * time   : 2018/6/29
 * desc   : 内容绑定
 */
public interface IBindBase {
//    /**
//     * 布局资源id
//     *
//     * @return
//     */
//    @NonNull
//    int getContentLayoutResId();


    /**
     * EventBus事件回调
     *
     * @return
     */
    IMessageEvent onMessageEvent();

    /**
     * EventBus全局事件回调
     *
     * @return
     */
    IMessageEvent onGlobalEvent();


    /**
     * 点击View时，不会隐藏软键盘
     *
     * @return
     */
    ArrayList<View> initInterceptFocusViews();

}
