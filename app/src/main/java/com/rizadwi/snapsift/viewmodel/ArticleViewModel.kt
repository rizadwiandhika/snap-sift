package com.rizadwi.snapsift.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizadwi.snapsift.common.constant.Constant.Companion.FIRST_PAGE
import com.rizadwi.snapsift.common.constant.Constant.Companion.SIZE_PER_PAGE
import com.rizadwi.snapsift.common.wrapper.Result
import com.rizadwi.snapsift.common.wrapper.UIState
import com.rizadwi.snapsift.model.Article
import com.rizadwi.snapsift.usecase.GetHeadlineArticlesUserCase
import com.rizadwi.snapsift.util.extension.postError
import com.rizadwi.snapsift.util.extension.postLoading
import com.rizadwi.snapsift.util.extension.postSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val getHeadlineArticlesUserCase: GetHeadlineArticlesUserCase) :
    ViewModel() {
    private val _flow = MutableSharedFlow<String>()
    private val _articleLiveData = MutableLiveData<UIState<List<Article>>>()
    private val _freshArticleLiveData = MutableLiveData<UIState<List<Article>>>()

    private var cachedArticles = mutableListOf<Article>()
    private var currentPage = FIRST_PAGE
    private var query = ""
    private var sources = ""

    val articleLiveData: LiveData<UIState<List<Article>>> = _articleLiveData
    val freshArticleLiveData: LiveData<UIState<List<Article>>> = _freshArticleLiveData

    init {
        viewModelScope.launch {
            _flow.debounce(DELAY).collectLatest(::resetThenLoadFreshArticle)
        }
    }

    fun loadFromCacheIfPossible() = viewModelScope.launch {
        if (cachedArticles.isEmpty()) {
            loadFreshHeadlineArticles()
        } else {
            _freshArticleLiveData.postSuccess(cachedArticles)
        }
    }

    private fun loadFreshHeadlineArticles() = viewModelScope.launch {
        loadHeadlineArticles(_freshArticleLiveData)
    }

    fun loadMoreHeadlineArticles() = viewModelScope.launch {
        loadHeadlineArticles(_articleLiveData)
    }

    fun changeQueryThenLoadFreshHeadlineArticles(query: String) = viewModelScope.launch {
        _flow.emit(query)
    }

    fun setSources(sources: String) {
        this.sources = sources
    }

    private fun resetThenLoadFreshArticle(keyword: String) {
        // Ignore if the keyword still the same as previous query
        if (query == keyword) {
            return
        }

        query = keyword
        currentPage = FIRST_PAGE
        cachedArticles = mutableListOf()

        loadFreshHeadlineArticles()
    }

    private fun loadHeadlineArticles(liveData: MutableLiveData<UIState<List<Article>>>) =
        viewModelScope.launch {
            liveData.postLoading()

            if (sources.isBlank()) {
                liveData.postError(Error("Sources should be specified"))
                return@launch
            }


            val result = if (query.isBlank()) {
                getHeadlineArticlesUserCase.invoke(sources, SIZE_PER_PAGE, currentPage++)
            } else {
                getHeadlineArticlesUserCase.invoke(query, sources, SIZE_PER_PAGE, currentPage++)
            }

            when (result) {
                is Result.Failure -> liveData.postError(result.cause)
                is Result.Success -> {
                    liveData.postSuccess(result.payload)
                    cachedArticles.addAll(result.payload)
                }
            }
        }


    companion object {
        const val DELAY = 350L
    }
}