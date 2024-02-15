package com.rizadwi.snapsift.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rizadwi.snapsift.common.base.BaseFragment
import com.rizadwi.snapsift.common.wrapper.UIState
import com.rizadwi.snapsift.databinding.FragmentSourceBinding
import com.rizadwi.snapsift.model.Source
import com.rizadwi.snapsift.presentation.adapter.CategoryAdapter
import com.rizadwi.snapsift.presentation.adapter.SourceAdapter
import com.rizadwi.snapsift.viewmodel.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SourceFragment :
    BaseFragment<FragmentSourceBinding>(), OnQueryTextListener {
    private val viewModel: SourceViewModel by viewModels()

    @Inject
    lateinit var sourceAdapter: SourceAdapter

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSourceBinding {
        return FragmentSourceBinding.inflate(inflater, container, false)
    }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        setupUI()
        observeViewModel()
        fetchData()
    }

    private fun setupUI() {
        binding.rvSources.adapter = sourceAdapter
        binding.rvCategory.adapter = categoryAdapter

        binding.incSearch.svSearchNews.queryHint = "Search source..."
        sourceAdapter.setOnItemClickListener(::handleSourceClicked)
        categoryAdapter.setOnCategoryClickListener(::handleCategoryClicked)
        binding.incSearch.svSearchNews.setOnQueryTextListener(this)
    }

    private fun handleCategoryClicked(category: String, index: Int) {
        viewModel.getSourcesByCategory(category)
        categoryAdapter.setActiveIndex(index)
    }


    private fun observeViewModel() {
        viewModel.sourceListLiveData.observe(viewLifecycleOwner, ::handleSourceListData)
        viewModel.categoryListLiveData.observe(viewLifecycleOwner, ::handleCategoryList)
    }

    private fun handleCategoryList(it: UIState<List<String>>) {
        when (it) {
            is UIState.Success -> categoryAdapter.setCategoryList(it.data)
            else -> {}
        }
    }

    private fun handleSourceListData(it: UIState<List<Source>>) {
        hideSourceListUI()
        when (it) {
            UIState.Pending -> binding.pbLoading.isVisible = true
            is UIState.Failure -> {
                binding.tvError.isVisible = true
                binding.tvError.text = it.cause.message
            }

            is UIState.Success -> {
                binding.rvSources.isVisible = true
                sourceAdapter.setSourceList(it.data)
            }
        }
    }

    private fun fetchData() {
        viewModel.getCategories()
        viewModel.getSourcesFromCacheIfPossible()
    }


    private fun hideSourceListUI() {
        with(binding) {
            pbLoading.isVisible = false
            rvSources.isVisible = false
            tvError.isVisible = false
        }
    }

    private fun handleSourceClicked(source: Source) {
        val toArticleFragment =
            SourceFragmentDirections.actionSourceFragmentToArticleFragment(source.id)
        findNavController().navigate(toArticleFragment)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(keyword: String?): Boolean {
        viewModel.prepareFilterSource(keyword ?: "")
        return true
    }

}