package com.rizadwi.snapsift.common.wrapper

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val payload: T) : Result<T>()
    data class Failure(val cause: Throwable) : Result<Nothing>()
}