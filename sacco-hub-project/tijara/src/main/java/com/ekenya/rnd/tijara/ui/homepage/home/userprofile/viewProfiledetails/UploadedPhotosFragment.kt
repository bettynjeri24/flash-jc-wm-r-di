package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentUploadedPhotosBinding
import com.ekenya.rnd.tijara.utils.bindImage
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import javax.inject.Inject


class UploadedPhotosFragment : BaseDaggerFragment() {
    private lateinit var binding:FragmentUploadedPhotosBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(ViewPersonalInfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUploadedPhotosBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.viemodels=viewModel
        setupNavUp()
        binding.whiteBgLoading.makeVisible()
        viewModel.statusCode.observe(viewLifecycleOwner,{
            if (it!==null){
                when(it){
                    1->{
                        binding.whiteBgLoading.makeGone()
                    }
                    0->{
                        binding.whiteBgLoading.makeGone()

                    }
                    else->{
                        binding.whiteBgLoading.makeGone()

                    }
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel?.personalInfoList?.observe(viewLifecycleOwner,  {
                if (it.idFrontUrl.isEmpty()){
                    bindImage(binding.frontPhoto,it.passportPhotoUrl)
                    binding.backPhoto.makeGone()
                    binding.backSide.makeGone()
                }else {
                    bindImage(binding.frontPhoto, it.idFrontUrl)
                    bindImage(binding.backPhoto, it.idBackUrl)
                }
            })
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  ="Uploaded Photos"
    }
}