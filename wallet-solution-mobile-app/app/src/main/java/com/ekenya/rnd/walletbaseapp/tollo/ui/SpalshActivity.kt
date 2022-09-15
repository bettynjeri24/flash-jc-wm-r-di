package com.ekenya.rnd.walletbaseapp.tollo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ekenya.rnd.common.abstractions.BaseActivity
import com.ekenya.rnd.walletbaseapp.R
import com.ekenya.rnd.walletbaseapp.tollo.WalletSolutionApp
import com.ekenya.rnd.walletbaseapp.databinding.ActivitySplashBinding
import com.ekenya.rnd.walletbaseapp.tollo.ui.main.MainFragment
import com.ekenya.rnd.walletbaseapp.tollo.ui.main.MainViewModel
import dagger.android.AndroidInjector
import javax.inject.Inject


class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    private var mApp: WalletSolutionApp? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        setTheme(R.style.MariWalletTheme);

        mApp = application as WalletSolutionApp
        binding = ActivitySplashBinding.inflate(layoutInflater)
  //      installSplashScreen()

        setContentView(binding.root)

        val data = mViewModel.getData()

        Log.i("SplashActivity", "=> $data")
        //
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as WalletSolutionApp).supportFragmentInjector()
    }


}