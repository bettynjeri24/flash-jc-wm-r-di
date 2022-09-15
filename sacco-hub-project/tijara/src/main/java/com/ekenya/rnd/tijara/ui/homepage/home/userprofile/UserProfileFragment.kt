package com.ekenya.rnd.tijara.ui.homepage.home.userprofile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.ekenya.rnd.tijara.adapters.layoutAdapter.UserProfileAdapter
import com.ekenya.rnd.tijara.databinding.BottomSheetChoosePhotoLayoutBinding
import com.ekenya.rnd.tijara.databinding.UserProfileFragmentBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.ui.auth.registration.SelfieFragment
import com.ekenya.rnd.tijara.utils.*
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.io.*

class UserProfileFragment : Fragment() {
    private  lateinit var binding: UserProfileFragmentBinding
    private lateinit var viewModel:UserProfileViewModel
    private  lateinit var bottomsheetbinding: BottomSheetChoosePhotoLayoutBinding
    private  var lists:ArrayList<BillPaymentMerchant> = ArrayList()
    private lateinit var destinationUri: Uri
    private lateinit var selfieFile: File
    private var selfiePath: String? = null
    companion object {
        private const val CAMERA_REQUEST_CODE = 2
        private const val GALLERY_REQUEST_CODE = 3
        //  lateinit var mypath: File
        private const val PERMISSION_REQUEST_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.buttonColor)
        binding= UserProfileFragmentBinding.inflate(layoutInflater)
        viewModel=ViewModelProvider(requireActivity())[UserProfileViewModel::class.java]
        binding.username.text=Constants.USERFULLNAME
        val mContextWrapper = ContextWrapper(requireContext())
        val mDirectory: File = mContextWrapper.getDir("Pictures",
            Context.MODE_PRIVATE)
        val photoPath = File(mDirectory, "Selfiephoto.png")
        destinationUri = Uri.parse(photoPath.path)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addProfileItems()
        setupNavUp()

        viewModel.statusCode.observe(viewLifecycleOwner,  {
            if (null != it) {
                binding.progressr.makeGone()
                viewModel.stopObserving()
                when (it) {
                    1 -> {
                        viewModel.stopObserving()
                        binding.progressr.makeGone()
                        viewModel.imageUrl.observe(viewLifecycleOwner,{photo->
                            loadImageSrc(binding.ivUser,photo)
                        })

                        toastySuccess("Profile picture uploaded successfully")

                    }
                    0 -> {
                        viewModel.stopObserving()
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                    }

                    else -> {
                        viewModel.stopObserving()
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }
                }
            }
        })


