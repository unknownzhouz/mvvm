package com.nick.mvvm.io.request

import android.util.ArrayMap
import com.nick.mvvm.io.util.Md5Util

abstract class Request<T> protected constructor(val url: String, val tClass: Class<T>? = null) {

    /**
     * 业务参数
     */
    val params: MutableMap<String, Any?> = ArrayMap()

    /**
     * 重发次数
     */
    var retryCount = RETRY_MAX_COUNT

    open fun put(key: String, value: Any?) {
        params[key] = value
    }

    open fun putAll(map: Map<String, Any>?) {
        if (null != map) {
            params.putAll(map)
        }
    }


    open fun generateKey(): String {
        val builder = StringBuilder(url)
        builder.append(params.toString())
        return Md5Util.gen(builder.toString())
    }

    abstract suspend fun sendBody(): String

    companion object {
        const val RETRY_MAX_COUNT: Long = 3  // 重试次数，默认3次
    }
}