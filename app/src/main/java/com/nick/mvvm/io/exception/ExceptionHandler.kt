package com.nick.mvvm.io.exception

import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.nick.mvvm.io.exception.CodeExp
import java.io.EOFException
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.*
import java.text.ParseException
import kotlin.coroutines.cancellation.CancellationException

/**
 *  created by cwzqf on 2021/6/16 0016
 *  异常显示加工
 */
object ExceptionHandler {

    fun handleException(e: Throwable?): ApiException {
        return when (e) {
            is EOFException,
            is ConnectException,
            is SocketException,
            is SocketTimeoutException,
            is UnknownHostException,
            is BindException,
            is HttpRetryException,
            is SuccessException -> {
                ApiException(e, CodeExp.ClientCode.NETWORK_ERROR, "连接超时")
            }
            is JsonParseException,
            is JsonSyntaxException,
            is JsonIOException,
            is IOException,
            is ParseException, // ParseException 解析异常（如Data格式化）
            is UnsupportedEncodingException -> {
                ApiException(e, CodeExp.ClientCode.FORMAT_ERROR, "格式错误")
            }
            is RetryOverTimeException -> {
                ApiException(e, CodeExp.ClientCode.RETRY_ERROR, "尝试重连" + e.retryTimes + "次失败")
            }
            is CancellationException -> {
                ApiException(e, CodeExp.ClientCode.CANCEL_ERROR, "请求取消")
            }
            else -> {
                ApiException(e, CodeExp.ClientCode.UNKNOWN_ERROR, "未知错误")
            }
        }

    }
}