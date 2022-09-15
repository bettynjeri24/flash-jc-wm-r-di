package com.ekenya.lamparam.ui.paymerchant

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.fragment_paymerchant.*
import kotlinx.android.synthetic.main.header_layout.*

class Paymerchant : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var scannerView: CodeScannerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_paymerchant, container, false)

        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Pay Merchant"
        ((activity as LampMainActivity). onBackClick(btn_back,view))
//        tvScanQR.setOnClickListener {
//            tvScanQR.setBackgroundColor(resources.getColor(R.color.colorWhite))
//            tvEnterMerchantID.setBackgroundColor(resources.getColor(R.color.colorPrimary))
//            scanner_view.visibility = View.VISIBLE
//            tlMerchantID.visibility = View.GONE
//            btnSearchID.visibility = View.GONE
//        }
//        tvEnterMerchantID.setOnClickListener {
//            tvScanQR.setBackgroundColor(resources.getColor(R.color.colorPrimary))
//            tvEnterMerchantID.setBackgroundColor(resources.getColor(R.color.colorWhite))
//            scanner_view.visibility = View.GONE
//            tlMerchantID.visibility = View.VISIBLE
//            btnSearchID.visibility = View.VISIBLE
//        }
        btnSearchID.setOnClickListener { findNavController().navigate(R.id.nav_merchantAmount) }
        //nav_merchantAmount
//        cameraPermision()
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
//        val activity = requireActivity()
//        codeScanner = CodeScanner(activity, scannerView)
//        codeScanner.decodeCallback = DecodeCallback {
//            activity.runOnUiThread {
//                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
//                Log.e("test", "maintest ${it.text}")
//                findNavController().navigate(R.id.nav_scanPayDetails)
//                //nav_scanPayDetails
//            }
//        }
        scannerView.setOnClickListener {
            Toast.makeText(requireContext(), "coming soon", Toast.LENGTH_SHORT).show()
//            codeScanner.startPreview()
//            findNavController().navigate(R.id.nav_scanPayDetails)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        codeScanner.startPreview()
//        //cameraPermision()
//    }
//
//    override fun onPause() {
//        codeScanner.releaseResources()
//        super.onPause()
//    }

    fun cameraPermision(){
        //if system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED){
                //permission was not enabled
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            }
            else{
                //permission already granted
                //dispatchTakePictureIntent()
                // previewScanner()
            }
        }
        else{
            //system os is < marshmallow
            //dispatchTakePictureIntent()
            //  previewScanner()
        }

    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        //called when user presses ALLOW or DENY from Permission Request Popup
//        when(requestCode){
//            PERMISSION_CODE -> {
//                if (grantResults.size > 0 && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED){
//                    //permission from popup was granted
//                    // previewScanner()
//                }
//                else{
//                    //permission from popup was denied
//                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
}