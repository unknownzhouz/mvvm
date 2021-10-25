package com.nick.mvvm.io.exception

/**
 * 自定义错误code类型:注解写法
 */
class CodeExp {
    /**
     * 客户端异常状态码
     */
    object ClientCode {
        const val UNKNOWN_ERROR = 1000000001 // 未知错误
        const val NETWORK_ERROR = 1000000002 // 网络错误
        const val RETRY_ERROR   = 1000000003 // 重试失败
        const val FORMAT_ERROR  = 1000000004 // 格式错误
        const val CANCEL_ERROR  = 1000000005 // 请求取消
    }

    /**
     * 服务端异常状态码
     */
    object ServerCode {
        const val DEFAULT               = -1000  // 默认无效code
        const val SUCCESS               = 0      // 请求成功
        const val INVALID_ACCESS_TOKEN  = 901    // access_token过期或无效，需要进行重新获取令牌
        const val INVALID_REFRESH_TOKEN = 902    // refresh_token过期，需要重新登录授权
        const val INVALID_SIGN          = 903    // sign无效
        const val INVALID_CLIENT_ID     = 904    // client_id或client_secret无效，需要强制更新APP
        const val INVALID_UUID          = 905    // uuid无效
    }


    companion object {
        /**
         * AccessToken过期
         */
        fun isExpireToken(errorCode: Int): Boolean {
            return errorCode == ServerCode.INVALID_ACCESS_TOKEN
        }

        /**
         * Token失效
         */
        fun isInvalidToken(errorCode: Int): Boolean {
            return errorCode == ServerCode.INVALID_REFRESH_TOKEN
                    || errorCode == ServerCode.INVALID_SIGN
                    || errorCode == ServerCode.INVALID_CLIENT_ID
                    || errorCode == ServerCode.INVALID_UUID
        }
    }


}