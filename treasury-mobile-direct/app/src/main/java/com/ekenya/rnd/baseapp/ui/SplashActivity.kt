package com.ekenya.rnd.baseapp.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ekenya.rnd.baseapp.TMDApp
import com.ekenya.rnd.baseapp.databinding.ActivitySplashBinding
import com.ekenya.rnd.baseapp.di.helpers.activities.ActivityHelperKt
import com.ekenya.rnd.baseapp.di.helpers.activities.AddressableActivity
import com.ekenya.rnd.baseapp.di.helpers.features.FeatureModule
import com.ekenya.rnd.baseapp.di.helpers.features.Modules
import com.ekenya.rnd.common.abstractions.BaseActivity
import dagger.android.AndroidInjector
import kotlinx.coroutines.delay
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var mApp: TMDApp

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val moduleCards by lazy {
        Modules.FeatureMyCards.INSTANCE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        lifecycleScope.launchWhenResumed {
            delay(500)
            showFeatureModule(moduleCards)
        }
    }

    override fun onResume() {
        super.onResume()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    private fun showFeatureModule(module: FeatureModule) {
        try {
            mApp.addModuleInjector(module)
            val intent = ActivityHelperKt.intentTo(this, module as AddressableActivity)
            this.startActivity(intent)
        } catch (e: Exception) {
            e.message?.let { Log.d("SplashActivity", it) }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return (application as TMDApp).supportFragmentInjector()
    }
}
