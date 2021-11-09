package com.vdcodeassociate.runningtrackerapp.ui.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.adapters.ProfileAdapter
import com.vdcodeassociate.fitme.adapters.ProfileFragmentAdapter
import com.vdcodeassociate.fitme.adapters.RunAdapter
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.constants.Constants.KEY_WEIGHT
import com.vdcodeassociate.fitme.databinding.FragmentProfileBinding
import com.vdcodeassociate.fitme.model.profilemodel.ProfileItemsClass
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile){

    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // adapter
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        setupRecyclerView()

        val list = ArrayList<ProfileItemsClass>()
        list.add(ProfileItemsClass(R.drawable.save_icons8,"Saved Articles"))
        list.add(ProfileItemsClass(R.drawable.task_icons8,"Scheduled Runs"))
        list.add(ProfileItemsClass(R.drawable.weather_icons8,"Invite Friends"))
        list.add(ProfileItemsClass(R.drawable.get_help_icons8,"Get Help!"))
        list.add(ProfileItemsClass(R.drawable.feedback_icons8,"Give us Feedback"))
        list.add(ProfileItemsClass(R.drawable.aboutus_icons8,"About us!"))
        list.add(ProfileItemsClass(R.drawable.logout_icons8,"Logout!"))

        profileAdapter.differ.submitList(list)
        profileAdapter.notifyDataSetChanged()

    }

    // Recycler view setup
    private fun setupRecyclerView(){
        profileAdapter = ProfileAdapter()
        val linearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.profileRecyclerView.apply {
            adapter = profileAdapter
            layoutManager = linearLayoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentProfileBinding.bind(view)
//
//        loadFieldsFromSharedPreferences()
//
//        binding.btnApplyChanges.setOnClickListener {
//            val success = applyChangesToSharedPreference()
//            if (success) {
//                Snackbar.make(requireView(), "Saved Changes", Snackbar.LENGTH_SHORT).show()
//            }else {
//                Snackbar.make(requireView(), "Please fill out all the fields", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//
//    }
//
//    private fun loadFieldsFromSharedPreferences(){
//        val name = sharedPreferences.getString(KEY_NAME,"")
//        val weight = sharedPreferences.getFloat(KEY_WEIGHT,80f)
//        binding.etName.setText(name)
//        binding.etWeight.setText(weight.toString())
//    }
//
//    private fun applyChangesToSharedPreference() : Boolean {
//        val name = binding.etName.text.toString()
//        val weight = binding.etWeight.text.toString()
//
//        if(name.isEmpty() || weight.isEmpty()){
//            return false
//        }
//
//        sharedPreferences.edit().putString(KEY_NAME,name).putFloat(KEY_WEIGHT,weight.toFloat()).apply()
//
//        return true
//    }

}