package com.nick.mvvm.io

import android.text.TextUtils
import com.google.gson.JsonParser
import com.nick.mvvm.io.exception.CodeExp
import com.nick.mvvm.io.exception.ExceptionHandler
import com.nick.mvvm.io.interceptor.ResponseKey
import com.nick.mvvm.io.request.Request
import com.nick.mvvm.util.GsonUtil
import com.nick.mvvm.util.Log3m
import kotlinx.coroutines.flow.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

/***
 * 获取接口数据
 */
class FlowManager {

    companion object {

        /**
         * 单接口获取
         */
        fun <T> asFlow(request: Request<T>) = flow {
            val responseString = request.sendBody()
            val jsonObj = JsonParser.parseString(responseString).asJsonObject
            val errorCode = jsonObj[ResponseKey.CODE].asInt
            val msg = jsonObj[ResponseKey.MSG]?.asString ?: ""
            val data = jsonObj[ResponseKey.DATA]?.toString()

            if (errorCode == CodeExp.ServerCode.SUCCESS) {
                val t = if (TextUtils.isEmpty(data) || null == request.tClass) {
                    null
                } else {
                    GsonUtil.getGson().fromJson(data, request.tClass)
                }
                emit(State.onSuccess<T>(t, msg))
            } else {
                emit(State.onFailure<T>(errorCode, msg))
            }
        }.retry(request.retryCount) {
            it is ConnectException
                    || it is SocketTimeoutException
                    || it is TimeoutException
        }.catch { e ->
            // 协程异常捕获
            if (Log3m.DEBUG) {
                e.printStackTrace()
            }
            Log3m.e("协程异常捕获 " + e.message)
            val eh = ExceptionHandler.handleException(e)
            emit(State.onFailure(eh.code, eh.msg))
        }.onCompletion { t: Throwable? ->
            if (t != null && Log3m.DEBUG) {
                t.printStackTrace()
                Log3m.e("程序抛出异常，请处理 " + t.message)
            }
        }


        /**
         * 多接口并发请求，
         * Success 以HashMap存储返回的结果内容，并按顺序请求
         * Error 以首次失败的数据，作为本次整体请求的错误信息
         */
        fun asCombFlow(flows: List<Flow<State<*>>>) = flow {
            val fc = FlowComb()
            flows.forEachIndexed { index, flow ->
                flow.collect {
                    if (fc.interrupt) {
                        return@collect
                    }
                    if (it is State.Error<*>) {
                        fc.interrupt = true
                        emit(StateComb.Error(it.errorCode, it.message))
                    } else if (it is State.Success) {
                        fc.map["$index"] = it.data!!
                        if (fc.map.size == flows.size) {
                            emit(StateComb.Success(fc.map))
                        }
                    }
                }
            }
        }


    }

}


sealed class State<T> {

    data class Success<T>(val data: T?, val message: String) : State<T>() // 请求成功状态
    data class Error<T>(val errorCode: Int, val message: String) : State<T>()  // 请求异常状态

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Error

    companion object {
        fun <T> onSuccess(data: T?, message: String = "") = Success(data, message)
        fun <T> onFailure(errorCode: Int, message: String) = Error<T>(errorCode, message)
    }
}


sealed class StateComb {
    data class Success(val data: Map<String, Any>?) : StateComb()
    data class Error(val errorCode: Int, val message: String) : StateComb()
}

class FlowComb {
    var interrupt = false
    val map = HashMap<String, Any>()
}