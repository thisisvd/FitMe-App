package com.vdcodeassociate.fitme.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants
import com.vdcodeassociate.fitme.constants.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.vdcodeassociate.fitme.constants.Constants.AVATAR_ID
import com.vdcodeassociate.fitme.constants.Constants.KEY_AGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_IMAGE
import com.vdcodeassociate.fitme.constants.Constants.KEY_NAME
import com.vdcodeassociate.fitme.databinding.ActivityMainBinding
import com.vdcodeassociate.fitme.utils.Permissions
import com.vdcodeassociate.fitme.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // TAG
    private val TAG = "MainActivity"

    // temp int
    private var locationCounter = 0

    // Injected Shared preferences
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // viewBinding
    private lateinit var binding: ActivityMainBinding

    // navHostFragment
    lateinit var navHostFragment: View

    // drawer layout
    private lateinit var drawerLayout: DrawerLayout

    // homeViewModel
    val viewModel: HomeViewModel by viewModels()

    // maps init
    private lateinit var googleApiClient: GoogleApiClient
    private val REQUEST_LOCATION = 199

    // location request
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Force No Night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //Screen orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Toolbar setup
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        binding.toolbar.overflowIcon!!.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        // Load and save avatar ID from sharedPref to constant
        loadAvatarID()

        // Setting up bottom nav controller
        navHostFragment = findViewById(R.id.navHostFragment)

        // check for notification (tracking) fragment
        navigateToTrackingFragment(intent)

        // Navigation Drawer init
        setUpNavigationDrawer()

        // location init
        locationUpdate()

        // setting nav host fragments
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        binding.bottomNavigationView.setOnItemReselectedListener {
            // don nothing
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment2 -> {
                    navHostFragment.findNavController().navigate(R.id.homeFragment2)
                    true
                }
                R.id.runFragment -> {
                    navHostFragment.findNavController().navigate(R.id.runFragment)
                    true
                }
                R.id.videoListFragment -> {
                    navHostFragment.findNavController().navigate(R.id.videoListFragment)
                    true
                }
                R.id.profileFragment -> {
                    navHostFragment.findNavController().navigate(R.id.profileFragment)
                    true
                }

                else -> {
                    true
                }
            }
        }

        // setting bottom nav and toolbar for fragment changes
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->

                when (destination.id) {
                    R.id.profileFragment, R.id.runFragment, R.id.videoListFragment,
                    R.id.homeFragment2 -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        binding.toolbar.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.bottomNavigationView.visibility = View.GONE
                        binding.toolbar.visibility = View.GONE
                    }
                }

            }
    }

    // navigate to tracking fragment
    private fun navigateToTrackingFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_tracking_fragment)
        }
    }

    // navigate to any other fragments
    fun navigateToFragment(id: Int) {
        navHostFragment.findNavController().navigate(id)
        // binding.bottomNavigationView.setItemSelected(id)
    }

    // navigate to the tracking fragment
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragment(intent)
    }

    // set up nav drawer
    private fun setUpNavigationDrawer() {
        // nav drawer setup
        drawerLayout = binding.mainRootView
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        setUpHeader()
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.black_normal_text)
        toggle.syncState()

        // nav Drawer set Item Listener
        binding.mainNavView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)

            when (item.itemId) {
                R.id.profileDrawer -> {
                    navigateToFragment(R.id.profileFragment)
                }
                R.id.scheduleRuns -> {
                    navHostFragment.findNavController().navigate(R.id.scheduleFragment)
                }
                R.id.savedTips -> {
                    navHostFragment.findNavController().navigate(R.id.newsFragment)
                }
                R.id.inviteFriends -> {
                    inviteFriend()
                }
                R.id.getHelp -> {
                    val bundle = Bundle().apply {
                        putString("myArgs", "Get Help!")
                    }
                    navHostFragment.findNavController().navigate(R.id.supportFragment, bundle)
                }
                R.id.giveFeedback -> {
                    val bundle = Bundle().apply {
                        putString("myArgs", "Feedback!")
                    }
                    navHostFragment.findNavController().navigate(R.id.supportFragment, bundle)
                }
                R.id.aboutUs -> {
                    navHostFragment.findNavController().navigate(R.id.aboutUsFragment)
                }
            }
            true
        }
    }

    // setting up nav drawer header
    fun setUpHeader() {
        val header = binding.mainNavView.getHeaderView(0)
        header.findViewById<TextView>(R.id.nav_name).text =
            sharedPreferences.getString(KEY_NAME, "")
        header.findViewById<TextView>(R.id.nav_age).text =
            "${sharedPreferences.getInt(KEY_AGE, 0)} Years"
        // load user image
        header.findViewById<ImageView>(R.id.nav_profile_image)
            .setImageResource(AVATAR_ID)
    }

    // All Toolbar's Menu Item selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.schedulesItems -> {
                navHostFragment.findNavController().navigate(R.id.scheduleFragment)
                true
            }
            R.id.editProfile -> {
                navHostFragment.findNavController().navigate(R.id.editProfileFragment)
                true
            }
            R.id.homeLastRunLayout -> {
                // binding.bottomNavigationView.setItemSelected(R.id.runFragment)
                true
            }
            else -> false
        }
    }

    // GPS On / Off Observer
    fun enableGPS() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {}
                override fun onConnectionSuspended(i: Int) {
                    googleApiClient?.connect()
                }
            })
            .addOnConnectionFailedListener {
            }.build()
        googleApiClient?.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                        this@MainActivity,
                        REQUEST_LOCATION
                    )
                } catch (e: IntentSender.SendIntentException) {
                }
            }
        }
    }

    // location update
    private fun locationUpdate() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (Permissions.hasLocationPermission(this)) {
            val request = LocationRequest().apply {
                interval = Constants.LOCATION_UPDATE_INTERVAL
                fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    // location callback
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            p0?.locations?.let { locations ->
                for (location in locations) {
                    Log.d(
                        TAG,
                        "New Counter : $locationCounter"
                    )

                    if ((locationCounter % 30 == 0)) {
                        Log.d(
                            TAG,
                            "New Location : ${location.latitude}, ${location.longitude} $location"
                        )
                        viewModel.getWeatherUpdate("${location.latitude},${location.latitude}")
                        if (locationCounter == 60) {
                            locationCounter = 0
                        }
                    }
                    locationCounter++

                }

            }

        }
    }

    // GPS activity results
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION -> when (resultCode) {
                Activity.RESULT_OK -> Log.d("abc", "OK")
                Activity.RESULT_CANCELED -> {
                    val dialog = MaterialAlertDialogBuilder(
                        this,
                        R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                    )
                        .setTitle("GPS Required!")
                        .setMessage("Without GPS this app will not work properly!")
                        .setCancelable(false)
                        .setIcon(R.drawable.gps_icons8)
                        .setPositiveButton("OK") { _, _ ->
                            enableGPS()
                        }
                        .create()
                    dialog.show()
                }
            }
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

    // invite Friend
    fun inviteFriend() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = getString(R.string.inviteFriends)
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(sharingIntent)
    }

    // Load Avatar ID For all
    private fun loadAvatarID() {
        AVATAR_ID = sharedPreferences.getInt(KEY_IMAGE, R.drawable.profile_other_image)
    }
}