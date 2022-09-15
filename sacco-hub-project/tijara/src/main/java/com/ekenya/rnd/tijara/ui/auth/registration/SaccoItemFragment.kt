package com.ekenya.rnd.tijara.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.SaccoListAdapter
import com.ekenya.rnd.tijara.databinding.SaccoItemsFragmentBinding
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import com.ekenya.rnd.tijara.utils.showToast
import javax.inject.Inject

class SaccoItemFragment : BaseDaggerFragment() {
    private lateinit var binding: SaccoItemsFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SaccoListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SaccoItemsFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.listViewModel = viewModel
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            btnContinue.setOnClickListener {
                if (Constants.SIGNUPORGID==""){
                    showToast(requireContext(),getString(R.string.select_one_sacco_to_continue))
                }else{
                findNavController().navigate(R.id.action_saccoListFragment_to_scannningFragment)
            }}
            rvSaccoList.adapter=SaccoListAdapter(SaccoListAdapter.OnClickListener{

            },requireContext())
        }
    }

}