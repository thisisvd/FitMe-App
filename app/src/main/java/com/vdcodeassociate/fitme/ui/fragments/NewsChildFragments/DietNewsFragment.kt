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

class DietNewsFragment: Fragment(R.layout.fragment_news_all) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}