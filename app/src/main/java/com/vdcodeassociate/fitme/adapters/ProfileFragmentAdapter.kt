package com.vdcodeassociate.fitme.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vdcodeassociate.fitme.ui.fragments.ArticleFragment
import com.vdcodeassociate.runningtrackerapp.ui.Fragments.StatisticsFragment

class ProfileFragmentAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return if (position == 0) {
            ArticleFragment()
        } else {
            StatisticsFragment()
        }

    }

}