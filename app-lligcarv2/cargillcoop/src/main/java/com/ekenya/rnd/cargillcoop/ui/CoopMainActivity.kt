package com.ekenya.rnd.cargillcoop.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.cargillcoop.databinding.ActivityMainCoopBinding
import com.ekenya.rnd.common.abstractions.BaseActivity
import dagger.android.AndroidInjector
import javax.inject.Inject

class CoopMainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainCoopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainCoopBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as CargillApp).supportFragmentInjector()
    }
}
