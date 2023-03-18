package com.vdcodeassociate.fitme.ui.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    // view binding
    private lateinit var binding: FragmentArticlesBinding

    // recycler adapter
    private lateinit var articleAdapter: ArticleAdapter

    // view model
    private val viewModel: NewsViewModel by viewModels()

    // alert progress dialog
    private lateinit var dialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticlesBinding.bind(view)

        // get args
        val getArgs = arguments?.getInt("amount")

        // init Loading Dialog
        dialog = Dialog(requireContext())
        dialog.apply {
            setContentView(R.layout.custom_dialog_layout)
            setCancelable(false)
            if (window != null) {
                window!!.setBackgroundDrawable(ColorDrawable(0))
            }
        }

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
                    viewModel.tabLatestNews(tab!!.position)
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
        viewModel.getLatestNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        if (!newsResponse.articles.isNullOrEmpty()) {
                            articleAdapter.differ.submitList(newsResponse.articles)
                            dialog.dismiss()
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.d(TAG, "An error occurred : $message")
                    }
                    dialog.dismiss()
                }
                is Resource.Loading -> {
                    Log.d(TAG, "Articles loading...")
                    dialog.show()
                }
            }
        }
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