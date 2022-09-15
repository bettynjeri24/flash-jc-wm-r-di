package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.CountyAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.DurationAccountAdapter
import com.ekenya.rnd.tijara.databinding.FragmentCountyLookUpBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.requestDTO.CountyLookUpDTO
import com.ekenya.rnd.tijara.requestDTO.ParkingLookUpDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*

class CountyLookUpFragment : Fragment() {
    private lateinit var binding:FragmentCountyLookUpBinding
    private lateinit var viewmodel: BillersViewModel
    private var billercode=""
    private var billerName=""
    private var countyId=-1
    private var countyIName=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCountyLookUpBinding.inflate(layoutInflater)
        viewmodel= ViewModelProvider(requireActivity()).get(BillersViewModel::class.java)
        binding.lifecycleOwner=this
        var name1=""
        var posone=""
        var postwo=""
        viewmodel.billerUrl.observe(viewLifecycleOwner) { logo ->
            if (logo.isNullOrEmpty()) {
                binding.initials.makeVisible()
                val splited: List<String> = billerName?.split("\\s".toRegex())
                if (splited.count() == 2) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    binding.initials.text = " $postwo $posone"
                } else if (splited.count() === 3) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    binding.initials.text = " $postwo $posone"
                } else {
                    val names = billerName
                    posone = names[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = names[0].toString().toUpperCase(Locale.ENGLISH)
                    binding.initials.text = " $postwo $posone"
                }

            } else {
                bindImage(binding.ivLogo, logo)
            }

        }
        viewmodel.billerCode.observe(viewLifecycleOwner) { code ->
            billercode = code
        }
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_county)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        viewmodel.loadingAccount.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.progressr.makeVisible()
                }
                false -> {
                    binding.progressr.visibility = View.GONE
                }
                else -> {
                    binding.progressr.makeGone()
                }
            }
        }
        viewmodel.accountCode.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressr.visibility = View.GONE
                when (it) {

                    1 -> {
                        binding.progressr.visibility = View.GONE
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        binding.progressr.visibility = View.GONE
                        viewmodel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility = View.GONE
                    }
                }
            }

        }
        viewmodel.billerName.observe(viewLifecycleOwner) { name ->
            billerName=name
            binding.toolbar.custom_toolbar.custom_toolbar_title.text = name
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.county.observe(viewLifecycleOwner) { county ->
            if (county != null) {
                populateCounty(county)
            } else {
                toastyInfos("No saving account found")
            }

        }
        viewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        binding.content.btnAccLookUP.isEnabled=true
                        findNavController().navigate(R.id.action_countyLookUpFragment_to_payBillFragment)
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        binding.content.btnAccLookUP.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusMessage.value)
                        viewmodel.stopObserving()

                    }
                    else -> {
                        binding.content.btnAccLookUP.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewmodel.stopObserving()
                    }
                }
            }
        })
        setupNavUp()

            with(binding.content){
                btnAccLookUP.setOnClickListener {
                    val billNo=etAccNo.text.toString()
                     if (countyIName.isEmpty()){
                        tlAccNo.error=""
                        toastyInfos("Select county")
                    }else if (billNo.isEmpty()){
                         tlAccNo.error=getString(R.string.required)
                    }else{
                        tlAccNo.error=""
                         binding.content.btnAccLookUP.isEnabled=false
                         val countyLookUpDTO = CountyLookUpDTO()
                        countyLookUpDTO.accountNumber = billNo
                        countyLookUpDTO.billerCode = billercode
                        countyLookUpDTO.countyId = countyId
                        binding.progressr.visibility = View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility = View.GONE
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        viewmodel.countyLookup(countyLookUpDTO)
                    }
                }
            }

    }
    private fun populateCounty(accList: List<County>) {
        val typeAdapter =ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
       with(binding.content) {
            countySpinner.setAdapter(typeAdapter)
            countySpinner.keyListener = null
            countySpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: County =
                    parent.adapter.getItem(position) as County
                countyId = selected.iD
                countyIName = selected.name
                viewmodel.setCountyName(selected.name)
            }
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}