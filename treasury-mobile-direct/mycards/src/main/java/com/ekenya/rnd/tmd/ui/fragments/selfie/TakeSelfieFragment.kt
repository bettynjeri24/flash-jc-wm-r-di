package com.ekenya.rnd.tmd.ui.fragments.selfie

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.mycards.databinding.FragmentTakeSelfieBinding
import com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment.OnboardViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TakeSelfieFragment(private val viewmodel: OnboardViewModel, private val next: () -> Unit) : BaseDaggerFragment() {

    private lateinit var binding: FragmentTakeSelfieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTakeSelfieBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            binding.buttonContinue.setOnClickListener {
                getfile(0)
            }
        }
    }

    private fun getfile(code: Int) {
        val chooseFile = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(chooseFile, code)
    }

    //    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                data?.let {
                    val extras: Bundle? = data.extras
                    val imageBitmap: Bitmap? = extras?.get("data") as Bitmap?
                    binding.imageView22.setImageBitmap(imageBitmap)
//                    val file = File(data?.data.toString())
//                    val currentfiles = viewmodel.multiParts.value
//                    currentfiles?.apply {
//                        add(file)
//                        viewmodel.multiParts.value = this
//                    }
                    binding.buttonContinue.setOnClickListener {
                        lifecycleScope.launch {
                            viewmodel.sendUserRegistration().collectLatest { resource ->
                                when (resource) {
                                    is Resource.Error -> {
                                        Log.e(TAG, "onActivityResult: ${resource.error?.message}")
                                        showHideProgress(null)
                                        Snackbar.make(requireView(),resource.data.toString(),Snackbar.LENGTH_LONG).show()
                                    }
                                    is Resource.Loading -> {
                                        showHideProgress("Hi!... hold on we are registering you")
                                    }
                                    is Resource.Success -> {
                                        showHideProgress(null)
                                        next()
                                    }
                                }
                            }
                        }
                    }
                    binding.buttonRetry.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            getfile(0)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "TakeSelfieFragment"
    }
}
