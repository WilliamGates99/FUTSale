package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: UiText? = null
) {
    companion object {
        fun <T> success(data: T? = null) = Resource(Status.SUCCESS, data)

        fun <T> error(message: UiText, data: T? = null) = Resource(Status.ERROR, data, message)

        fun <T> loading(data: T? = null) = Resource(Status.LOADING, data)
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}