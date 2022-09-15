package io.eclectics.cargilldigital.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import io.eclectics.agritech.cargill.ui.splash.SplashViewModel
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargilldigital.databinding.SplashFragmentBinding
import java.math.BigInteger

class SplashFragment : Fragment() {

    private lateinit var viewModel: SplashViewModel

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!

    // Animation
    private lateinit var animRotate: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        animRotate = AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate)
        animRotate.fillAfter = true
        binding.ivLoader.startAnimation(animRotate)
        //LoggerHelper.loggerError("timesutc","time ${NetworkUtility().dateToUTC()}")

        viewModel.eventTimeExpired.observe(viewLifecycleOwner, Observer {
            if (it)
                navigate()
        })
        testBigInteger()

    }

    private fun testBigInteger() {
        var mainStrinfgtest = "34fr87cx1o" //from 10 two 24
        val inputStringBytes: ByteArray = mainStrinfgtest.toByteArray()
        val result = BigInteger(inputStringBytes)
        LoggerHelper.loggerError("bigints","$result and ${ String(result.toByteArray())}")
        var dates = NetworkUtility().getUTCtime()
        LoggerHelper.loggerError("dates","dates-$dates")
    }

    private fun navigate(){
        view?.findNavController()?.navigate(R.id.nav_setLanguage)
        viewModel.hasNavigated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}