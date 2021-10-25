package com.nick.mvvm.io.request

import com.nick.mvvm.io.M3SDK
import com.nick.mvvm.util.GsonUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * POST
 */
class Post<T> @JvmOverloads constructor(url: String, tClass: Class<T>? = null) : Request<T>(url, tClass) {

    override suspend fun sendBody(): String {
        val requestContent: String = GsonUtil.getGson().toJson(params)
        val requestBody = requestContent.toRequestBody("application/json;charset=utf-8".toMediaType())
        val responseBody = M3SDK.call().post(url, requestBody)
        return responseBody.string()
    }
}