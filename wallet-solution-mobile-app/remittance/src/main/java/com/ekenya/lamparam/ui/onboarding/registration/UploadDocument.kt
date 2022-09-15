package com.ekenya.lamparam.ui.onboarding.registration

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.FragmentUploadDocumentBinding
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.utilities.FieldValidation
import com.ekenya.lamparam.utilities.GlobalMethods
import kotlinx.android.synthetic.main.fragment_upload_document.*
import kotlinx.android.synthetic.main.header_layout.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UploadDocument : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //initialized for all fragments
    private val onboardingViewModel: OnBoardingViewModel by activityViewModels() { viewModelFactory }

    @Inject
    lateinit var globalMethods: GlobalMethods

    private var _binding: FragmentUploadDocumentBinding? = null
    private val binding get() = _binding!!

    private lateinit var fullName: String
    private lateinit var phoneNumber: String
    private lateinit var dob: String
    private lateinit var address: String
    private lateinit var docType: String
    private lateinit var docNumber: String

    private var frontIDImg: Bitmap? = null
    private var backIDImg: Bitmap? = null

    private lateinit var currentPhotoPath: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE = 200

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = resources.getText(R.string.str_doc_photo)
        ((activity as OnBoardingActivity).onBackClick(btn_back, view)) //back button

        binding.btnUploadDoc.setOnClickListener {
            if (validateFields()) {
                onboardingViewModel.setUserIDInfo(docNumber, docType)

                it.findNavController().navigate(
                    R.id.nav_alternativeData,
                    null,
                    null
                )
            }
        }

        val list = resources.getStringArray(R.array.docs_array_values)
        val adapter: ArrayAdapter<String> =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, list) }!!
        binding.etIdType.setAdapter(adapter)
        binding.etIdType.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.etIdType.showDropDown()
            false
        })

        binding.ivFrontId.setOnClickListener {
            Toast.makeText(requireContext(), "coming soon", Toast.LENGTH_SHORT).show()
//            if (checkPermission()) {
//                dispatchTakePictureIntent()
//            } else {
//                requestPermission()
//            }
        }
        binding.ivBackId.setOnClickListener {
            Toast.makeText(requireContext(), "coming soon", Toast.LENGTH_SHORT).show()
//            if (checkPermission()) {
//            dispatchTakePictureIntent()
//        } else {
//            requestPermission()
//        }
        }

    }

    /**
     * Checks if user has accepted camera permission
     */
    private fun checkPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Requests the camera permission
     */
    private fun requestPermission() = requestPermissions(
        arrayOf(
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION
        ), PERMISSION_REQUEST_CODE
    )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        globalMethods.showMessageOKCancel(requireActivity(),"You need to allow access permissions",
                            SweetAlertDialog.OnSweetClickListener {
                                it.dismiss()
                                it.setConfirmClickListener {
                                    requestPermission()
                                }
                            })
                    }
                }
            }
        }
    }

    /**
     * Method dispatches photo intent
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Toast.makeText(requireContext(), "Failed to capture photo", Toast.LENGTH_SHORT)
                        .show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.ekenya.lamparam.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    requireActivity().startActivityForResult(
                        takePictureIntent,
                        REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }
    }

    /**
     * Creates an image file for storing the photo captured
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            selfieImg = data?.extras?.get("data") as Bitmap
//            if (selfieImg != null)
//                setPic()
        }
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = binding.ivFrontId.width
        val targetH: Int = binding.ivFrontId.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            binding.ivFrontId.setImageBitmap(bitmap)
        }
    }

    private fun validateFields(): Boolean {
        val validMsg = FieldValidation.VALIDINPUT
        docNumber = binding.etNin.text.toString()
        docType = binding.etIdType.text.toString()

        val validIDNumber =
            FieldValidation().validIdNumber(docNumber, "ID Number")

        if (!validIDNumber.contentEquals(validMsg)) {
            binding.etNin.requestFocus()
            tlNationalID.error = validIDNumber
            return false
        } else {
            tlNationalID.error = null
            binding.etNin.clearFocus()
        }

        if(docType.isEmpty()){
            Toast.makeText(requireContext(), "Please select document type", Toast.LENGTH_SHORT).show()
            return false
        }

        docType = when(docType){
            "National ID" -> "id"
            "Passport" -> "pp"
            else -> "dl"
        }

        return true
    }

}