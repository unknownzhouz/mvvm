package com.nick.mvvm.repository

import com.nick.mvvm.io.FlowManager
import com.nick.mvvm.io.State
import com.nick.mvvm.io.request.Post
import kotlinx.coroutines.flow.Flow


/**
 *  created by cwzqf on 2021/8/2 0002
 */
object CommonRepository {

    /**
     * 版本更新检查
     */
    fun requestCheckVersion(versionNo: Long, channel: Int): Flow<State<Any>> {
        val request = Post(Api.ACTION_VERSION_UPDATE, Any::class.java)
        request.put("versionNo", versionNo)
        request.put("channel", channel)
        return FlowManager.asFlow(request)
    }


}