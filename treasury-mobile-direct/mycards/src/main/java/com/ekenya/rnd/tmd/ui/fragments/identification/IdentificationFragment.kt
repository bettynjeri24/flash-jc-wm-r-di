package com.ekenya.rnd.tmd.ui.fragments.identification

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.ekenya.rnd.mycards.databinding.FragmentIdentificationBinding
import com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment.OnboardViewModel
import com.ekenya.rnd.tmd.utils.setUpSpinner
import kotlinx.android.synthetic.main.fragment_identification.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class IdentificationFragment(private val viewmodel: OnboardViewModel, private val next: () -> Unit) : Fragment() {

    private var newUserRequest = viewmodel.newUserRequest.value!!
    private lateinit var binding: FragmentIdentificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentIdentificationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        viewmodel.newUserRequest.observe(viewLifecycleOwner) {
            newUserRequest = it
        }
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 0)
    }

    private fun setUpUi() {
        val options = mutableListOf("Identification Number", "Passport Number")
        binding.apply {
            autocompleteIDorPassport.setUpSpinner(
                options,
                onItemClick = { parent, view, position, id ->
                    textInputLayout5.hint = options[position]
                }
            )

            materialCardViewFront.setOnClickListener {
                getfile(0)
            }

            materialCardViewBack.setOnClickListener {
                getfile(1)
            }

            materialCardViewSignature.setOnClickListener {
                getfile(2)
            }

            button3.setOnClickListener {
                newUserRequest.nationalId = textInputIdNumber.text.toString()
                viewmodel.newUserRequest.value = newUserRequest
                next()
            }
        }
    }

    private fun getfile(code: Int) {
        val chooseFile = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(chooseFile, code)
    }

    private fun makePhotoFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "MyPhoto_" + timeStamp + "_"
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    //    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                val file = createImage(data?.extras?.get("data") as Bitmap)
                val currentfiles = viewmodel.multiParts.value
                currentfiles?.apply {
                    add(file)
                    viewmodel.multiParts.value = this
                }
                imageViewFrontCheck.isVisible = true
            }
            1 -> {
                val file = createImage(data?.extras?.get("data") as Bitmap)
                val currentfiles = viewmodel.multiParts.value
                currentfiles?.apply {
                    add(file)
                    viewmodel.multiParts.value = this
                }
                imageViewBackCheck.isVisible = true
            }
            2 -> {
                val file = createImage(data?.extras?.get("data") as Bitmap)
                val currentfiles = viewmodel.multiParts.value
                currentfiles?.apply {
                    add(file)
                    viewmodel.multiParts.value = this
                }
                imageViewSignatureCheck.isVisible = true
            }
        }
    }

    private fun createImage(bitmap: Bitmap): File {
        val filesDir: File = requireContext().filesDir
        val imageFile = File(filesDir, "${System.currentTimeMillis()}.jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
        return imageFile
    }
}