        /* val path = requireContext().getDir("imageDir", Context.MODE_PRIVATE)
         try {
             if (path==null){
                 val drawable= AppCompatResources.getDrawable(requireContext(), R.drawable.user_pic)
                 Glide.with(requireContext())
                     .load(drawable)
                     .into(binding.ivUser!!)
                 binding.ivUser.setImageResource(R.drawable.user_pic)
             }else{
                 loadImageFromStorage()
             }
         }catch (e: Exception){
             Timber.d("LOADING IMAGE ERROR ${e.message}")
         }*/
        binding.apply {
            binding.rvUserProfile.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            var userPAdapter= UserProfileAdapter(lists)
            binding.rvUserProfile.adapter=userPAdapter


            imageCamera.setOnClickListener {
                showChoosePicDialog()
            }

        }
    }

    private fun addProfileItems() {
        lists.add(BillPaymentMerchant(R.drawable.personal_info,"Personal Details", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.id,"Identification", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.bank,"Bank Details", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.kin,"Next Of Kin Details", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.work,"Work Details", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.residense,"Residence Details", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.setting,"Settings", R.drawable.arrow_right))
    }

    private fun showChoosePicDialog() {
        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
        bottomsheetbinding = BottomSheetChoosePhotoLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bottomsheetbinding.root)
        bottomsheetbinding.ivCam.setOnClickListener {
            dialog.dismiss()
            cameraCheckPermission()
        }
        bottomsheetbinding.ivGallery.setOnClickListener {
            dialog.dismiss()
            pickFromGallery(GALLERY_REQUEST_CODE)
        }

        dialog.show()


    }
    private fun takePictures(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    toastyErrors("Failed to capture photo")
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.eclectics.rnd.baseapp.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,
                        CAMERA_REQUEST_CODE
                    )
                }
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val directory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${uniqueFileName()}_", /* prefix */
            ".jpg", /* suffix */
            directory /* directory */
        ).apply {
            selfiePath=absolutePath
        }
    }

    private fun cameraCheckPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            takePictures()
        } else {
            /**else if we dont have permission to use the cam we request for it*/
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )

        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // permission was granted
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                    /**we use camera functionality*/
                    takePictures()
                } else {
                    toastyInfos(
                        "Camera Permission was denied!! \n But don't worry you can allow this permission on the app setting",
                    )
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            /**we display the data as bitmap and set our image to that data
             * nb// you are not supposed to change the key here ,else the app will crash with
             * kotlin.TypeCastException: null cannot be cast to non-null type android.graphics.Bitmap*/
            when(requestCode){
                CAMERA_REQUEST_CODE ->{
                    val file = File(selfiePath!!)
                    val uri = Uri.fromFile(file)
                    UCrop.of(uri, destinationUri)
                        .withAspectRatio(5f, 5f)
                        .start(requireContext(),this)

                }
                UCrop.REQUEST_CROP->{
                    val uri: Uri = UCrop.getOutput(data!!)!!
                    val filePath = getRealPathFromURIPath(uri, requireActivity())
                    selfieFile = File(filePath)
                    val bitmap = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            uri
                        )

                    } else {
                        val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    }
                    binding.ivUser.setImageBitmap(bitmap)
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    viewModel.selfiePhoto=selfieFile
                    // viewModel.uploadProfile()

                }
            }

        }
    }
    /*  private fun cameraCheckPermission() {
          if (ContextCompat.checkSelfPermission(
                  requireContext(), Manifest.permission.CAMERA
              ) == PackageManager.PERMISSION_GRANTED
          ) {
              *//**if permission is granted, we use camera functionality, ie prepare intent to start our camera*//*
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            *//**else if we dont have permission to use the cam we request for it*//*
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )

        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // permission was granted
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                    *//**we use camera functionality*//*
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                } else {
                    showToast(
                        requireContext(),
                        "Camera Permission was denied!! \n But don't worry you can allow this permission on the app setting",
                    )
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when(requestCode){
                GALLERY_REQUEST_CODE ->{
                    val sourceURI = data!!.data
                    context?.let {
                        CropImage.activity(sourceURI)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(it,this)
                    }
                }
                CAMERA_REQUEST_CODE ->{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.ivUser.setImageBitmap(bitmap)
                    saveToInternalStorage(bitmap,requireContext())
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE-> {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        val resultUri: Uri = result.uri
                        Glide.with(requireContext())
                            .load(resultUri)
                            .into(binding.ivUser!!)

                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            resultUri
                        )
                          saveToInternalStorage(bitmap,requireContext())
                        binding.ivUser.setImageBitmap(bitmap)

                    }
                }




            }
        }catch (e:Exception){
            Timber.e("CROPPING EXCEPTION ${e.message}")

        }

    }*/


    /* private fun saveToInternalStorage(bitmapImage: Bitmap,context: Context): String? {
         // return a directory in internal storage
         val directory = context.getDir("imageDir", Context.MODE_PRIVATE)
         Timber.d("FILE DIRECTORY $directory")
         // Create a file to save the image
         mypath = File(directory, "profile.png")
         Timber.d("FILE PATH $mypath")
         var fos: FileOutputStream? = null
         try {
             fos = FileOutputStream(mypath)
             // Use the compress method on the BitMap object to write image to the OutputStream
             bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)

             fos.flush()
             fos.close()
             showSuccessSnackBar(requireView(),"Profile  Uploaded Successfully")
         } catch (e: Exception) {
             e.printStackTrace()
             showToast(requireContext(),"Unable to save the photo")
         }

         return directory.absolutePath
     }
     private fun loadImageFromStorage() {
         try {
             val path = requireContext().getDir("imageDir", Context.MODE_PRIVATE)

             val file = File(path, "profile.png")
             val bitmap = BitmapFactory.decodeStream(FileInputStream(file))

             binding.ivUser.setImageBitmap(bitmap)
         } catch (e: FileNotFoundException) {
             e.printStackTrace()
         }
     }*/

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.profile)
    }

}