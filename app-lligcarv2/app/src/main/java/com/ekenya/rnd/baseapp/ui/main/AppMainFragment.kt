package com.ekenya.rnd.baseapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.baseapp.databinding.FragmentMainBinding
import com.ekenya.rnd.baseapp.di.helpers.activities.ActivityHelperKt
import com.ekenya.rnd.baseapp.di.helpers.activities.AddressableActivity
import com.ekenya.rnd.baseapp.di.helpers.features.FeatureModule
import com.ekenya.rnd.baseapp.di.helpers.features.Modules
import com.ekenya.rnd.baseapp.ui.bixolon.BixolonPrinterActivity
import com.ekenya.rnd.baseapp.ui.deeplinking.FarmForceSenderMainActivity
import com.ekenya.rnd.baseapp.ui.offlineussd.OfflineUssdActivity
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class AppMainFragment : Fragment() {
    private var mApp: CargillApp? = null
    private var splitInstallManager: SplitInstallManager? = null
    private lateinit var binding: FragmentMainBinding

    companion object {
        fun newInstance() = AppMainFragment()
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private val moduleCargillAuth by lazy {
        Modules.FeatureCargillAuth.INSTANCE
    }
    private val moduleCargillFarmer by lazy {
        Modules.FeatureCargillFarmer.INSTANCE
    }

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        splitInstallManager = SplitInstallManagerFactory.create(requireActivity())
        //
        mApp = requireActivity().application as CargillApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDeepLinking.text = "FARM FORCE"
        binding.btnDeepLinking.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    FarmForceSenderMainActivity::class.java
                )
            )
            // requireActivity().finish()
        }

        binding.btnCargillApp.text = "CARGILL APP"
        binding.btnCargillApp.setOnClickListener {
            showFeatureModule(moduleCargillAuth)
        }

        binding.btnPos.text = "POS"
        binding.btnPos.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    BixolonPrinterActivity::class.java
                )
            )
        }
        binding.btnOfflineUssd.text = "Offline ussd"
        binding.btnOfflineUssd.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    OfflineUssdActivity::class.java
                )
            )
        }

        binding.btnCoopApp.text = "Coop App"
        binding.btnCoopApp.setOnClickListener {
            showFeatureModule(Modules.FeatureCargillCooperative.INSTANCE)
        }
        // setSplitInstallManager()
    }

    //
    private fun setSplitInstallManager() {
        if (splitInstallManager!!.installedModules.contains(moduleCargillFarmer.toString())) {
            setStatus("Modules Ready\nPress login to continue ..")
            showFeatureModule(moduleCargillFarmer)
            return
        }

        val request = SplitInstallRequest
            .newBuilder()
            .addModule(moduleCargillFarmer.name)
            .build()

        splitInstallManager!!.startInstall(request)

        setStatus("Start install for $moduleCargillFarmer.name")
    }

    /**
     *
     */
    private fun showFeatureModule(module: FeatureModule, actionTo: String) {
        try {
            // Inject
            mApp!!.addModuleInjector(module)
            //

            val intent = ActivityHelperKt.intentTo(
                activity,
                Modules.FeatureCargillFarmer.INSTANCE as AddressableActivity
            )
            intent.action = actionTo
            this.startActivity(
                intent
            )
        } catch (e: Exception) {
            e.message?.let { Timber.d("MainFragment %s", it) }
        }
    }

    //
    private fun showFeatureModule(module: FeatureModule) {
        try {
            // Inject
            mApp!!.addModuleInjector(module)
            //
            val intent = ActivityHelperKt.intentTo(requireActivity(), module as AddressableActivity)
            //
            this.startActivity(intent)
            requireActivity().finishAffinity()
        } catch (e: Exception) {
            e.message?.let { Log.d("SplashActivity", it) }
        }
    }

    //
    private val listener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> //
                setStatus("DOWNLOADING")
            SplitInstallSessionStatus.INSTALLING -> //
                setStatus("INSTALLING")
            SplitInstallSessionStatus.INSTALLED -> {
                //
                // Enable module immediately
                SplitCompat.install(requireActivity())
                setStatus("INSTALLED")
            }
            SplitInstallSessionStatus.FAILED -> //
                setStatus("FAILED")
        }
    }

    //
    override fun onResume() {
        super.onResume()
        splitInstallManager!!.registerListener(listener)
    }

    //
    override fun onPause() {
        splitInstallManager!!.unregisterListener(listener)
        super.onPause()
    }

    //
    private fun setStatus(label: String) {
        Timber.e("========================== $label  ==========================")
    }
}
