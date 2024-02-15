package com.rizadwi.snapsift.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.common.wrapper.UIState
import com.rizadwi.snapsift.model.Source
import com.rizadwi.snapsift.usecase.GetCategoriesUseCase
import com.rizadwi.snapsift.usecase.GetSourcesUseCase
import com.rizadwi.snapsift.util.extension.postError
import com.rizadwi.snapsift.util.extension.postLoading
import com.rizadwi.snapsift.util.extension.postSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val getSourcesUseCase: GetSourcesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private var selectedCategory: String = "all"
    private val _categoryListLiveData = MutableLiveData<UIState<List<String>>>()
    private val _sourceListLiveData = MutableLiveData<UIState<List<Source>>>()
    private val debounceFlow = MutableSharedFlow<String>()

    private var sourceList: MutableList<Source> = mutableListOf()

    val sourceListLiveData: LiveData<UIState<List<Source>>> = _sourceListLiveData
    val categoryListLiveData: LiveData<UIState<List<String>>> = _categoryListLiveData

    init {
        viewModelScope.launch {
            debounceFlow.debounce(DELAY_DEBOUNCE_MS).collect(::filterSources)
        }
    }


    private fun filterSources(keyword: String) {
        if (keyword.isBlank()) {
            _sourceListLiveData.postSuccess(sourceList)
            return
        }

        val filteredSources = sourceList.filter { it.name.contains(keyword, true) }
        _sourceListLiveData.postSuccess(filteredSources)
    }

    fun getCategories() = viewModelScope.launch {
        _categoryListLiveData.postLoading()
        when (val it = getCategoriesUseCase.invoke()) {
            is Result.Failure -> _categoryListLiveData.postError(it.cause)
            is Result.Success -> _categoryListLiveData.postSuccess(it.payload)
        }
    }

    fun getSourcesFromCacheIfPossible() = viewModelScope.launch {
        if (selectedCategory.isNotBlank()) {
            getSourcesByCategory(selectedCategory)
        } else {
            getAllSources()
        }
    }

    private fun getAllSources() = viewModelScope.launch {
        _sourceListLiveData.postLoading()
        when (val it = getSourcesUseCase.invoke()) {
            is Result.Failure -> _sourceListLiveData.postError(it.cause)
            is Result.Success -> {
                _sourceListLiveData.postSuccess(it.payload)
                sourceList = it.payload.toMutableList()
            }
        }
    }

    fun getSourcesByCategory(category: String) = viewModelScope.launch {
        selectedCategory = category
        if (category == "all") {
            getAllSources()
            return@launch
        }

        _sourceListLiveData.postLoading()
        when (val it = getSourcesUseCase.invoke(category)) {
            is Result.Failure -> _sourceListLiveData.postError(it.cause)
            is Result.Success -> {
                _sourceListLiveData.postSuccess(it.payload)
                sourceList = it.payload.toMutableList()
            }
        }
    }

    fun prepareFilterSource(keyword: String) = viewModelScope.launch {
        debounceFlow.emit(keyword)
    }


    companion object {
        const val DELAY_DEBOUNCE_MS = 250L
    }
}