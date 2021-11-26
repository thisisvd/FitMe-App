package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.ArticleAdapter
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.vdcodeassociate.fitme.databinding.FragmentArticlesBinding

@AndroidEntryPoint
class ArticleFragment: Fragment(R.layout.fragment_articles) {

    // TAG
    private var TAG = "ArticleFragment"

    // viewBinding
    private lateinit var binding: FragmentArticlesBinding

    // Recycler adapter
    private lateinit var articleAdapter: ArticleAdapter

    // viewModel
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticlesBinding.bind(view)

        // get args
        val getArgs = arguments?.getInt("amount")

        // init recyclerView
        setUpRecyclerView()

        // Tab Layout init
        binding.apply {

            // Adding news tabs
            newsTabLayout.addTab(newsTabLayout.newTab().setText("TIPS"))
            newsTabLayout.addTab(newsTabLayout.newTab().setText("DIET"))
            newsTabLayout.addTab(newsTabLayout.newTab().setText("EXERCISE & MEDITATION"))

            // getting tab position
            if(getArgs != null) {
                newsTabLayout.getTabAt(getArgs.toInt())?.select()
            }

            // on tab select listener
            newsTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {

                    var queryPosition = 0

                    when(tab?.position) {
                        1 -> queryPosition = 1
                        2 -> queryPosition = 2
                    }

                    viewModel.isDataAdded = false

                    viewModel.tabLatestNews(queryPosition)
                    binding.recyclerView.visibility = View.GONE
                    articleAdapter.notifyDataSetChanged()

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Do not implement anything
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Do not do anything
                }

            })

            // Set Up adapter
            articleAdapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putSerializable("article",it)
                }
                findNavController().navigate(
                    R.id.action_newsFragment_to_articleWebPage,
                    bundle
                )
            }

            // on click listener
            backNews.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        // viewModel Observer
        viewModelObserver()

    }

    // viewModel observer method
    private fun viewModelObserver() {
        // viewModel observe
        viewModel.getLatestNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        articleAdapter.differ.submitList(newsResponse.articles)
                        articleAdapter.notifyDataSetChanged()
                        if (viewModel.isDataAdded) {
                            binding.progress.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }
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
        articleAdapter = ArticleAdapter()
        binding.recyclerView.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}