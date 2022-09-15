package com.ekenya.rnd.cargillbuyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.cargillbuyer.databinding.ActivityMainBuyerBinding
import com.ekenya.rnd.common.abstractions.BaseActivity
import dagger.android.AndroidInjector
import javax.inject.Inject

class CargillBuyerMainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityMainBuyerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBuyerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as CargillApp).supportFragmentInjector()
    }
}
