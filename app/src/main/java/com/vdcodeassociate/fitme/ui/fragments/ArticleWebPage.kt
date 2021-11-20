package com.vdcodeassociate.fitme.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentArticleWebPageBinding

class ArticleWebPage: Fragment(R.layout.fragment_article_web_page) {

    // viewBinding
    private lateinit var binding: FragmentArticleWebPageBinding

    // getting arguments as passed
    private val args: ArticleWebPageArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleWebPageBinding.bind(view)

        // init args values that have been passed
        val article = args.article

        binding.apply {

            // web view implementation
            webView.apply {
                webViewClient = object : WebViewClient(){

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        webViewProgressBar.apply {
                            visibility = View.VISIBLE
                        }
                    }

                    override fun onPageCommitVisible(view: WebView?, url: String?) {
                        super.onPageCommitVisible(view, url)
                        webViewProgressBar.apply {
                            visibility = View.GONE
                        }
                    }

                }
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                loadUrl(article.url)
            }

            // set up title
            title.text = "From - ${article.source.name}"

            // handle onBack pressed
            back.setOnClickListener {
                requireActivity().onBackPressed()
            }

        }

    }

}