package com.nick.mvvm

import android.app.Application
import com.nick.mvvm.io.M3SDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = M3SDK.Config(
            BuildConfig.APPLICATION_ID,
            BuildConfig.VERSION_CODE,
            BuildConfig.VERSION_NAME,
            "https://www.baidu.com"
        )
        M3SDK.INSTANCE.init(config)
    }

}