package com.vdcodeassociate.fitme.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vdcodeassociate.fitme.ui.fragments.NewsChildFragments.AllNewsFragment
import com.vdcodeassociate.fitme.ui.fragments.NewsChildFragments.DietNewsFragment

class FragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {



    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        when(position) {
            1 ->  return DietNewsFragment()
        }

        return AllNewsFragment()
    }
}