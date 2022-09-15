package com.ekenya.rnd.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.ekenya.rnd.common.abstractions.BaseActivity
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.DashBoardActivity
import com.ekenya.rnd.onboarding.databinding.ActivityMainBinding
import com.ekenya.rnd.walletbaseapp.tollo.WalletSolutionApp
import dagger.android.AndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    lateinit var navController: NavController

    // private MainViewModel mViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        supportActionBar!!.title = ""
        setContentView(binding!!.root)
        // val navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(navController)

        if (SharedPreferencesManager.hasFinishedSliders(applicationContext) == true && SharedPreferencesManager.hasReachedHomepage(
                applicationContext
            ) == true
        ) {
            // go to get started landing page
            val intent = Intent(applicationContext, DashBoardActivity::class.java)
            startActivity(intent)
        } else if (SharedPreferencesManager.hasFinishedSliders(applicationContext) == true) {
            navController.navigate(R.id.action_segFragment_to_getStartedFragment)
        } else {
            Log.e("getStartedFragment", "getStartedFragment")
        }

//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            val destinationId = destination.id
//            if (destinationId == R.id.getStartedFragment ||
//                destinationId == R.id.landingPageFragment ||
//                destinationId == R.id.segFragment
//            ) {
//            } else {
//
//            }
//        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as WalletSolutionApp).supportFragmentInjector()
    }

    override fun onBackPressed() {
        if (navController.currentDestination!!.id == R.id.getStartedFragment || navController.currentDestination!!.id == R.id.segFragment) {
            finishAffinity()
        } else {
            navController.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
