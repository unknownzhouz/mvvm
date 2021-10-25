package com.nick.mvvm.ui

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nick.mvvm.io.State
import com.nick.mvvm.repository.CommonRepository
import com.nick.mvvm.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 *  created by  on 2021/8/2 0002
 */
class MainViewModel : BaseViewModel() {

    /**
     * 不同的写法作用
     */
    fun requestVersion() {
        viewModelScope.launch {
            CommonRepository.requestCheckVersion(1000L, 1)
                .onStart {
                    loading.value = true
                }
                .collect {
                    loading.value = false
                    when (it) {
                        is State.Success -> {
                            message.value = it.message
                        }
                        is State.Error -> {
                            message.value = it.message
                        }
                    }
                }
        }
    }

    /**
     * 直接返回 LiveData
     */
    fun requestVersion2() = CommonRepository.requestCheckVersion(1000L, 1).asLiveData()


}