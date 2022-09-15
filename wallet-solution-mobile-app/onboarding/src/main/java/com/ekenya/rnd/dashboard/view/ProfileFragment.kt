package com.ekenya.rnd.dashboard.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.onboarding.BuildConfig
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentProfileBinding
import com.ekenya.rnd.walletbaseapp.BuildConfig.VERSION_CODE
import com.ekenya.rnd.walletbaseapp.BuildConfig.VERSION_NAME
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


open class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var frontDestinationUri: Uri
    private var currentFrontPhotoPath: String? = null
    private val captureFrontImage = 1
    private var currentCapture = 0
    private lateinit var frontFile: File


    val PERMISSION_ALL = 1

    val PERMISSIONS = arrayOf(

        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initUI()
        initClickListeners()

        return binding.root
    }

    private fun initClickListeners() {
        /*binding.closeIcon.setOnClickListener {
            findNavController().popBackStack()
        }*/
        binding.imageCaptureprofilePhoto.setOnClickListener {
            captureProfilePhoto()
        }
        binding.tvSecurity.setOnClickListener {
            findNavController().navigate(R.id.securitySettingsFragment)
        }
        binding.tvPersonalDetails.setOnClickListener {
            findNavController().navigate(R.id.personalDetailsFragment)
        }
        binding.tvCustomerService.setOnClickListener {
            findNavController().navigate(R.id.aboutUsFragment)
        }
        binding.tvLogout.setOnClickListener {
            findNavController().navigate(R.id.landingPageFragment)
        }
        binding.tvFAQs.setOnClickListener {
            findNavController().navigate(R.id.aboutUsFragment)

        }
        binding.tvRateUs.setOnClickListener {
           toastMessage("Feature Coming Soon")

        }
        binding.tvContactSupport.setOnClickListener {
            findNavController().navigate(R.id.aboutUsFragment)

        }
        binding.buttonInviteFriends.setOnClickListener {
            shareLink()
        }
    }

    private fun captureProfilePhoto() {
        checkForPermissions()
        initCameraParameters()
        doProfileImageCapture()

    }

    private fun doProfileImageCapture() {
        if (!hasPermissions(requireContext(), *PERMISSIONS)) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


        } else {

            currentCapture = captureFrontImage
            selectImage()


        }
    }

    open fun selectImage() {

        val items = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
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
                openCameraForPickingImage(Constants.CAMERA)
            } else if (items[item] == "Choose From Gallery") {

                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun initCameraParameters() {
        val mContextWrapper = ContextWrapper(requireContext())
        val mDirectory: File = mContextWrapper.getDir(
            "tempImage",
            Context.MODE_PRIVATE
        )

        frontDestinationUri = Uri.parse(File(mDirectory, AppUtils.uniqueFileName()).path)
    }

    private fun checkForPermissions() {
        if (!hasPermissions(requireContext(), *PERMISSIONS)) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)


        }
    }

    private fun shareLink() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                
                
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    private fun initUI() {

        val lastName = SharedPreferencesManager.getLastName(requireContext())
        val secondName = SharedPreferencesManager.getMiddleName(requireContext())
        val firstName = SharedPreferencesManager.getFirstName(requireContext())
        binding.customersName.text ="$firstName $secondName $lastName"


        val manager: PackageManager = requireActivity().packageManager
        try {
            val info: PackageInfo = manager.getPackageInfo(requireActivity().packageName, 0)
            val packageName: String = info.packageName
            val versionCode: Int = info.versionCode
            val versionName: String = info.versionName
            binding.tvVersioncode.text=versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {

        }

        setProfileImage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(
            "TAG",
            "onActivityResult  data : $data  resultcode :$resultCode  requestcode: $requestCode"
        )


        if (resultCode == Activity.RESULT_OK) {


            val options = UCrop.Options()
            options.setCircleDimmedLayer(true)


            when (requestCode) {
                Constants.REQUEST_IMAGE_CAPTURE -> {
                    if (currentCapture == captureFrontImage) {
                        try {


                            val file = File(currentFrontPhotoPath)
                            val uri = Uri.fromFile(file)




                            UCrop.of(uri, frontDestinationUri).withAspectRatio(85F, 55F)
                                .withOptions(options)
                                .start(requireContext(), this)

                        } catch (e: Exception) {

                        }
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

                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray: ByteArray = stream.toByteArray()
                        bitmap.recycle()
                        val encodedImage: String =
                            Base64.encodeToString(byteArray, Base64.DEFAULT)

                        SharedPreferencesManager.setProfilePhoto(requireContext(), encodedImage)

                        setProfileImage()


                    }

                }
                UCrop.RESULT_ERROR -> {
                    val error = UCrop.getError(data!!)
                    Log.d("TAG", "onActivityResult: $error")
                }


            }
        }
    }

    private fun setProfileImage() {

        val imageString = SharedPreferencesManager.getProfilePhoto(requireContext())
        try {

            val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            binding.iconProfile.setImageBitmap(bitmap)
        } catch (e: Exception) {

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
            }


        }
    }

}