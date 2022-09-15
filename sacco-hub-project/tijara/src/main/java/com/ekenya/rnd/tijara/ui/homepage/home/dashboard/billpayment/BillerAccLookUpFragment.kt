package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.BillerAccLookUpFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.BillerLookupDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.merchants_items_lists.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*

class BillerAccLookUpFragment : Fragment() {
private lateinit var billerBinding:BillerAccLookUpFragmentBinding
    private lateinit var viewModel: BillersViewModel
   private var billercode=""
   private var billerName=""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        billerBinding= BillerAccLookUpFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(BillersViewModel::class.java)
        viewModel.billerName.observe(viewLifecycleOwner) { name ->
            billerName = name
            billerBinding.toolbar.custom_toolbar.custom_toolbar_title.text = name
        }
        var name1=""
        var posone=""
        var postwo=""
        viewModel.billerUrl.observe(viewLifecycleOwner) { logo ->
            if (logo.isNullOrEmpty()) {
                billerBinding.initials.makeVisible()
                val splited: List<String> = billerName?.split("\\s".toRegex())
                if (splited.count() == 2) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    billerBinding.initials.text = " $postwo $posone"
                } else if (splited.count() === 3) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    billerBinding.initials.text = " $postwo $posone"
                } else {
                    val names = billerName
                    posone = names[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = names[0].toString().toUpperCase(Locale.ENGLISH)
                    billerBinding.initials.text = " $postwo $posone"
                }

            } else {
                bindImage(billerBinding.ivLogo, logo)
            }

        }
        viewModel.billerCode.observe(viewLifecycleOwner) { code ->
            billercode = code
            billerBinding.apply {
                if (code.contains(getString(R.string.kplc))) {
                    billerBinding.tvDetails.text =
                        getString(R.string.please_provide_your_meter_number)
                    billerBinding.tlAccNo.hint = getString(R.string.enter_meter_number)
                    cbCorporate.visibility = View.GONE
                    cbSaveBiller.makeVisible()
                    cbSaveBiller.setText("Remember Meter Number")
                } else if (code.contains(getString(R.string.nhif))) {
                    billerBinding.tvDetails.text =
                        getString(R.string.please_provide_your_nhif_number)
                    billerBinding.tlAccNo.hint = getString(R.string.enter_nhif)
                    cbCorporate.visibility = View.VISIBLE
                    cbSaveBiller.makeVisible()
                    val isCoporate: Int
                    if (billerBinding.cbCorporate.isChecked) {
                        isCoporate = 1
                        Timber.d(" Checked is: $isCoporate")
                    } else {
                        isCoporate = 0
                        Timber.d(" Unchecked is: $isCoporate")
                    }
                }
            }


        }

        checkRememberUser()
        setupNavUp()
        return billerBinding.root
    }
    private fun rememberBller(account: String,checked: Boolean) {
        if (checked) {
            PrefUtils.setPreference(requireContext(), "isRememberMe", "true")
            PrefUtils.setPreference(requireContext(),"savedBiller",account)
        } else {
            PrefUtils.setPreference(requireContext(), "isRememberMe", "false")
            PrefUtils.setPreference(requireContext(),"savedBiller"," ")
        }
    }

    private fun checkRememberUser() {
        val savedBiller = PrefUtils.getPreferences(requireContext(), "savedBiller")
        if (PrefUtils.getPreferences(requireContext(), "isRememberMe").equals("true")) {
            billerBinding.cbSaveBiller.isChecked = true
            if (savedBiller?.isNotEmpty() == true) {
                billerBinding.etAccNo.setText(savedBiller)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                billerBinding.progressr.makeGone()
                when (it) {

                    1 -> {
                        billerBinding.btnAccLookUP.isEnabled=true
                        billerBinding.progressr.makeGone()
                            findNavController().navigate(R.id.action_billerAccLookUpFragment_to_payBillFragment)
                            viewModel.stopObserving()
                    }
                    0 -> {
                        billerBinding.btnAccLookUP.isEnabled=true
                        billerBinding.progressr.makeGone()
                        viewModel.stopObserving()
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                    }
                    else -> {
                        billerBinding.btnAccLookUP.isEnabled=true
                        viewModel.stopObserving()
                        billerBinding.progressr.makeGone()
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

                    }
                }
            }
        })
        billerBinding.apply {
            btnAccLookUP.setOnClickListener {
                val isChecked = billerBinding.cbSaveBiller.isChecked
                val meterNo=etAccNo.text.toString()
                if (meterNo.isEmpty()){
                    tlAccNo.error=getString(R.string.required)
                }else if (billercode.contains(getString(R.string.nhif)) && meterNo.length<8){
                    tlAccNo.error="Enter a valid nhif number"
                }else {
                    btnAccLookUP.isEnabled=false
                    tlAccNo.error = ""
                    billerBinding.progressr.visibility=View.VISIBLE
                    billerBinding.progressr.tv_pbTitle.visibility=View.GONE
                    billerBinding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    rememberBller(meterNo,isChecked)
                    val billerLookupDTO = BillerLookupDTO()
                    billerLookupDTO.accountNumber = meterNo
                    billerLookupDTO.billerCode = billercode
                    viewModel.billerLookUp(billerLookupDTO)

                }

            }
        }
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        billerBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}