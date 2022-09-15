package com.ekenya.rnd.onboarding.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.Constants.CAMERA
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.CapturePhotosFragmentBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException

class CapturePhotosFragment : BaseDaggerFragment() {
    private lateinit var binding: CapturePhotosFragmentBinding
    private var isCaptured: Boolean = false
    private lateinit var fontImageView: ImageView
    private lateinit var frontFile: File
    private lateinit var backFile: File
    private lateinit var frontInputFile: File
    private lateinit var backInputFile: File
    private lateinit var frontDestinationUri: Uri
    private lateinit var backDestinationUri: Uri
    private var currentFrontPhotoPath: String? = null
    private var currentBackPhotoPath: String? = null
    private val captureFrontImage = 1
    private val captureBackImage = 2
    private var currentCapture = 0
    val PERMISSION_ALL = 1
    val PERMISSIONS = arrayOf(

        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CapturePhotosFragmentBinding.inflate(layoutInflater)


        initUI()


        if (!hasPermissions(requireContext(), *PERMISSIONS)) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


        }


        val mContextWrapper = ContextWrapper(requireContext())
        val mDirectory: File = mContextWrapper.getDir(
            "tempImage",
            Context.MODE_PRIVATE
        )

        frontDestinationUri = Uri.parse(File(mDirectory, AppUtils.uniqueFileName()).path)
        backDestinationUri = Uri.parse(File(mDirectory, AppUtils.uniqueFileName()).path)

        setOnclickListeners()


        /*val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)*/





        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        val documentSelected = arguments?.getString("documentSelected")
        binding.tvManualVerification.text = "Take photos of your $documentSelected"

        if (documentSelected != "National ID") {
            binding.tvCaptureFront.text = "Tap to Capture Front of Your $documentSelected"
            binding.tvCaptureBack.text = "Tap to Capture Back of Your ${documentSelected}"
        } else {
            binding.tvCaptureFront.text = "Tap to Capture Front Side of Your ${documentSelected}"
            binding.tvCaptureBack.text = "Tap to Capture Back Side of Your ${documentSelected}"

        }
    }

    private fun setOnclickListeners() {
        binding.tvRetakePhoto.setOnClickListener {
            if (!hasPermissions(requireContext(), *PERMISSIONS)) {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


            } else {

                currentCapture = captureFrontImage
                selectImage()


            }

        }
        binding.tvRetakeBackPhoto.setOnClickListener {
            if (!hasPermissions(requireContext(), *PERMISSIONS)) {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


            } else {
                currentCapture = captureBackImage
                selectImage()
            }

        }
        binding.frontImageIcon.setOnClickListener {

            if (!hasPermissions(requireContext(), *PERMISSIONS)) {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


            } else {

                currentCapture = captureFrontImage
                selectImage()


            }
        }

        binding.backImagecard.setOnClickListener {
            if (!hasPermissions(requireContext(), *PERMISSIONS)) {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


            } else {
                currentCapture = captureBackImage
                selectImage()
            }
        }



        binding.buttonContinueToselfie.setOnClickListener {

            if (validateDetails()) {
                //save the image and proceed

                Navigation.findNavController(it)
                    .navigate(R.id.action_capturePhotosFragment_to_finalDetailsVerificationFragment)
            }


        }
    }


    private fun validateDetails(): Boolean {
        if (binding.frontImage.drawable == null) {
            showErrorSnackBar("Front Image is Required")
            return false
        }
        if (binding.backimage.drawable == null) {
            showErrorSnackBar("Back Image is Required")
            return false
        }


        return true


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(
            "TAG",
            "onActivityResult  data : $data  resultcode :$resultCode  requestcode: $requestCode"
        )


        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                Constants.REQUEST_IMAGE_CAPTURE -> {
                    if (currentCapture == captureFrontImage) {
                        try {


                            val file = File(currentFrontPhotoPath)
                            val uri = Uri.fromFile(file)




                            UCrop.of(uri, frontDestinationUri).withAspectRatio(85F, 55F)
                                .start(requireContext(), this)

                        } catch (e: Exception) {

                        }
                    } else {

                        val file = File(currentBackPhotoPath)
                        val uri = Uri.fromFile(file)
                        UCrop.of(uri, backDestinationUri).withAspectRatio(85F, 55F)
                            .start(requireContext(), this)
                    }
                }

                UCrop.REQUEST_CROP -> {

                    val uri: Uri = UCrop.getOutput(data!!)!!
                    val filePath = AppUtils.getRealPathFromURIPath(uri, requireActivity())
                    if (currentCapture == captureFrontImage) {


                        frontFile = File(filePath)
                        AppUtils.frontImagePath = frontFile.toString()

                        val bitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                uri
                            )
                        } else {
                            val source =
                                ImageDecoder.createSource(requireContext().contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        }
                        binding.frontImage.setImageBitmap(bitmap)
                        binding.tvRetakePhoto.makeVisible()
                        binding.tvCaptureFront.makeInvisible()


                    } else {
                        backFile = File(filePath)
                        AppUtils.backImagePath = backFile.toString()

                        val bitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                uri
                            )
                        } else {
                            val source =
                                ImageDecoder.createSource(requireContext().contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        }
                        binding.backimage.setImageBitmap(bitmap)
                        binding.tvRetakeBackPhoto.makeVisible()
                        binding.tvCaptureBack.makeInvisible()


                    }

                }
                UCrop.RESULT_ERROR -> {
                    val error = UCrop.getError(data!!)
                    Log.d("TAG", "onActivityResult: $error")
                }


            }
        }
    }


    private fun makeStoragePermisionRequest() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            Constants.MY_PERMISSIONS_REQUEST_READ_STORAGE
        )
    }

    private fun makeCameraPermisionRequest() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.MY_PERMISSIONS_REQUEST_READ_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (checknGrantPermmision()) {
                        if (checkWriteStoragePermission()) {
                            selectImage()
                        } else {
                            makeStoragePermisionRequest()
                        }
                    } else {
                        makeCameraPermisionRequest()
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    /*ToastnDialog.toastError(
                        requireContext(),
                        "Storage Permission was denied, allow access to upload image"
                    )*/
                }
                return
            }
