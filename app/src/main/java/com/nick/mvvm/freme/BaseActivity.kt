package com.nick.mvvm.freme


import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nick.mvvm.eventbus.IMessageEvent
import com.nick.mvvm.util.DeviceUtils
import java.util.*


/**
 * 基础BaseActivity
 * zhengz
 */

abstract class BaseActivity : AppCompatActivity(), IBindBase {

    protected val TAG: String = javaClass.simpleName

    private var mDelegate: DelegateActivity? = null


    companion object {
        var mCurrTopActivityName: String? = null
    }

    /**
     * 点击输入框外面事件，是否隐藏软键盘
     */
    open var touchHideInput: Boolean = false

    /**
     * 状态栏字体深浅
     *
     * @return
     */
    open var isStatusBarLight: Boolean = false

//    /**
//     * 状态沉浸栏
//     */
//    open var mSystemBarHelper: SystemBarTintHelper? = null
//
//    open var isStatusBarWhiteTint = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(contentLayoutResId)

        mDelegate = DelegateActivity(this)
        mDelegate?.onCreate()

//        mSystemBarHelper = SystemBarTintHelper(this)
//        if (isStatusBarLight) {
//            QMUIStatusBarUtil.setStatusBarLightMode(this)
//        } else {
//            QMUIStatusBarUtil.setStatusBarDarkMode(this)
//        }
//        if (isStatusBarWhiteTint) {
//            mSystemBarHelper?.setStatusBarTintColor(Color.WHITE)
//        }
    }

    override fun onGlobalEvent(): IMessageEvent? {
        return null
    }

    override fun onMessageEvent(): IMessageEvent? {
        return null
    }

    // 点击current View不被隐藏软键盘
    override fun initInterceptFocusViews(): ArrayList<View>? {
        return null
    }

    override fun onResume() {
        super.onResume()
        mCurrTopActivityName = TAG
    }

    override fun onDestroy() {
        mDelegate?.onDestroy()
        super.onDestroy()
    }

    override fun finish() {
        mDelegate?.finish()
        DeviceUtils.hideInputMethodManager(this, window.decorView)
        super.finish()
    }

    /**
     * 点击输入框外面事件，是否隐藏软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (touchHideInput) {
            mDelegate?.dispatchTouchEvent(ev, this)
        }
        return super.dispatchTouchEvent(ev)
    }

}
