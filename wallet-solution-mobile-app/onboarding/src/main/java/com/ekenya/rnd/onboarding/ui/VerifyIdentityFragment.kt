package com.ekenya.rnd.onboarding.ui

import android.Manifest
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.VerifyIdentityFragmentBinding
/*import com.ekenya.rnd.scannerlib.ScannerInterface
import com.ekenya.rnd.scannerlib.ScannerInterfaceImp
import com.ekenya.rnd.scannerlib.models.IdScanResult*/


class VerifyIdentityFragment : BaseDaggerFragment() {

    private lateinit  var binding: VerifyIdentityFragmentBinding

   // private val scannerInterfaceImp: ScannerInterfaceImp = ScannerInterfaceImp()
    val PERMISSION_ALL = 1
    val PERMISSIONS = arrayOf(

        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

   /* private val cardResultsCallback: ScannerInterfaceImp.CardResults = object :
        ScannerInterfaceImp.CardResults {
        override fun onCardResult(scanResult: IdScanResult) {

            Log.d(TAG, "onCardResult: $scanResult")
           *//* if (side == "front") { //SIDE is the side you want to caoture
//                scanResult.details
//                scanResult.card

            }

            else if (side == "back") {
                registrationViewModel.setBackID(scanResult.card)
                binding.btnStartCapture.text = "Proceed"
            }
            if (scanResult.face != null) {
                registrationViewModel.setCardFace(scanResult.face)
            }*//*
        }
        override fun onCardFailure(error: String) {
            Toast.makeText(activity, "Errorzz : $error", Toast.LENGTH_LONG).show()


          //  binding?.let { Navigation.findNavController(it.btnVerifyIdentity).navigate(R.id.action_verifyIdentityFragment_to_manualVerificationFragment) }

        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VerifyIdentityFragmentBinding.inflate(layoutInflater)


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        val firstName = context?.let { SharedPreferencesManager.getFirstName(it) }
        binding.tvLetsVerifyidentity.text = "Let's Verify your identity \n $firstName!"




        binding.btnVerifyIdentity
            .setOnClickListener{

                showManualVerificationDialog()

                //binding?.let { Navigation.findNavController(it.btnVerifyIdentity).navigate(R.id.action_verifyIdentityFragment_to_manualVerificationFragment) }


                //Check for camera permission otherwise initialise application


            }
        return binding.root
    }

    private fun captureID() {

        //Check for camera permission otherwise initialise application
        /*if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        )
            requestCameraPermission()
        else {
            registrationViewModel.getRegistrationDetails().observe(viewLifecycleOwner, {
                it.let {
                    regData = it
                }
            }
            if (regData!!.backIDImage.isNotEmpty() && regData!!.frontIDImage.isNotEmpty()) {
                findNavController().navigate(R.id.action_captureIdFragment_to_idDetailsFragment)
            }
            else {*/
                //showBottomSheet()

      //  scannerInterfaceImp.scanIdCard(requireActivity(), ScannerInterface.CardType.IDCARD, cardResultsCallback)
           // })
        }


    private fun requestCameraPermission() {

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
                == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(requireActivity(),  arrayOf(Manifest.permission.CAMERA), 5);
            } else {
                captureID()
                Toast.makeText(requireContext(), "capture id started", Toast.LENGTH_LONG).show()


            }

           //Check for camera permission otherwise initialise application
               if (context?.let { it1 ->
                       ActivityCompat.checkSelfPermission(
                           it1,
                           Manifest.permission.CAMERA
                       )
                   } != PackageManager.PERMISSION_GRANTED
               ){
                   captureID()
               }else{
                   captureID()
           }
        Toast.makeText(requireContext(), "request permission method", Toast.LENGTH_LONG).show()



    }



    fun showManualVerificationDialog( ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.manual_verification_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogbtnManual = dialog.findViewById<Button>(R.id.btn_manualverification)
        val dialogBtn_remove = dialog.findViewById<Button>(R.id.btn_scanID)
        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener{
            dialog.dismiss()
        }
        dialogBtn_remove.setOnClickListener {
            //activity!!.finish()
            dialog.dismiss()
            if (!hasPermissions(requireContext(), *PERMISSIONS)) {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


            }else {
             //   scannerInterfaceImp.scanIdCard(requireActivity(), ScannerInterface.CardType.IDCARD, cardResultsCallback)


            }


        }
        dialogbtnManual.setOnClickListener {
            Navigation.findNavController(binding.btnVerifyIdentity).navigate(R.id.action_verifyIdentityFragment_to_manualVerificationFragment)
            dialog.dismiss()

        }
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ChangeActionBarandBackArrowColor("#100fd6")
    }

    override fun onStop() {
        super.onStop()
        makeStatusBarTransparent()
       // showSupportActionBar()
    }

    override fun onResume() {
        super.onResume()
        extendStatusBarBackground()
        changeActionbarColor(Color.parseColor("#ffee1a23"))

    }




}