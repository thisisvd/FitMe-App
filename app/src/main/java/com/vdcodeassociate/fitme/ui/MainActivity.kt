package com.vdcodeassociate.fitme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.vdcodeassociate.fitme.databinding.ActivityMainBinding
import com.vdcodeassociate.fitme.room.RunDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var runDao: RunDao

    // viewBinding
    lateinit var binding: ActivityMainBinding

    // navHostFragment
    lateinit var navHostFragment: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up bottom nav controller
        navHostFragment = findViewById(R.id.navHostFragment)

        // check for notification (tracking) fragment
        navigateToTrackingFragment(intent)

        // Toolbar support
        setSupportActionBar(binding.toolbar)

        binding.bottomNavigationView.setupWithNavController(navHostFragment!!.findNavController())
        binding.bottomNavigationView.setOnNavigationItemReselectedListener {
            // not to be implemented
        }
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->

                when(destination.id){
                    R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment ->
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }

            }

    }

    private fun navigateToTrackingFragment(intent: Intent?) {
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_tracking_fragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragment(intent)
    }

}