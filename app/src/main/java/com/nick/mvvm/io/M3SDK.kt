package com.nick.mvvm.io

import android.text.TextUtils
import com.nick.mvvm.io.interceptor.RetryInterceptor
import com.nick.mvvm.io.interceptor.TokenInterceptor
import com.nick.mvvm.io.request.Callback
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

enum class M3SDK {
    INSTANCE;

    private var call: Callback? = null

    /**
     * 配置信息
     *
     * @return
     */
    var config: Config? = null
        private set

    fun init(config: Config?) {
        this.config = config
    }

    // 基础地址
    val baseUrl: String
        get() = config?.baseUrl ?: ""


    /**
     * 主Application 参数
     */
    class Config(
        var applicationId: String,    // application id
        var versionCode: Int,         // 版本号
        var versionName: String,      // 版本号名称
        var baseUrl: String,          // api地址
    )

    companion object {
        fun call(): Callback {
            val baseUrl = INSTANCE.baseUrl
            if (TextUtils.isEmpty(baseUrl)) {
                throw NullPointerException("网络库地址未设置")
            }
            if (null == INSTANCE.call) {
                INSTANCE.call = Retrofit.Builder()
                    .baseUrl(baseUrl) // 解析器是由添加的顺序分别试用的，解析成功就直接返回，失败则调用下一个解析器
                    //            .addConverterFactory(ProtoConverterFactory.create())
                    //            .addConverterFactory(StringConverterFactory.create())
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(TokenInterceptor())
                            .addInterceptor(RetryInterceptor()) // 【重点】 RetryInterceptor 拦截放在最后，才能每次重建
                            .writeTimeout(10000, TimeUnit.MILLISECONDS) // 写入超时 (默认10秒)
                            .readTimeout(20000, TimeUnit.MILLISECONDS) // 读取超时
                            .connectTimeout(10000, TimeUnit.MILLISECONDS) // 连接超时
                            .build()
                    )
                    .build()
                    .create(Callback::class.java)
            }
            return INSTANCE.call!!
        }
    }
}