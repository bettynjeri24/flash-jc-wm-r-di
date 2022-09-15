package com.ekenya.rnd.tijara.ui.auth.changepassword.forgetPin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.SelectIDTypeFragmentBinding
import com.ekenya.rnd.tijara.network.model.IDType
import com.ekenya.rnd.tijara.utils.toastyErrors
import com.ekenya.rnd.tijara.utils.waringAlertDialogUp
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class SelectIDTypeFragment : Fragment() {
    var typeName=""
private lateinit var binding:SelectIDTypeFragmentBinding
    private lateinit var viewModel: SelectIDTypeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SelectIDTypeFragmentBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }
        viewModel = ViewModelProvider(requireActivity()).get(SelectIDTypeViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressr.visibility=View.VISIBLE
        requireActivity().window.statusBarColor = resources.getColor(R.color.spinkit_color)
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_identity)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        viewModel.identityType.observe(viewLifecycleOwner) { idTypes ->
            if (idTypes != null) {
                populateIdTypes(idTypes)
            } else {
                Toasty.error(
                    requireContext(),
                    "An error occurred. Please try again",
                    Toasty.LENGTH_LONG
                ).show()
            }
        }
        viewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                binding.progressr.visibility=View.GONE
                when (it) {
                    1 -> {
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        binding.progressr.visibility=View.GONE
                        viewModel.stopObserving()
                    }
                    0 -> {
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        waringAlertDialogUp(requireContext(),requireView(),getString(R.string.oops_we_are_sorry)
                            ,getString(R.string.unable_to_complete_your_request))
                        binding.progressr.visibility=View.GONE
                        viewModel.stopObserving()
                    }
                    else -> {
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        binding.progressr.visibility=View.GONE
                        viewModel.stopObserving()
                    }
                }
            }
        })
        binding.btnContinue.setOnClickListener {
            val idNumber=binding.etAccNo.text.toString().trim()
            val identifier=binding.acType.text.toString().trim()
            if (identifier.isEmpty()){
                binding.tiType.error="Required"
              //  toastyErrors("Select ID Type to continue")
            }else if (idNumber.isEmpty()){
                binding.tlAccNo.error="Required"
            }else{
                binding.tlAccNo.error=""
                viewModel.setTypeID(idNumber)
                findNavController().navigate(R.id.action_selectIDTypeFragment_to_selfiePinFragment)
            }
        }
    }
    private fun populateIdTypes(iDTypesList: List<IDType>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, iDTypesList)
        binding.acType.setAdapter(typeAdapter)
        binding.acType.keyListener = null
        binding.acType.setOnItemClickListener { parent, _, position, _ ->
            val selected: IDType = parent.adapter.getItem(position) as IDType
            typeName=selected.name
            if (selected.id==1){
                binding.tlAccNo.visibility=View.VISIBLE
                binding.tlAccNo.hint=getString(R.string.id_number)
            }else{
                binding.tlAccNo.visibility=View.VISIBLE
                binding.tlAccNo.hint="Passport Number"
            }
        }
    }

}