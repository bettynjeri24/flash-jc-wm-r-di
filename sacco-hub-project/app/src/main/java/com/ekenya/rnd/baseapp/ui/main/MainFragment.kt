package com.ekenya.rnd.baseapp.ui.main

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ekenya.rnd.baseapp.BaseApp
import com.ekenya.rnd.baseapp.R
import com.ekenya.rnd.baseapp.databinding.FragmentMainBinding
import com.ekenya.rnd.baseapp.di.helpers.activities.ActivityHelperKt
import com.ekenya.rnd.baseapp.di.helpers.activities.AddressableActivity
import com.ekenya.rnd.baseapp.di.helpers.features.FeatureModule
import com.ekenya.rnd.baseapp.di.helpers.features.Modules
import com.ekenya.rnd.common.Utils.Companion.isEmulator
import com.ekenya.rnd.common.abstractions.BaseActivity
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : BaseActivity() {

    private lateinit var binding: FragmentMainBinding
    companion object {
        fun newInstance() = MainFragment()
    }

    private var mApp: BaseApp? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private val module by lazy {
        Modules.FeatureTijara.INSTANCE
    }

    private val splitInstallManager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.create(this)
    }

    private val listener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                setStatus("DOWNLOADING")
            }
            SplitInstallSessionStatus.INSTALLING -> {
                setStatus("INSTALLING")
            }
            SplitInstallSessionStatus.INSTALLED -> {

                // Enable module immediately
                this?.let { SplitCompat.install(it) }

                setStatus("${module.name} already installed\nPress start to continue ..")
                //
                /*binding.startButton.visibility = View.VISIBLE
                binding.startButton.setOnClickListener{
                    showFeatureModule(module)
                }*/
                    showFeatureModule(module)
                finishAffinity()

            }
            SplitInstallSessionStatus.FAILED -> {
                setStatus("FAILED")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            binding = FragmentMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            val data = mViewModel.getData()

            Log.i("MainFragment", "=> $data")
            mApp = this?.application as BaseApp

            if (splitInstallManager.installedModules.contains(module.toString())) {
                showFeatureModule(module)
//            setStatus("${module.name} already installed\nPress start to continue ..")
//            //
//            binding.startButton.visibility = View.VISIBLE
//            binding.startButton.setOnClickListener{
//                showFeatureModule(module)
//            }
                return
            }

            val request = SplitInstallRequest
                .newBuilder()
                .addModule(module.name)
                .build()

            splitInstallManager.startInstall(request)
            setStatus("Start install for ${module.name}")
       // }
    }

    override fun onResume() {
        super.onResume()
        splitInstallManager.registerListener(listener)
    }

    override fun onPause() {
        splitInstallManager.unregisterListener(listener)
        super.onPause()
    }

    private fun setStatus(label: String){
        //binding.status.text = label
       // Toast.makeText(context,label,Toast.LENGTH_SHORT).show()
        Log.d("LABEEEB", label)
    }
    /**
     *
     */
    private fun showFeatureModule(module: FeatureModule)
    {
        try {
            //Inject
            mApp!!.addModuleInjector(module)
            //

            this.startActivity(ActivityHelperKt.intentTo(this, module as AddressableActivity))
            //finish();
        } catch (e: Exception) {
            e.message?.let {
                Log.d("MainFragment", it) };
        }
    }
}