// Add other 'when' lines to check for other
// permissions this app might request.
            else -> {
// Ignore all other requests.
            }
        }
        when (requestCode) {
            CAMERA -> {
// If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    selectImage()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    /*ToastnDialog.toastError(
                        requireContext(),
                        "Camera Permission was denied, allow access to upload image"
                    )*/
                }
                return
            }
        }

    }

    private fun checknGrantPermmision(): Boolean {

        val camera = ContextCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.CAMERA
        )
        if (camera != PackageManager.PERMISSION_GRANTED) {
            makeCameraPermisionRequest()
            return false
        }
        return true
    }

    private fun checkWriteStoragePermission(): Boolean {

        val storage = ContextCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (storage != PackageManager.PERMISSION_GRANTED) {
            makeStoragePermisionRequest()
            return false
        }
        return true
    }

    open fun selectImage() {

        val items = arrayOf<CharSequence>("Take Photo", "Cancel")
        val title = TextView(context)
        title.text = "Add Photo"
        title.setPadding(10, 15, 15, 10)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.WHITE)
        title.textSize = 22f
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        builder!!.setCustomTitle(title)
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Take Photo") {
                openCameraForPickingImage(CAMERA)
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder!!.show()
    }

    override fun onResume() {
        super.onResume()
        changeActionbarColor(Color.parseColor("#ffffff"))

    }


    @Throws(IOException::class)
    private fun createImageFile(fileType: Int = 0): File {
        val directory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${AppUtils.uniqueFileName()}_", /* prefix */
            ".jpg", /* suffix */
            directory /* directory */

        ).apply {
            if (fileType == captureFrontImage) {
                currentFrontPhotoPath = absolutePath
            } else if (fileType == captureBackImage) {
                currentBackPhotoPath = absolutePath

            }


        }
    }

    private fun openCameraForPickingImage(code: Int) {
        /* Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
             startActivityForResult(Intent.createChooser(this, "Select File"), code)

         }*/
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(currentCapture)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    toastMessage(getString(R.string.photo_capture_error))
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.ekenya.rnd.walletbaseapp.tollo.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, code)
                }
            }
        }
    }
}