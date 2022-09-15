package com.ekenya.rnd.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.TakeSelfieFragmentBinding
import kotlin.properties.Delegates

class TakeSelfieFragment : BaseDaggerFragment() {
    private var binding: TakeSelfieFragmentBinding? = null
    //private val scannerInterfaceImp: ScannerInterfaceImp = ScannerInterfaceImp()
    private lateinit var takeSelfieViewModel: TakeSelfieViewModel
    private var  isCaptured by Delegates.notNull<Boolean>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TakeSelfieFragmentBinding.inflate(layoutInflater)
        binding!!.closeIcon.setOnClickListener{
            findNavController().popBackStack()
        }

        takeSelfieViewModel =
            ViewModelProvider(this).get(TakeSelfieViewModel::class.java)
        takeSelfieViewModel.isCaptured.observe(viewLifecycleOwner, Observer {
            isCaptured = it
            if (it) {
                findNavController()
                    .navigate(R.id.action_takeSelfieFragment_to_finalDetailsVerificationFragment)

            }})
        binding!!.btnTakeSelfie.setOnClickListener{
           // scannerInterfaceImp.checkLiveliness(requireActivity(), livenessResultsCallback)


            Navigation.findNavController(it).navigate(R.id.action_takeSelfieFragment_to_finalDetailsVerificationFragment)
        }

        return binding!!.root    }



/*
    private val livenessResultsCallback: LivenessResults = object : LivenessResults {
        override fun isLive(result: Boolean) {
            try {


            if (result)
                */
/*Toast.makeText(activity, "Face is live", Toast.LENGTH_SHORT)
                .show()*//*

                takeSelfieViewModel.isCaptured(true)


            else
            {
                Toast.makeText(
                context,
                "Face is not live",
                Toast.LENGTH_SHORT
            ).show()}

        }catch (e:Exception){
                Log.d("testingliveness", "isLive: $e")

        }}

        override fun checkFailed(error: String) {
            Toast.makeText(
                context,
                "Liveness check has failed $error",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun faceImage(face: Bitmap) {
           */
/* ivFacePrev.setVisibility(View.GONE)
            tvInstr.setVisibility(View.GONE)
            ivUserImg.setImageBitmap(face)
            faceBitmap = face*//*



        }
    }
*/


}