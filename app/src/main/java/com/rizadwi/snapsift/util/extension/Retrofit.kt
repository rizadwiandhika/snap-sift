package com.rizadwi.snapsift.util.extension

import com.google.gson.Gson
import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.datasource.service.dto.response.FailureResponse
import retrofit2.Response

inline fun <reified T : Any> Response<T>.getResult(): Result<T> {
    if (isSuccessful) {
        val data: T = this.body()!!
        return Result.Success(data)
    }

    val failure = Gson().fromJson(errorBody()?.string(), FailureResponse::class.java)
    return Result.Failure(Error(failure.message))
}