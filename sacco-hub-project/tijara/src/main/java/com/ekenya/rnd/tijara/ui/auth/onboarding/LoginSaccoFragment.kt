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
import com.ekenya.rnd.tijara.adapters.SelectSaccoAdapter
import com.ekenya.rnd.tijara.databinding.LoginSaccoFragmentBinding
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.requestDTO.LoginSaccoDTO
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.callbacks.SaccoDetailsCallBack
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject


class LoginSaccoFragment : BaseDaggerFragment(),SaccoDetailsCallBack {
    lateinit var binding: LoginSaccoFragmentBinding
    lateinit var selectSaccoAdapter: SelectSaccoAdapter
    private val saccoList:ArrayList<SaccoDetailEntity> = arrayListOf()


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(CountryViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
            binding = LoginSaccoFragmentBinding.inflate(layoutInflater)

            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.saccoViewModel = viewModel

        binding.apply {
            saccoContinue.setOnClickListener {
                if (Constants.ORGID==""){
                    showToast(requireContext(),getString(R.string.select_one_sacco_to_continue))
                }else{
                findNavController().navigate(R.id.loginPinFragment)
            }}

        }
   selectSaccoAdapter= SelectSaccoAdapter( requireContext(),this,saccoList)
        binding.rvSelectSacco.adapter=selectSaccoAdapter


        viewModel.getAllSaccos().observe(viewLifecycleOwner, Observer {
            saccoList.clear()
            saccoList.addAll(it!!)
            selectSaccoAdapter.notifyDataSetChanged()
        })



        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }


    }


    override fun onItemSelected(item: SaccoDetailEntity) {
       // viewModel.setOrgId(item.orgId.toString())
       // viewModel.setUserName(item.username)
    }



}