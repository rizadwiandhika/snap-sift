package com.rizadwi.snapsift.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.common.wrapper.UIState
import com.rizadwi.snapsift.model.Source
import com.rizadwi.snapsift.usecase.GetAllSourcesUseCase
import com.rizadwi.snapsift.usecase.GetCategoriesUseCase
import com.rizadwi.snapsift.usecase.GetSourcesByCategory
import com.rizadwi.snapsift.util.extension.postError
import com.rizadwi.snapsift.util.extension.postLoading
import com.rizadwi.snapsift.util.extension.postSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val getAllSourcesUseCase: GetAllSourcesUseCase,
    private val getSourcesByCategory: GetSourcesByCategory,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _categoryListLiveData = MutableLiveData<UIState<List<String>>>()
    private val _sourceListLiveData = MutableLiveData<UIState<List<Source>>>()

    val sourceListLiveData: LiveData<UIState<List<Source>>> = _sourceListLiveData
    val categoryListLiveData: LiveData<UIState<List<String>>> = _categoryListLiveData

    fun getCategories() = viewModelScope.launch {
        _categoryListLiveData.postLoading()
        when (val it = getCategoriesUseCase.invoke()) {
            is Result.Failure -> _categoryListLiveData.postError(it.cause)
            is Result.Success -> _categoryListLiveData.postSuccess(it.payload)
        }
    }

    fun getAllSources() = viewModelScope.launch {
        _sourceListLiveData.postLoading()
        when (val it = getAllSourcesUseCase.invoke()) {
            is Result.Failure -> _sourceListLiveData.postError(it.cause)
            is Result.Success -> _sourceListLiveData.postSuccess(it.payload)
        }
    }

    fun getSourcesByCategory(category: String) = viewModelScope.launch {
        if (category == "all") {
            getAllSources()
            return@launch
        }

        _sourceListLiveData.postLoading()
        when (val it = getSourcesByCategory.invoke(category)) {
            is Result.Failure -> _sourceListLiveData.postError(it.cause)
            is Result.Success -> _sourceListLiveData.postSuccess(it.payload)
        }
    }
}