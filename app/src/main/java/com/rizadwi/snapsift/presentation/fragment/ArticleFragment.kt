package com.rizadwi.snapsift.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rizadwi.snapsift.common.base.BaseFragment
import com.rizadwi.snapsift.databinding.FragmentArticleBinding

class ArticleFragment : BaseFragment<FragmentArticleBinding>() {
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
        TODO("Not yet implemented")
    }

    private fun observeViewModel() {
        TODO("Not yet implemented")
    }

    private fun fetchData() {
        TODO("Not yet implemented")
    }
}