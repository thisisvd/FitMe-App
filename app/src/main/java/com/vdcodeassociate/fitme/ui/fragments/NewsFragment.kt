package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.FragmentAdapter
import com.vdcodeassociate.fitme.adapters.NewsAdapter
import com.vdcodeassociate.fitme.databinding.FragmentNewsBinding
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment: Fragment(R.layout.fragment_news) {

    // TAG
    private var TAG = "NewsFragment"

    // viewBinding
    private lateinit var binding: FragmentNewsBinding

    // Fragment adapter
    private lateinit var fragmentAdapter: FragmentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        fragmentAdapter = FragmentAdapter(this)
        binding.newsViewPager2.adapter = fragmentAdapter

        TabLayoutMediator(binding.newsTabLayout,binding.newsViewPager2) { tab, position ->
            if(position == 1) {
                tab.text = "All"
            }else {
                tab.text = "Diet"
            }
        }.attach()

    }

}