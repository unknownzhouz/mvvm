package com.nick.mvvm.io.request

import com.nick.mvvm.io.M3SDK

/**
 * DELETE
 */
class Delete<T> @JvmOverloads constructor(url: String, tClass: Class<T>? = null) : Request<T>(url, tClass) {

    override suspend fun sendBody(): String {
        val responseBody = M3SDK.call().delete(url)
        return responseBody.string()

    }


}