package com.nick.mvvm.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import com.nick.mvvm.R
import com.nick.mvvm.widget.materialishprogress.ProgressWheel

class CircleAlertDialog(val title: String = "", private val cancelable: Boolean = false, context: Context) : AlertDialog(context, R.style.WaitDialog) {

    init {
        initView(context)
    }

    fun initView(context: Context?) {
        // 居中处理，防止部分设备显示（华为平板出现过）不居中的Bug
        val attr = window?.attributes
        if (null != attr) {
            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attr.width = ViewGroup.LayoutParams.WRAP_CONTENT
            attr.gravity = Gravity.CENTER
        }

        show()

        setContentView(R.layout.dialog_waiting)
        setTitle(title)
        setCanceledOnTouchOutside(cancelable)
        setOnKeyListener { _, keyCode, _ ->
            !cancelable && keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    fun setTitle(title: String = "") {
        val msgText = window?.findViewById<TextView>(R.id.tvContent)
        msgText?.text = title
        if (title.isEmpty()) {
            msgText?.visibility = View.GONE
        } else {
            msgText?.visibility = View.VISIBLE
        }
    }

    fun setProgressColor(@ColorRes color: Int) {
        val progressWheel = window?.findViewById<ProgressWheel>(R.id.process_bar)
        progressWheel?.barColor = context.resources.getColor(color)
    }

}