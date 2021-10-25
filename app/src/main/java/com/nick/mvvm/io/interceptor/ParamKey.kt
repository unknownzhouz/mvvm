package com.nick.mvvm.io.interceptor

/**
 * 请求基础参数构建器类
 *
 * @author zhengz
 * @Date 2017/6/9 9:32
 */
/**
 * 客户端请求参数
 */
object RequestHeader {
    /**
     * 设备类型 0=ios  1=android
     */
    const val DEV_TYPE = "X-MMM-DeviceType"

    /**
     * android 版本
     */
    const val APP_VERSION = "X-MMM-Version"

    /**
     * 时间戳
     */
    const val TIMESTAMP = "X-MMM-Timestamp"

    /**
     * 签名
     */
    const val SIGN = "X-MMM-Sign"

    /**
     * UUID
     */
    const val UUID = "X-MMM-Uuid"

    /**
     * accesstoken
     */
    const val ACCESS_TOKEN = "X-MMM-AccessToken"
    //        public static final String CLIENT_ID="X-MMM-ClientId";
    /**
     * appName 应用ID
     */
    const val APP_NAME = "X-MMM-AppName"

    /**
     * appProject 应用名称
     */
    const val APP_PROJECT = "X-MMM-AppProject"
}

/**
 * 服务端响应参数
 */
object ResponseKey {
    /**
     * code
     */
    const val CODE = "status"

    /**
     * msg
     */
    const val MSG = "msg"

    /**
     * data
     */
    const val DATA = "data"
}
