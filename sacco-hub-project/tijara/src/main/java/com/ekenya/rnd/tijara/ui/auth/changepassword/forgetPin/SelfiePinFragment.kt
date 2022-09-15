package com.ekenya.rnd.tijara.ui.auth.changepassword.forgetPin

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.PinResetDialogBinding
import com.ekenya.rnd.tijara.databinding.SelfiePinFragmentBinding
import com.ekenya.rnd.tijara.utils.*
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import java.io.File
import java.io.IOException

class SelfiePinFragment : Fragment() {
    private lateinit var binding: SelfiePinFragmentBinding
    private lateinit var cardBinding: PinResetDialogBinding
    private lateinit var viewModel: SelectIDTypeViewModel
    private lateinit var destinationUri: Uri
    private lateinit var selfieFile: File
    private var idtype = ""
    private var reseOption = ""
    private var selfiePath: String? = null

    companion object {
        private const val CAMERA_REQUEST_CODE = 2
        private const val PERMISSION_REQUEST_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SelfiePinFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(SelectIDTypeViewModel::class.java)
        binding.apply {
            binding.imageBack.setOnClickListener {
                findNavController().navigateUp()
            }

            viewModel.resetOption.observe(viewLifecycleOwner) { option ->
                reseOption = option
            }
            btnContinue.setOnClickListener {
                if (!::selfieFile.isInitialized) {
                    toastyInfos("please take a selfie")
                } else {
                    viewModel.selfiePhoto = selfieFile
                    binding.progressr.visibility = View.VISIBLE
                    btnContinue.isEnabled = false
                    requireActivity().window.statusBarColor =
                        resources.getColor(R.color.spinkit_color)
                    binding.progressr.tv_pbTitle.text =
                        getString(R.string.we_are_processing_requesrt)
                    binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                    viewModel.resetPin(idtype, reseOption)
                    // findNavController().navigate(R.id.action_selfieFragment_to_moreDetailFragment)
                }
            }
            viewModel.status.observe(viewLifecycleOwner) {
                if (null != it) {
                    btnContinue.isEnabled = true
                    binding.progressr.visibility = View.GONE
                    when (it) {
                        1 -> {
                            btnContinue.isEnabled = true
                            requireActivity().window.statusBarColor =
                                resources.getColor(R.color.white)
                            showResetDialog()
                            binding.progressr.visibility = View.GONE
                            viewModel.stopObserving()
                        }
                        0 -> {
                            btnContinue.isEnabled = true
                            requireActivity().window.statusBarColor =
                                resources.getColor(R.color.white)
                            toastyErrors(viewModel.statusMessage.value!!)
                            binding.progressr.visibility = View.GONE
                            viewModel.stopObserving()
                        }
                        else -> {
                            btnContinue.isEnabled = true
                            requireActivity().window.statusBarColor =
                                resources.getColor(R.color.white)
                            binding.progressr.visibility = View.GONE
                            toastyErrors(viewModel.statusMessage.value!!)
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mContextWrapper = ContextWrapper(requireContext())
        val mDirectory: File = mContextWrapper.getDir(
            "Pictures",
            Context.MODE_PRIVATE
        )
        val photoPath = File(mDirectory, "Selfiephoto.png")
        destinationUri = Uri.parse(photoPath.path)
        binding.btnTakeSelfie.setOnClickListener {
            cameraCheckPermission()
        }
        viewModel.typeID.observe(viewLifecycleOwner) {
            Log.d("TAG", "kk$it")
            idtype = it
        }
    }

    private fun takePictures() {
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
                    startActivityForResult(
                        takePictureIntent,
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
            selfiePath = absolutePath
        }
    }

    private fun cameraCheckPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
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
                        "Camera Permission was denied!! \n But don't worry you can allow this permission on the app setting"
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
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val file = File(selfiePath!!)
                    val uri = Uri.fromFile(file)
                    UCrop.of(uri, destinationUri)
                        .withAspectRatio(5f, 5f)
                        .start(requireContext(), this)
                }
                UCrop.REQUEST_CROP -> {
                    val uri: Uri = UCrop.getOutput(data!!)!!
                    val filePath = getRealPathFromURIPath(uri, requireActivity())
                    selfieFile = File(filePath)
                    val bitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            uri
                        )
                    } else {
                        val source =
                            ImageDecoder.createSource(requireContext().contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    }
                    binding.selfieIv.makeGone()
                    binding.selfieTaken.makeVisible()
                    binding.btnContinue.visibility = View.VISIBLE
                    binding.btnTakeSelfie.makeGone()
                    binding.selfieTaken.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun showResetDialog() {
        val dialog = Dialog(requireContext())
        cardBinding =
            PinResetDialogBinding.inflate(LayoutInflater.from(context))
        cardBinding.btnREGISTER.setOnClickListener {
            dialog.dismiss()
            val directions =
                SelfiePinFragmentDirections.actionSelfiePinFragmentToDefaultPinFragment(fragmentType = 1)
            findNavController().navigate(directions)
            viewModel.stopObserving()
        }

        dialog.setContentView(cardBinding.root)
        dialog.show()
        dialog.setCancelable(false)
    }
}
