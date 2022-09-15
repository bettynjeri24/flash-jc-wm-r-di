package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.ViewPersonalInfoFragmentBinding
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.inject.Inject

class ViewPersonalInfoFragment : BaseDaggerFragment() {
private lateinit var binding: ViewPersonalInfoFragmentBinding
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
        requireActivity().window.statusBarColor = resources.getColor(R.color.buttonColor)
        binding= ViewPersonalInfoFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel
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
        binding.apply {
            viewModel?.personalInfoList?.observe(viewLifecycleOwner, Observer {
                etPhone.setText(it.phone)
                etMemberNo.setText(it.memberNumber)
                etEmail.setText(it.email)
                etKraPin.setText(it.kraPin)
            })
        }
    }
   /* private fun initUserPhoto(){
        val path = requireContext().getDir("imageDir", Context.MODE_PRIVATE)
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
        }
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
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.personal_details)
    }

}