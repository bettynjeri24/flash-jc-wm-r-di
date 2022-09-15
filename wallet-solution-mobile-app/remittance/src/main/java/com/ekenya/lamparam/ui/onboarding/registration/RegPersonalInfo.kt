package com.ekenya.lamparam.ui.onboarding.registration

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.databinding.FragmentRegPersonalInfoBinding
import com.ekenya.lamparam.utilities.FieldValidation
import com.ekenya.lamparam.utilities.GlobalMethods
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_reg_personal_info.*
import kotlinx.android.synthetic.main.header_layout.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class RegPersonalInfo : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //initialized for all fragments
    private val onboardingViewModel: OnBoardingViewModel by activityViewModels() { viewModelFactory }

    @Inject
    lateinit var globalMethods: GlobalMethods

    private val myCalendar: Calendar = Calendar.getInstance()

    private var fullName = ""
    private var selfieImg: Bitmap? = null
    private var dob = ""
    private var address = ""

    private lateinit var navOptions: NavOptions
    private lateinit var currentPhotoPath: String

    private var _binding: FragmentRegPersonalInfoBinding? = null
    private val binding get() = _binding!!

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE = 200

    /**
     * Date dialog listener
     */
    private var date =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegPersonalInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = resources.getString(R.string.str_personal_info)
        ((activity as OnBoardingActivity).onBackClick(btn_back, view)) //back button
        navOptions = UtilityClass().getNavoptions()

        val args = requireArguments()
        val agreeTerms = args.getBoolean("accept", false)
        binding.cbTerms.isChecked = agreeTerms

        btnCreateAcc.setOnClickListener {
            if (validateFields()) {
                onboardingViewModel.setPersonalInfo(fullName, dob, address, selfieImg)

                it.findNavController().navigate(
                    R.id.nav_uploadDoc,
                    null,
                    navOptions
                )
            }
        }

        binding.etDob.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.tvAcceptTermsAndConditions.setOnClickListener {
            it.findNavController().navigate(
                R.id.policyFragment,
                null,
                navOptions
            )
        }

        binding.ivSelfie.setOnClickListener {
            Toast.makeText(requireContext(), "coming soon", Toast.LENGTH_SHORT).show()
//            if (checkPermission()) {
//                dispatchTakePictureIntent()
//            } else {
//                requestPermission()
//            }
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
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        globalMethods.showMessageOKCancel(requireActivity(),
                            "You need to allow access permissions",
                            SweetAlertDialog.OnSweetClickListener {
                                it.dismiss()
                                requestPermission()
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
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            selfieImg = data?.extras?.get("data") as Bitmap
            if (selfieImg != null)
                setPic()
        }
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = binding.ivSelfie.width
        val targetH: Int = binding.ivSelfie.height

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
            binding.ivSelfie.setImageBitmap(bitmap)
        }
    }

    private fun validateFields(): Boolean {
        val validMsg = FieldValidation.VALIDINPUT
        fullName = binding.etFullName.text.toString()
        address = binding.etAddress.text.toString()
        dob = binding.etDob.text.toString()

        val validName =
            FieldValidation().validName(fullName, "name")

        if (!validName.contentEquals(validMsg)) {
            binding.etFullName.requestFocus()
            tlFullNames.error = validName
            return false
        } else {
            tlFullNames.error = null
            binding.etFullName.clearFocus()
        }

        if (!binding.cbTerms.isChecked) {
            Toast.makeText(
                requireContext(),
                getString(R.string.please_accept_terms),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

//        if (selfieImg == null) {
//            Toast.makeText(
//                requireContext(),
//                getString(R.string.capture_selfie_img),
//                Toast.LENGTH_SHORT
//            ).show()
//            return false
//        }

        return true
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etDob.setText(sdf.format(myCalendar.time))
    }
}