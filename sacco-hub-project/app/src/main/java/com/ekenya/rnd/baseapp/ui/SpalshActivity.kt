package com.ekenya.rnd.baseapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ekenya.rnd.baseapp.R
import com.ekenya.rnd.baseapp.BaseApp
import com.ekenya.rnd.baseapp.databinding.ActivitySplashBinding
import com.ekenya.rnd.baseapp.ui.main.MainFragment
import com.ekenya.rnd.baseapp.ui.main.MainViewModel
import com.ekenya.rnd.common.Utils.Companion.isEmulator
import com.ekenya.rnd.common.abstractions.BaseActivity
import com.scottyab.rootbeer.RootBeer
import dagger.android.AndroidInjector
import javax.inject.Inject


class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    private var mApp: BaseApp? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreenTheme)
        //
        mApp = application as BaseApp
        /**check for rooted devices*/
       // val rootBeer = RootBeer(this)
       /* if (rootBeer.isRooted) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(title)
            builder.setMessage(getString(R.string.root_message))
            builder.setPositiveButton(getString(R.string.ok)) { _, which ->
                finishAffinity()
            }
            builder.setCancelable(false)
            builder.show()
        } else if (isEmulator()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(title)
            builder.setMessage(getString(R.string.emulator_message))
            builder.setPositiveButton(getString(R.string.ok)) { _, which ->
                finishAffinity()
            }
            builder.setCancelable(false)
            builder.show()
        } else {*/
            binding = ActivitySplashBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val data = mViewModel.getData()

            Log.i("SplashActivity", "=> $data")
            //
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                 //   .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
            }
      //  }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as BaseApp).supportFragmentInjector()
    }


}