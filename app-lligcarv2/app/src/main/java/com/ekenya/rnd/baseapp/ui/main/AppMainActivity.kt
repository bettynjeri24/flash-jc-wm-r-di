package com.ekenya.rnd.baseapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.ekenya.rnd.baseapp.AppMainViewModel
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.baseapp.R
import com.ekenya.rnd.baseapp.databinding.ActivitySplashBinding
import com.ekenya.rnd.common.abstractions.BaseActivity
import dagger.android.AndroidInjector
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var mViewModel: AppMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainAppLayout, AppMainFragment.newInstance())
                .commitNow()
        }

        val data = mViewModel.getData()

        Log.i("SplashActivity", "=> $data")
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as CargillApp).supportFragmentInjector()
    }
}
