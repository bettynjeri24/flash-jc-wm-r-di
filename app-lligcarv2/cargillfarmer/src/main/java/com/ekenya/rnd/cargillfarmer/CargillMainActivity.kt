package com.ekenya.rnd.cargillfarmer

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.cargillfarmer.databinding.ActivityMainCargillBinding
import com.ekenya.rnd.common.utils.custom.LogOutTimerUtil
import com.ekenya.rnd.common.utils.custom.RootUtil
import com.ekenya.rnd.common.abstractions.BaseActivity
import dagger.android.AndroidInjector
import timber.log.Timber

class CargillMainActivity : BaseActivity(), LogOutTimerUtil.LogOutListener, LifecycleObserver {
    private lateinit var binding: ActivityMainCargillBinding

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCargillBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

   /* private fun performCustomNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.mobile_navigation)
        when (intent.action) {
            Modules.FeatureCargillFarmer.WalletAction.ACTION_FARMER.toString() -> {
                graph.setStartDestination(R.id.farmerHomeFragment)
            }
            else -> {
                Timber.e("MainActivity====> HomeFragment")
                graph.setStartDestination(R.id.farmerHomeFragment)
            }
        }
        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }*/


    private fun avoidRunningOnRootedDevice() {
        /*check for rooted devices*/
        if (RootUtil.isEmulator) {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Device is Emulator")
            builder.setMessage(getString(com.ekenya.rnd.common.R.string.emulator_message))
            builder.setPositiveButton(getString(com.ekenya.rnd.common.R.string.ok)) { dialog, which ->
                dialog.dismiss()
                this.finishAffinity()
            }
            builder.setCancelable(false)
            builder.show()
        } else if (RootUtil.isDeviceRooted) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Device is Rooted")
            builder.setMessage(getString(com.ekenya.rnd.common.R.string.root_message))
            builder.setPositiveButton(getString(com.ekenya.rnd.common.R.string.ok)) { dialog, which ->
                dialog.dismiss()
                this.finishAffinity()
            }
            builder.setCancelable(false)
            builder.show()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Fragment Injector should use the Application class
        // If necessary, I will use AndroidInjector as well as App class (I have not done this time)
        return (application as CargillApp).supportFragmentInjector()
    }

    override fun doLogout() {
        LogOutTimerUtil.stopLogoutTimer()
        //startActivity(Intent(this, CargillMainActivity::class.java))
        //this.finish()
    }

    override fun onStart() {
        super.onStart()
        LogOutTimerUtil.startLogoutTimer(this, this)
        Timber.e("OnStart () &&& Starting timer")
        // register observer
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        LogOutTimerUtil.startLogoutTimer(this, this)
        Timber.e("User interacting with screen")
    }


    override fun onPause() {
        /*    this.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )*/
        super.onPause()
    }

}