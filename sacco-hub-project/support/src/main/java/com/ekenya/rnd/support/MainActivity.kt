package com.ekenya.rnd.support

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.ekenya.rnd.baseapp.BaseApp
import com.ekenya.rnd.common.abstractions.BaseActivity
import dagger.android.AndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    private val mViewModel by lazy {
//        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
//    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_support)

       // setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as BaseApp).supportFragmentInjector()
    }
}