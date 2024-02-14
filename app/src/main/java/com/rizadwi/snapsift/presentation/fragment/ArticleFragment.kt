package com.rizadwi.snapsift.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.rizadwi.snapsift.common.base.BaseFragment
import com.rizadwi.snapsift.common.wrapper.UIState
import com.rizadwi.snapsift.databinding.FragmentArticleBinding
import com.rizadwi.snapsift.model.Article
import com.rizadwi.snapsift.presentation.adapter.ArticleAdapter
import com.rizadwi.snapsift.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : BaseFragment<FragmentArticleBinding>(),
    OnQueryTextListener {

    private val viewModel: ArticleViewModel by viewModels()

    @Inject
    lateinit var articleAdapter: ArticleAdapter

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentArticleBinding {
        return FragmentArticleBinding.inflate(inflater, container, false)
    }


    override fun setupView(view: View, savedInstanceState: Bundle?) {
        setupUI()
        observeViewModel()
        fetchData()
    }

    private fun setupUI() {
        val source = ArticleFragmentArgs.fromBundle(arguments as Bundle).source

        viewModel.setSources(source)
        articleAdapter.stateRestorationPolicy = PREVENT_WHEN_EMPTY
        articleAdapter.setOnItemClickListener(::moveToWebView)

        binding.tvSource.text = source
        binding.incSearch.svSearchNews.setOnQueryTextListener(this)
        binding.rvArticle.adapter = articleAdapter
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadMoreHeadlineArticles()
                }
            }
        })
    }

    private fun moveToWebView(article: Article) {
        val toWebViewFragment =
            ArticleFragmentDirections.actionArticleFragmentToWebViewFragment(article.url ?: "")
        findNavController().navigate(toWebViewFragment)
    }

    private fun observeViewModel() {
        viewModel.freshArticleLiveData.observe(viewLifecycleOwner, ::handleFreshData)
        viewModel.articleLiveData.observe(viewLifecycleOwner, ::handleMoreData)
    }

    private fun fetchData() {
        viewModel.loadFromCacheIfPossible()
    }

    private fun handleFreshData(state: UIState<List<Article>>) {
        binding.tvError.visibility = View.GONE
        when (state) {
            UIState.Pending -> binding.pbLoading.visibility = View.VISIBLE
            is UIState.Failure -> {
                binding.pbLoading.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = state.cause.message
            }

            is UIState.Success -> {
                articleAdapter.setArticleList(state.data)
                if (state.data.isEmpty()) {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        }
    }

    private fun handleMoreData(state: UIState<List<Article>>) {
        binding.tvError.visibility = View.GONE
        when (state) {
            UIState.Pending -> {
                binding.pbLoading.visibility = View.VISIBLE
            }

            is UIState.Failure -> {
                binding.pbLoading.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = state.cause.message
            }

            is UIState.Success -> {
                articleAdapter.appendArticleList(state.data)
                if (state.data.isEmpty()) {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        }
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let(viewModel::changeQueryThenLoadFreshHeadlineArticles)
        return true
    }

}