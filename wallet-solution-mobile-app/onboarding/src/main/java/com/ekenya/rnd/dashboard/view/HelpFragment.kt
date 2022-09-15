package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentAboutUsBinding


class HelpFragment : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        requireActivity().window.setFeatureInt(
            Window.FEATURE_PROGRESS,
            Window.PROGRESS_VISIBILITY_ON
        );


        binding.webView.webViewClient = WebViewClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                binding.progressBar.makeVisible()
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.progressBar.makeInvisible()
            }
        }
        // this will load the url of the website
        binding.webView.loadUrl(getString(R.string.contact_url))

        // this will enable the javascript settings
        binding.webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

}