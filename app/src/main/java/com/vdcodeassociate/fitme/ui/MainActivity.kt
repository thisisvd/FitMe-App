package com.vdcodeassociate.fitme.ui

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.vdcodeassociate.fitme.databinding.ActivityMainBinding
import com.vdcodeassociate.fitme.room.RunDao
import com.vdcodeassociate.fitme.ui.fragments.NewsFragment
import com.vdcodeassociate.runningtrackerapp.ui.Fragments.RunFragment
import com.vdcodeassociate.runningtrackerapp.ui.Fragments.StatisticsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var runDao: RunDao

    // viewBinding
    private lateinit var binding: ActivityMainBinding

    // navHostFragment
    lateinit var navHostFragment: View

    // drawer layout
    private lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar setup
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        binding.toolbar.overflowIcon!!.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        // Setting up bottom nav controller
        navHostFragment = findViewById(R.id.navHostFragment)

        // check for notification (tracking) fragment
        navigateToTrackingFragment(intent)

//        binding.bottomNavigationView.setupWithNavController(navHostFragment!!.findNavController())
//        binding.bottomNavigationView.setOnNavigationItemReselectedListener {
//            // not to be implemented
//        }

        setUpNavigationDrawer()

        navHostFragment.findNavController().navigate(R.id.homeFragment2)
        binding.bottomNavigationView.setItemSelected(R.id.homeFragment2)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it) {
                R.id.homeFragment2 -> {
                    navHostFragment.findNavController().navigate(R.id.homeFragment2)
                }
                R.id.runFragment -> {
                    navHostFragment.findNavController().navigate(R.id.runFragment)
                }
                R.id.statisticsFragment -> {
                    navHostFragment.findNavController().navigate(R.id.statisticsFragment)
                }
                R.id.profileFragment -> {
                    navHostFragment.findNavController().navigate(R.id.profileFragment)
                }
            }
        }

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->

                when(destination.id){
                    R.id.profileFragment, R.id.runFragment, R.id.statisticsFragment,
                    R.id.homeFragment2 ->
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

    // set up nav drawer
    private fun setUpNavigationDrawer(){
        // nav drawer setup
        drawerLayout = binding.mainRootView
        val toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.black_normal_text)
        toggle.syncState()

        // nav Drawer set Item Listener
        binding.mainNavView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawer(GravityCompat.START)

            when(item.itemId) {
                R.id.homeFragment2 -> {
                    Toast.makeText(applicationContext, "Home Fragments!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.runFragment -> {
                    Toast.makeText(applicationContext, "Run Fragments!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.statisticsFragment -> {
                    Toast.makeText(applicationContext, "Statistics Fragments!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.profileFragment -> {
                    Toast.makeText(applicationContext, "Profile Fragment!", Toast.LENGTH_SHORT).show()
                    true
                }
            }
            true
        }
    }

    // All Toolbar's Menu Item selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.schedulesItems -> {
                Toast.makeText(this,"Schedule Icon",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.logoutProfile -> {
                Toast.makeText(this,"Logging out!",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editProfile -> {
                Toast.makeText(this,"Edit Profile!",Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

    // Handle On Back pressing
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}