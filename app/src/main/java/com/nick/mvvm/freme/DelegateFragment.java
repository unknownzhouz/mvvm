package com.nick.mvvm.freme;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.nick.mvvm.eventbus.IMessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * author : zhengz
 * time   : 2018/6/29
 * desc   :
 */
public class DelegateFragment {

    /**
     * EventBus 全局事件
     */
    private IMessageEvent mGlobalEvent;

    /**
     * EventBus 业务事件
     */
    private IMessageEvent mMessageEvent;


    private IBindBase mContentCallback;

    public DelegateFragment(@NonNull IBindBase bcCallback) {
        this.mContentCallback = bcCallback;
    }


    public void onViewCreated(View view, Bundle savedState) {
        registerEventHandler();
    }

    public void onDestroy() {
        unregisterEventHandler();
    }

    /**
     * 注册EventBus事件
     */
    private void registerEventHandler() {
        // 防止多次注册
        unregisterEventHandler();

        mMessageEvent = mContentCallback.onMessageEvent();
        if (mMessageEvent != null) {
            EventBus.getDefault().register(mMessageEvent);
        }
        mGlobalEvent = mContentCallback.onGlobalEvent();
        if (mGlobalEvent != null) {
            EventBus.getDefault().register(mGlobalEvent);
        }
    }

    /**
     * 移除EventBus事件
     */
    private void unregisterEventHandler() {
        if (mMessageEvent != null) {
            EventBus.getDefault().unregister(mMessageEvent);
        }
        if (mGlobalEvent != null) {
            EventBus.getDefault().unregister(mGlobalEvent);
        }
    }
}
