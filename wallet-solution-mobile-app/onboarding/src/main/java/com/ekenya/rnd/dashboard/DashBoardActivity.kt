package com.ekenya.rnd.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.abstractions.BaseActivity
import com.ekenya.rnd.dashboard.utils.removeActionBarElevation
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.ActivityDashBoardBinding


class DashBoardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)

        setContentView(binding!!.root)

        val navView = binding.navView


        navController = findNavController(R.id.nav_host_fragment_activity_dashboard)


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.growthFragment
            )
        )

        setupActionBarWithNavController(navController)
        navView.setupWithNavController(navController)

        removeActionBarElevation()

    }

    override fun onBackPressed() {

        val currentDestinationId = navController.currentDestination!!.id

        if (currentDestinationId == R.id.landingPageFragment || currentDestinationId == R.id.loginFragment2 || currentDestinationId == R.id.initialPinFragment2) {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit this app?")
                .setPositiveButton(
                    "Yes"
                ) { _, _ -> finishAffinity() }
                .setNegativeButton("No", null)
                .show()


            // finishAffinity()
        }else if(currentDestinationId == R.id.navigation_dashboard || currentDestinationId == R.id.transactionsFragment){
            navController.navigate(R.id.navigation_home)
        } else {
            navController.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}