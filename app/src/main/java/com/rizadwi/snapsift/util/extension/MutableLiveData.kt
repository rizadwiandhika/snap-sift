package com.rizadwi.snapsift.util.extension

import androidx.lifecycle.MutableLiveData
import com.rizadwi.snapsift.common.wrapper.UIState

fun <T : Any> MutableLiveData<UIState<T>>.postLoading() {
    this.postValue(UIState.Pending)
}

fun <T : Any> MutableLiveData<UIState<T>>.postSuccess(data: T) {
    this.postValue(UIState.Success(data))
}

fun <T : Any> MutableLiveData<UIState<T>>.postError(err: Throwable) {
    this.postValue(UIState.Failure(err))
}