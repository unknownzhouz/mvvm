package com.nick.mvvm.ui.base


import android.app.Dialog
import com.nick.mvvm.freme.BaseActivity
import com.nick.mvvm.widget.dialog.CircleAlertDialog

abstract class BusinessActivity : BaseActivity() {

    private var mWaitDialog: Dialog? = null

//    var mFloatingProxy: FloatingProxy? = null

    override var isStatusBarLight = true

//    /**
//     * 是否显示有话说
//     *
//     * @return
//     */
//    open var isFloatDragger: Boolean = true

//    open var isMockServe: Boolean = false


//    override fun setContentView(layoutResID: Int) {
//        if (isFloatDragger) {
//            val contentView = LayoutInflater.from(this).inflate(layoutResID, null)
//            mFloatingProxy = FloatingProxy(this, contentView, isMockServe)
//            super.setContentView(mFloatingProxy?.draggedLayout)
//        } else {
//            super.setContentView(layoutResID)
//        }
//    }

//    /**
//     * 系统通知事件
//     *
//     * @return
//     */
//    override fun onGlobalEvent(): IMessageEvent {
//        return object : MessageEventImpl() {
//            @Subscribe(threadMode = ThreadMode.MAIN)
//            fun onMessageEvent(event: GlobalTokenEvent) {
//                dismissDialog()
//                if (event.tokenType == GlobalTokenEvent.TOKEN_INVALID_POSITIVE
//                    || event.tokenType == GlobalTokenEvent.TOKEN_INVALID_NEGATIVE
//                ) {
//                    AppUtil.signOut()
//                }
//            }
//
//            @Subscribe(threadMode = ThreadMode.MAIN)
//            fun onMessageEvent(event: GlobalTalkMessageEvent) {
//                mFloatingProxy?.onResume()
//            }
//
//            @Subscribe(threadMode = ThreadMode.MAIN)
//            fun onMessageEvent(event: GlobalUpdateVersionEvent?) {
//                if (TAG != mCurrTopActivityName || null == event) {
//                    return
//                }
//                if (UpdateVersion.NO_UPDATE == event.info.updateType) {
//                    return
//                }
//                val updateDialog = UpdateDialog.UpdateDialogBuilder(this@BusinessActivity)
//                    .setUpdateType(event.info.updateType)
//                    .setContent(event.info.content)
//                    .setVersion(event.info.versionNo)
//                    .build()
//                updateDialog.show()
//            }
//        }
//    }

//
//    override fun onResume() {
//        super.onResume()
//        mFloatingProxy?.onResume()
//    }
//
//    override fun onDestroy() {
//        mFloatingProxy?.onDestroy()
//        super.onDestroy()
//    }

    fun showDialog(msg: String = "") {
        if (null == mWaitDialog) {
            mWaitDialog = CircleAlertDialog(msg, false, this)
        }
        mWaitDialog?.setTitle(msg)
        mWaitDialog?.show()
    }

    fun dismissDialog() {
        mWaitDialog?.dismiss()
    }

}