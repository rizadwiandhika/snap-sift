package com.rizadwi.snapsift.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.rizadwi.snapsift.common.base.BaseFragment
import com.rizadwi.snapsift.databinding.FragmentWebviewBinding
import com.rizadwi.snapsift.util.Clipper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebviewBinding>() {

    @Inject
    lateinit var clipper: Clipper

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

        with(binding) {
            progressBar.isIndeterminate = false
            webView.webViewClient = getWebViewClient()
            webView.webChromeClient = getWebChromeClient()
            webView.settings.javaScriptEnabled = true
            tvBarUrl.text = clipper.clip(url, MAX_URL_LENGTH)
            close.setOnClickListener { findNavController().popBackStack() }
            webView.loadUrl(url)
        }
    }

    private fun getWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.setProgress(newProgress, true)
                binding.tvBarTitle.text = clipper.clip(view?.title ?: "", MAX_TITLE_LENGTH)

                if (newProgress == 100) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun getWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String
            ): Boolean {
                binding.tvBarUrl.text = clipper.clip(url, MAX_URL_LENGTH)
                return false
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.webView.webChromeClient = null
    }

    companion object {
        const val MAX_URL_LENGTH = 42
        const val MAX_TITLE_LENGTH = 56
    }
}