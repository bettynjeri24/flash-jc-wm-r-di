package com.ekenya.lamparam.activities.onboarding

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.ActivityOnBoardingBinding
//import com.ekenya.lamparam.di.DaggerLampAppComponent
//import com.ekenya.lamparam.di.LampAppComponent

class OnBoardingActivity : AppCompatActivity() {
//    val appComponent: LampAppComponent = DaggerLampAppComponent.factory().create(this)

    private lateinit var binding: ActivityOnBoardingBinding

    lateinit var appBarConfiguration: AppBarConfiguration

    // Stores an instance of OnboardingComponent so that its Fragments can access it
    lateinit var onboardingComponent: OnboardingComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Registration component by grabbing the factory from the app graph
       // onboardingComponent = (application as AppC4MxFNKMi4N).appComponent.onboardingComponent().create()
//        onboardingComponent = appComponent.onboardingComponent().create()
        // Injects this activity to the just created registration component
//        onboardingComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //  authenticate("65680","0088");
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //handle all back button here
    fun onBackClick(imgView: ImageView, rootView: View) {
        imgView.setOnClickListener { rootView.findNavController().navigateUp() }
    }
}