package com.nick.mvvm.freme;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.nick.mvvm.eventbus.IMessageEvent;
import com.nick.mvvm.util.DeviceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * author : zhengz
 * time   : 2018/6/29
 * desc   : 事件代理
 */
public class DelegateActivity {

    /**
     * EventBus 全局事件
     */
    private IMessageEvent mGlobalEvent;

    /**
     * EventBus 业务事件
     */
    private IMessageEvent mMessageEvent;


    /**
     * 点击current View不被隐藏软键盘
     */
    private ArrayList<View> interceptFocusViews;

    /**
     * 内容绑定回调
     */
    private IBindBase mCMCallback;

    public DelegateActivity(@NonNull IBindBase bcCallback) {
        this.mCMCallback = bcCallback;
    }

    public void onCreate() {
        registerEventHandler();
        loadInterceptFocusViews();
    }

    public void onDestroy() {
        unregisterEventHandler();
    }

    /**
     * {@link Activity#finish()}
     */
    public void finish() {
        unregisterEventHandler();
    }


    public void loadInterceptFocusViews() {
        interceptFocusViews = mCMCallback.initInterceptFocusViews();
    }

    /**
     * 注册EventBus事件
     */
    private void registerEventHandler() {
        // 防止多次注册
        unregisterEventHandler();

        mMessageEvent = mCMCallback.onMessageEvent();
        if (mMessageEvent != null) {
            EventBus.getDefault().register(mMessageEvent);
        }
        mGlobalEvent = mCMCallback.onGlobalEvent();
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


    /**
     * 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
     *
     * @param ev
     * @param activity
     */
    void dispatchTouchEvent(MotionEvent ev, Activity activity) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isRange(ev)) {
                return;
            }

            // 点击坐标，是否已经不在当前焦点控件了
            View view = activity.getCurrentFocus();
            if (view instanceof EditText && !isRange(view, ev)) {
                DeviceUtils.hideInputMethodManager(activity, activity.getWindow().getDecorView());
            }
        }
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param event
     * @return
     */
    public boolean isRange(MotionEvent event) {
        if (null == interceptFocusViews || interceptFocusViews.size() == 0) {
            return false;
        }
        boolean isRange;
        for (View view : interceptFocusViews) {
            isRange = isRange(view, event);
            if (isRange) {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param view
     * @param event
     * @return true 坐标在view范围内，反之在view范围外
     */
    public boolean isRange(View view, MotionEvent event) {
        int location[] = {0, 0};
        view.getLocationInWindow(location);
        int left = location[0];
        int top = location[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        return event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom;
    }


}