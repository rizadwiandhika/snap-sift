package com.rizadwi.snapsift.common.wrapper

sealed class UIState<out T : Any> {
    data class Failure(val cause: Throwable) : UIState<Nothing>()
    data class Success<out T : Any>(val data: T) : UIState<T>()
    data object Pending : UIState<Nothing>()
}