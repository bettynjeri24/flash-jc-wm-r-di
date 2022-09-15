package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.LoanGuarontedAdapter
import com.ekenya.rnd.tijara.adapters.LoanGuarontedRequestAdapter
import com.ekenya.rnd.tijara.databinding.LoanGuarantedFragmentBinding
import com.ekenya.rnd.tijara.network.model.NewLoanGuaranteData
import com.ekenya.rnd.tijara.requestDTO.NewLoanRequestDTO
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LoanGuarantedFragment : Fragment() {
    private lateinit var binding:LoanGuarantedFragmentBinding
    private lateinit var viewModel: LoanGuarantedViewModel
    private lateinit var adapter: LoanGuarontedRequestAdapter
    private lateinit var loanGuarontedAdapter: LoanGuarontedAdapter
    private val newRequest:ArrayList<NewLoanGuaranteData> = arrayListOf()
    private val loans:ArrayList<NewLoanGuaranteData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LoanGuarantedFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(LoanGuarantedViewModel::class.java)
        binding.lifecycleOwner=this
        binding.viewmodel=viewModel
        binding.ClNewLoanRequest.visibility=View.VISIBLE


        binding.apply {
            NewRequests.setOnClickListener {
                binding.ClNewLoanRequest.visibility=View.VISIBLE
                binding.ClAllLoanQuaranted.visibility=View.GONE
                binding.NewRequests.setBackgroundColor(resources.getColor(R.color.blue_light))
                binding.LoanGuaranted.setBackgroundColor(resources.getColor(R.color.color_000))
                binding.LoanGuaranted.setTextColor(resources.getColor(R.color.hintColor))
                binding.NewRequests.setTextColor(resources.getColor(R.color.black))
            }
            LoanGuaranted.setOnClickListener {
                // viewmodel?.getAllLoanGuaranted()
                binding.ClNewLoanRequest.visibility=View.GONE
                binding.ClAllLoanQuaranted.visibility=View.VISIBLE
                binding.LoanGuaranted.setBackgroundColor(resources.getColor(R.color.blue_light))
                binding.NewRequests.setBackgroundColor(resources.getColor(R.color.color_000))
                binding.NewRequests.setTextColor(resources.getColor(R.color.hintColor))
                binding.LoanGuaranted.setTextColor(resources.getColor(R.color.black))
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setupNavUp()
        adapter= LoanGuarontedRequestAdapter(requireContext(),
            newRequest,
            LoanGuarontedRequestAdapter.OnClickListener {
                Constants.GENDER_ID = it.requestId.toString()
                val directions =
                    LoanGuarantedFragmentDirections.actionLoanGuarantedFragmentToAcceptLoanRequestFragment(
                        it
                    )
                findNavController().navigate(directions)
            })
        binding.rvLoanRequst.adapter=adapter
        binding.rvLoanRequst.layoutManager=GridLayoutManager(requireContext(),1)
        viewModel.loansRequest.observe(viewLifecycleOwner, Observer {
            Log.d("TAG"," RESULTS $it")
            newRequest.clear()
            newRequest.addAll(it)
            adapter.notifyDataSetChanged()
        })





        loanGuarontedAdapter=LoanGuarontedAdapter(loans, LoanGuarontedAdapter.OnClickListener {
            val direction =
                LoanGuarantedFragmentDirections.actionLoanGuarantedFragmentToLoanGuarantedDetailsFragment(
                    it
                )
            findNavController().navigate(direction)
        },requireContext())
        binding.rvallLoanGuaranteed.adapter=loanGuarontedAdapter
        binding.rvallLoanGuaranteed.layoutManager=GridLayoutManager(requireContext(), 1)
        viewModel.loans.observe(viewLifecycleOwner, Observer {
            loans.clear()
            loans.addAll(it)
            loanGuarontedAdapter.notifyDataSetChanged()
        })




    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loans_guaranteed)
    }



}
