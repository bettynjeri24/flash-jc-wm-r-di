package com.ekenya.rnd.tijara

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.ekenya.rnd.baseapp.BaseApp
import com.ekenya.rnd.baseapp.ui.main.MainViewModel
import com.ekenya.rnd.common.abstractions.BaseActivity
import com.ekenya.rnd.tijara.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjector
import javax.inject.Inject


class MainActivity : BaseActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setTheme(R.style.FragmentTheme)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        bottomNavView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)


          navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeMainFragment || destination.id == R.id.serviceFragment
                || destination.id == R.id.loanDashBoardFragment || destination.id == R.id.userProfileFragment) {
                binding.navView.visibility = View.VISIBLE
            } else {
                binding.navView.visibility = View.GONE
            }
        }



    }
    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as BaseApp).supportFragmentInjector()
    }



}
