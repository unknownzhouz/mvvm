package com.nick.mvvm.io.interceptor

import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import com.google.gson.JsonParser
import com.nick.mvvm.io.exception.CodeExp
import com.nick.mvvm.util.Log3m
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.locks.ReentrantLock


class TokenInterceptor : Interceptor {
    private val lock = ReentrantLock()

//    var count = 2;

    /**
     * 更新Token有效时间
     */
    private var updateTimeWithToken = 0L

    override fun intercept(chain: Interceptor.Chain): Response {
        // RetryInterceptor 拦截请求响应
        val response = chain.proceed(chain.request())

        // ResponseBody
        val responseBody = response.body ?: return response

        // responseBody.string() 调用一次后就会close，后续要继续传递，必须重建 Response
        val responseContent = responseBody.string()

        // String转Json对象
        val jsonObject = JsonParser.parseString(responseContent).asJsonObject
        // 这里需要根据各自业务解析数据参数,
        // 获取天气内容
//        {
//            "status":"0",
//            "msg":"预警信息。",
//            "city":"长春",
//            "weathercode":"101060101",
//            "update":"202110251100",
//            "data":[
//            ]
//        }

//        val errorCode = jsonObject[ResponseKey.CODE].asInt
        val errorCode = 0
        if (CodeExp.isInvalidToken(errorCode)) {
            clearTokenNotify()
            return RetryInterceptor.rebuildResponse(response, responseContent) //  重建Response
        }

//        count++
//        if (chain.request().url.toString() == "https://suplus-worth-app-gateway-prd.fjmaimaimai.com/v2/user/userInfo") {
//            if (count % 3 == 0) {
//                errorCode = CodeExp.ServerCode.INVALID_ACCESS_TOKEN
//                updateTimeWithToken = 0L
//                Log.e("同步操作", "过期了~~~ $count ")
//            }
//        }

        // 刷新Token
        var isRefreshSuccess = false
        if (CodeExp.isExpireToken(errorCode)) {
            lock.lock()
            try {
                val refreshToken = "refreshToken"
                if (SystemClock.elapsedRealtime() - updateTimeWithToken > 10_000 && !TextUtils.isEmpty(refreshToken)) {
                    isRefreshSuccess = fetchRefreshToken()
                }
            } finally {
                lock.unlock()
            }
        }

        return if (isRefreshSuccess) {
            rebuildRequest(chain)  //  重建Request
        } else {
            RetryInterceptor.rebuildResponse(response, responseContent) //  重建Response
        }
    }


    /**
     * 重建请求内容
     */
    private fun rebuildRequest(chain: Interceptor.Chain): Response {
        val retryRequest = chain.request().newBuilder().build()
        return chain.proceed(retryRequest)
    }

//    /**
//     * 更新Token
//     */
//    private fun updateTokenNotify(wrapper: AccessTokenWrapper?) {
//        TokenController.saveAccountInfo(wrapper?.access)
//    }

    /**
     * 退出登录并通知
     */
    private fun clearTokenNotify() {
        if (Log3m.DEBUG) {
            Log.e(RetryInterceptor.TAG, "退出登录授权")
        }
        updateTimeWithToken = 0L
//        // 清空Token
//        TokenController.clean()
//        // 通知退出登录
//        EventBus.getDefault().post(GlobalTokenEvent(GlobalTokenEvent.TOKEN_INVALID_NEGATIVE))
    }

    /**
     * 刷新Token
     */
    private fun fetchRefreshToken(): Boolean {
//        val builder = Request.Builder()
//        // 接口地址
//        builder.url(ApiSystem.transform(ApiSystem.ACTION_REFRESH_TOKEN))
//        builder.headers(RetryInterceptor.buildHeader())
//
//        // 添加内容
//        val params = HashMap<String, String>()
//        params["refreshToken"] = TokenController.getRefreshToken()
//        val requestContent: String = GsonUtil.getGson().toJson(params)
//        val requestBody = requestContent.toRequestBody("application/json;charset=utf-8".toMediaType())
//        builder.post(requestBody)
//
//        val client = OkHttpClient()
//        val call = client.newCall(builder.build())
//        try {
//            val responseBody = call.execute()
//            val responseString = responseBody.body?.string()
//
//            val jsonObj = JsonParser.parseString(responseString).asJsonObject
//            val errorCode = jsonObj[ResponseKey.CODE].asInt
//            val data = jsonObj[ResponseKey.DATA]?.toString()
//
//            if (errorCode == CodeExp.ServerCode.SUCCESS) {
//                updateTokenNotify(GsonUtil.getGson().fromJson(data, AccessTokenWrapper::class.java))
//                updateTimeWithToken = SystemClock.elapsedRealtime() // 更新刷新时间
//                return true
//            } else {
//                clearTokenNotify()
//            }
//        } catch (e: Exception) {
//            clearTokenNotify()
//            if (Log3m.DEBUG) {
//                e.printStackTrace()
//            }
//        }
        return false
    }

}