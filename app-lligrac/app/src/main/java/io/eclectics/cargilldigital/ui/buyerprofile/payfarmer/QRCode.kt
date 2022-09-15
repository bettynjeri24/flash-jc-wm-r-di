package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentQrCodeBinding
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class QRCode : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var scannerView: CodeScannerView
    @Inject
    lateinit var navOptions: NavOptions
    var mPermissionGranted by Delegates.notNull<Boolean>()
    private var _binding: FragmentQrCodeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrCodeBinding.inflate(inflater, container, false)
        ToolBarMgmt.setPayfarmerToolbarTitle(resources.getString(R.string.pay_farmer),resources.getString(R.string.pay_or_scan),binding.mainLayoutToolbar,requireActivity())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)


        try {

            cameraPermision()
            startScanner(view)
            codeScanner = CodeScanner(requireActivity(), scannerView)

            /*val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
            val activity = requireActivity()
            codeScanner = CodeScanner(activity, scannerView)
            codeScanner.decodeCallback = DecodeCallback {
                activity.runOnUiThread {
                    Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                    Log.e("test", "maintest ${it.text}")
                    //processScannedText(it.text)
                    var scanBundle = Bundle()
                    scanBundle.putString("scanned", it.text)
                    findNavController().navigate(R.id.nav_scannedCoolerInfo, scanBundle, navOptions)
                }
            }*/
            scannerView.setOnClickListener {
                codeScanner.startPreview()
                //findNavController().navigate(R.id.nav_scannedCoolerInfo,null,navOptions)
            }
        }catch (ex:Exception){}
    }

    fun startScanner(view: View){
        //val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                Log.e("test", "maintest ${it.text}")
                //processScannedText(it.text)
                var scanBundle = Bundle()
                scanBundle.putString("scanned", it.text)
                findNavController().navigate(R.id.nav_qrCodeDetails, scanBundle, navOptions)
            }
        }
    }
    private fun processScannedText(scannedText: String?) {

    }

    override fun onResume() {
        super.onResume()
        // codeScanner = CodeScanner(requireActivity(), scannerView)
        if (mPermissionGranted) {
            codeScanner.startPreview()
        }
        //cameraPermision()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
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
                mPermissionGranted = false
            }
            else{
                //permission already granted
                //dispatchTakePictureIntent()
                // previewScanner()
                mPermissionGranted = true
            }
        }
        else{
            //system os is < marshmallow
            //dispatchTakePictureIntent()
            //  previewScanner()
            mPermissionGranted = true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    // previewScanner()
                    mPermissionGranted = true
                }
                else{
                    //permission from popup was denied
                    mPermissionGranted = false
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

/*
    fun previewScanner(){
        val activity = requireActivity()
        var codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        //codeScanner.startPreview()
    }
*/
}