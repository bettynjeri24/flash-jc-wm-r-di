package io.eclectics.cargilldigital.ui.farmforcedeeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.ActivityFarmForceBuyerMainBinding
import io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel.FarmForceCargillViewModel
import io.eclectics.cargilldigital.utils.dk.FFUNIXTIMESTAMP
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FarmForceBuyerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFarmForceBuyerMainBinding

    @Inject
    lateinit var viewModel: FarmForceCargillViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val timestamp = System.currentTimeMillis() / 1000
        Timber.e("VIEWMODEL.UNIXTIMESTAMP <==> ${FFUNIXTIMESTAMP}")
        if (FFUNIXTIMESTAMP == 0L) {
            Timber.e("TIMESTAMP WHEN viewModel.unixTimestamp is 0l <==> $timestamp")
            FFUNIXTIMESTAMP = timestamp
        } else {
            Timber.e("TIMESTAMP WHEN viewModel.unixTimestamp is not 0 <==> $timestamp")
            FFUNIXTIMESTAMP = timestamp
        }
        Timber.e("TIMESTAMP <==> $timestamp")

        binding = ActivityFarmForceBuyerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //navigate to ussd
    fun navigateUSSDFrag(
        ussdCode: String,
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>
    ) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment?
        try {
            MainActivity.currentLiveData = respLiveData
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            var bundle = Bundle()
            bundle.putString("ussdcode", ussdCode)
            currentFragment!!.findNavController().navigate(R.id.nav_ussdFragment, bundle)
        } catch (ex: java.lang.Exception) {
        }
    }
}