package com.nick.mvvm.io.interceptor

import android.util.Log
import com.nick.mvvm.io.M3SDK
import com.nick.mvvm.io.exception.SuccessException
import com.nick.mvvm.io.util.EncryptUtil
import com.nick.mvvm.util.Log3m
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import java.util.*

/**
 * zhengz
 * 200 （成功） 服务器已成功处理了请求。 通常，这表示服务器提供了请求的网页。
 * 201 （已创建） 请求成功并且服务器创建了新的资源。
 * 202 （已接受） 服务器已接受请求，但尚未处理。
 * 203 （非授权信息） 服务器已成功处理了请求，但返回的信息可能来自另一来源。
 * 204 （无内容） 服务器成功处理了请求，但没有返回任何内容。
 * 205 （重置内容） 服务器成功处理了请求，但没有返回任何内容。
 * 206 （部分内容） 服务器成功处理了部分 GET 请求。
 */
class RetryInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val headers = buildHeader()
        // 重建请求数据 -> 添加头部参数
        val retryRequest = chain.request()
            .newBuilder()
            .headers(headers)
            .build()


        val startTime = System.currentTimeMillis()
        val response: Response = chain.proceed(retryRequest)
        if (response.code in 201..206) {
            throw SuccessException()
        }

        if (Log3m.DEBUG) {
//            val sTime = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date(startTime)) // 2020-06-30 11:00:26.401
            val duration = System.currentTimeMillis() - startTime
            // body.string() 调用一次后就会close，后续要继续传递，必须重建Response
            val content = response.body?.string() ?: return response
            Log.d(TAG, "************************************************************************************************************")
            Log.e(TAG, "----------- Start ------------------")
            Log.d(TAG, "| Request:{method=" + retryRequest.method + ", url=" + retryRequest.url.toString() + "}")
            Log.d(TAG, "| request headers:{${headers.toString().replace("[\r\n]".toRegex(), ", ")}}")
            Log.d(TAG, "| request params:${bodyToString(retryRequest.body)}")
            Log.d(TAG, "| Response:$content")
            Log.e(TAG, "----------- End:${duration}ms -----------")
            Log.d(TAG, "************************************************************************************************************")

            return rebuildResponse(response, content)
        }
        return response
    }


    companion object {
        const val TAG = "TL"

        /**
         * 重建响应内容
         */
        fun rebuildResponse(response: Response, content: String): Response {
            val mediaType = response.body?.contentType() ?: "application/json;charset=utf-8".toMediaType()
            return response.newBuilder()
                .body(content.toResponseBody(mediaType))
                .build()
        }

        /**
         * 构建头部参数
         */
        fun buildHeader(): Headers {
            val builder = Headers.Builder()
            // 头部文件

            val timestamp = System.currentTimeMillis()
            val uuid = UUID.randomUUID().toString()
            val token = "accessToken"
            val sign = EncryptUtil.sign(timestamp.toString(), uuid, token)
            builder.add(RequestHeader.ACCESS_TOKEN, token)
            builder.add(RequestHeader.SIGN, sign)
            builder.add(RequestHeader.TIMESTAMP, timestamp.toString())
            builder.add(RequestHeader.UUID, uuid)
            builder.add(RequestHeader.DEV_TYPE, 1.toString())
            val config = M3SDK.INSTANCE.config
            if (null != config) {
                builder.add(RequestHeader.APP_VERSION, config.versionName)
                builder.add(RequestHeader.APP_NAME, config.applicationId)
//                builder.add(RequestHeader.APP_PROJECT, "ability")
            }
            return builder.build()
        }

    }


    private fun bodyToString(request: RequestBody?): String {
        request ?: return "Empty RequestBody"
        try {
            val buffer = Buffer()
            request.writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: Exception) {

        }
        return "Empty RequestBody"
    }
}