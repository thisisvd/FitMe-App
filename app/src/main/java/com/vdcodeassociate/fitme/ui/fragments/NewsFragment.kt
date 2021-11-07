package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.NewsAdapter
import com.vdcodeassociate.fitme.databinding.FragmentNewsBinding
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


@AndroidEntryPoint
class NewsFragment: Fragment(R.layout.fragment_news) {

    // TAG
    private var TAG = "NewsFragment"

    // viewBinding
    private lateinit var binding: FragmentNewsBinding

    // Recycler adapter
    private lateinit var newsAdapter: NewsAdapter

    // viewModel
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)


        binding.apply {

            // Adding news tabs
            newsTabLayout.addTab(newsTabLayout.newTab().setText("ALL"))
            newsTabLayout.addTab(newsTabLayout.newTab().setText("TIPS"))
            newsTabLayout.addTab(newsTabLayout.newTab().setText("EXERCISE"))
            newsTabLayout.addTab(newsTabLayout.newTab().setText("DIET"))
            newsTabLayout.addTab(newsTabLayout.newTab().setText("MEDITATION"))

            // on tab select listener
            newsTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position) {
                        1 -> tab?.text = "Tips"
                        2 -> tab?.text = "Exercise"
                        3 -> tab?.text = "Diet"
                        4 -> tab?.text = "Meditation"
                        else -> tab?.text = "All"
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Do not do anything
                }

            })
        }

        setUpRecyclerView()

        // viewModel observe
        viewModel.getLatestNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progress.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "AN error occurred : $message")
                    }
                }
                is Resource.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        })

    }

    // set up recycler view
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}