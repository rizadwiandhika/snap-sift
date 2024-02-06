package com.rizadwi.snapsift.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
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

        binding.progressBar.isIndeterminate = false
        binding.webView.webChromeClient = getWebChromeClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.tvBarUrl.text = clipper.clip(url, MAX_URL_LENGTH)
        binding.close.setOnClickListener { findNavController().popBackStack() }
        binding.webView.loadUrl(url)
    }

    private fun getWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.setProgress(newProgress, true)

                if (newProgress == 100) {
                    binding.progressBar.visibility = View.GONE
                    binding.tvBarTitle.text = clipper.clip(view?.title ?: "", MAX_TITLE_LENGTH)
                }
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