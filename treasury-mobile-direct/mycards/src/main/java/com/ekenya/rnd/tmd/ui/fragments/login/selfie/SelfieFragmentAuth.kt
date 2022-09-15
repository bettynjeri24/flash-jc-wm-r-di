package com.ekenya.rnd.tmd.ui.fragments.login.selfie

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.databinding.FragmentAcctSelfieBinding
import com.ekenya.rnd.scannerlib.ScannerInterfaceImp
import com.ekenya.rnd.tmd.utils.GlobalMethods
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [SelfieFragmentAuth] factory method to
 * create an instance of this fragment.
 */
class SelfieFragmentAuth : BaseDaggerFragment() {

    private lateinit var binding: FragmentAcctSelfieBinding

    @Inject
    lateinit var globalMethods: GlobalMethods

    private val scannerInterfaceImp = ScannerInterfaceImp()

    private var faceBitmap: Bitmap? = null

    // initialized for all fragments

    private val livenessResultsCallback: ScannerInterfaceImp.LivenessResults = object :
        ScannerInterfaceImp.LivenessResults {
        override fun isLive(result: Boolean) {
            if (result) {
                binding.groupSuccess.isVisible = true
                Toast.makeText(requireContext(), "Face is live", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Face is not live", Toast.LENGTH_SHORT).show()
            }
        }

        override fun checkFailed(error: String) {
            Toast.makeText(requireContext(), "Liveness check has failed $error", Toast.LENGTH_SHORT)
                .show()
        }

        override fun faceImage(face: Bitmap) {
            binding.apply {
                animationView.visibility = View.GONE
                btnRecaptureCapture.visibility = View.VISIBLE
                faceBitmap = face
                ivSelfie.setImageBitmap(faceBitmap)
                cvImage.visibility = View.VISIBLE

                btnSelfieCapture.text = "Proceed"
                btnSelfieCapture.setOnClickListener {
                    if (faceBitmap == null) {
                        Toast.makeText(
                            requireContext(),
                            "Capture selfie first",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    }
                    faceBitmap = globalMethods.getResizedBitmap(faceBitmap!!, 100)
                    lifecycleScope.launch {
                        showHideProgress("Authenticating Face id")
                        delay(3000)
                        showHideProgress(null)
                        navigate(faceBitmap!!)
                    }
                }
            }
        }
    }

    /**
     * Handles navigation to previous screen and returns image in the process
     */
    private fun navigate(selfie: Bitmap) {
        // Setting the result in the stateHandle of the previousBackStackEntry before navigating back to Fragment A
        // will allow Fragment A to access the result in the stateHandle of its currentBackStackEntry
        val navController = findNavController()
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set("selfie", selfie)
//        setFragmentResult("requestKey", bundleOf("voice" to VOICE.SUCCESS))

        navController.navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAcctSelfieBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()

        binding.apply {
            btnSelfieCapture.setOnClickListener {
                checkLiveness()
            }

            btnRecaptureCapture.setOnClickListener {
                checkLiveness()
            }
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun checkLiveness() {
        scannerInterfaceImp.checkLiveliness(requireActivity(), livenessResultsCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) Toast.makeText(
                requireContext(),
                "Permission Granted",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
