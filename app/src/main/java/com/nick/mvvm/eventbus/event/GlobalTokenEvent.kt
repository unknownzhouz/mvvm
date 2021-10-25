package com.nick.mvvm.eventbus.event

/**
 * Token事件（全局）
 */
class GlobalTokenEvent(val tokenType: Int = TOKEN_INVALID_POSITIVE) {

    companion object {
        /**
         * Token失效，主动失效
         * 如.主动退出账户等行为
         */
        const val TOKEN_INVALID_POSITIVE = 0

        /**
         * Token失效，被动失效
         * 如.服务端返回Token无效
         */
        const val TOKEN_INVALID_NEGATIVE = 1

        /**
         * Token有效，
         * 如.登录成功
         */
        const val TOKEN_VALID_LOGIN = 2
    }
}