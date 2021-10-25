package com.nick.mvvm.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 基础 ViewModel
 */
open class BaseViewModel : ViewModel() {

    /**
     * 显示加载进度
     */
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 文本信息显示
     */
    val message: MutableLiveData<String> = MutableLiveData()

}