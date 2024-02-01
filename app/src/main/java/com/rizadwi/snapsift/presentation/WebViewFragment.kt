package com.rizadwi.snapsift.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rizadwi.snapsift.common.base.BaseFragment
import com.rizadwi.snapsift.databinding.FragmentWebviewBinding

class WebViewFragment : BaseFragment<FragmentWebviewBinding>() {
    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentWebviewBinding {
        return FragmentWebviewBinding.inflate(inflater, container, false)
    }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        val url = WebViewFragmentArgs.fromBundle(arguments as Bundle).url
        if (url.isBlank()) {
            Toast.makeText(requireContext(), "URL should be specified!", Toast.LENGTH_SHORT).show()
            return
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)
    }
}