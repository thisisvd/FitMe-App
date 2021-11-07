package com.vdcodeassociate.fitme.ui.fragments.NewsChildFragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.NewsAdapter
import com.vdcodeassociate.fitme.databinding.FragmentNewsAllBinding
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllNewsFragment: Fragment(R.layout.fragment_news_all) {

    // TAG
    private var TAG = "NewsFragment"

    // viewBinding
    private lateinit var binding: FragmentNewsAllBinding

    // Recycler adapter
    private lateinit var newsAdapter: NewsAdapter

    // viewModel
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsAllBinding.bind(view)

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