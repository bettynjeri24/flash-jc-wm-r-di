package com.ekenya.rnd.tijara.ui.auth.onboarding

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
import com.ekenya.rnd.tijara.adapters.NewSaccoListAdapter
import com.ekenya.rnd.tijara.adapters.SaccoListAdapter
import com.ekenya.rnd.tijara.adapters.SelectSaccoAdapter
import com.ekenya.rnd.tijara.databinding.NewSaccoFragmentBinding
import com.ekenya.rnd.tijara.databinding.SaccoItemsFragmentBinding
import com.ekenya.rnd.tijara.network.model.local.NewSaccoDataEntity
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.ui.auth.registration.SaccoListViewModel
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import com.ekenya.rnd.tijara.utils.showToast
import javax.inject.Inject

class NewSaccoFragment : BaseDaggerFragment() {
    private lateinit var binding: NewSaccoFragmentBinding
    private lateinit var newSaccoAdapter: NewSaccoListAdapter
    private val saccoList:ArrayList<NewSaccoDataEntity> = arrayListOf()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CountryViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= NewSaccoFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.listViewModel = viewModel
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.whiteBgLoading.makeVisible()

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
        })*/

        binding.apply {
            btnContinue.setOnClickListener {
                if (Constants.SIGNUPORGID==""){
                    showToast(requireContext(),getString(R.string.select_one_sacco_to_continue))
                }else{
                findNavController().navigate(R.id.action_newSaccoFragment_to_scannningFragment)
            }}
            newSaccoAdapter= NewSaccoListAdapter(NewSaccoListAdapter.OnClickListener{},requireContext(),saccoList)
            rvSaccoList.adapter=newSaccoAdapter
            viewModel.getNewSaccos().observe(viewLifecycleOwner, Observer {
                saccoList.clear()
                saccoList.addAll(it!!)
                newSaccoAdapter.notifyDataSetChanged()
            })
        }
    }

}