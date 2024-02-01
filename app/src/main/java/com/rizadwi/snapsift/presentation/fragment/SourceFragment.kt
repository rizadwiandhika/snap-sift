package com.rizadwi.snapsift.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    BaseFragment<FragmentSourceBinding>() {
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
        sourceAdapter.setOnItemClickListener(::handleSourceClicked)
        categoryAdapter.setOnCategoryClickListener(::handleCategoryClicked)

        binding.rvSources.adapter = sourceAdapter
        binding.rvCategory.adapter = categoryAdapter
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
            UIState.Pending -> binding.pbLoading.visibility = View.VISIBLE
            is UIState.Failure -> {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = it.cause.message
            }

            is UIState.Success -> {
                binding.rvSources.visibility = View.VISIBLE
                sourceAdapter.setSourceList(it.data)
            }
        }
    }

    private fun fetchData() {
        viewModel.getCategories()
        viewModel.getAllSources()
    }


    private fun hideSourceListUI() {
        with(binding) {
            pbLoading.visibility = View.GONE
            rvSources.visibility = View.GONE
            tvError.visibility = View.GONE
        }
    }

    private fun handleSourceClicked(source: Source) {
        val toArticleFragment =
            SourceFragmentDirections.actionSourceFragmentToArticleFragment(source.id)
        findNavController().navigate(toArticleFragment)
    }

}