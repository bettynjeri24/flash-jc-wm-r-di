package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.BillerAccLookUpFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.BillerLookupDTO
import com.ekenya.rnd.tijara.utils.*
import com.ekenya.rnd.tijara.utils.bindImage
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
/**hold data for bills generated with amount*/
class KplcLookUpFragment : Fragment() {
    private lateinit var billerBinding:BillerAccLookUpFragmentBinding
    private var billercode=""
    private var billerName=""
    private lateinit var viewModel: BillersViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        billerBinding= BillerAccLookUpFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(BillersViewModel::class.java)
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
            Log.d("TAG", "CODEB $billercode")
            Log.d("TAG", "CODB $code")
            billerBinding.apply {
                if (billercode.contains(getString(R.string.kplc))) {
                    billerBinding.tvDetails.text =
                        getString(R.string.please_provide_your_meter_number)
                    billerBinding.tlAccNo.hint = getString(R.string.enter_meter_number)
                    cbCorporate.visibility = View.GONE
                } else {
                    billerBinding.tvDetails.text =
                        getString(R.string.please_provide_your_account_number)
                    billerBinding.tlAccNo.hint = getString(R.string.enter_account_number)
                    cbCorporate.visibility = View.GONE
                }
            }

        }
        viewModel.billerName.observe(viewLifecycleOwner) { name ->
            billerName=name
            billerBinding.toolbar.custom_toolbar.custom_toolbar_title.text = name
        }

        setupNavUp()
        return billerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                when (it) {
                    1 -> {
                        billerBinding.btnAccLookUP.isEnabled=true
                        billerBinding.progressr.makeGone()
                        findNavController().navigate(R.id.action_kplcLookUpFragment_to_payBillFragment)
                         viewModel.stopObserving()
                    }
                    0 -> {
                        billerBinding.btnAccLookUP.isEnabled=true
                        billerBinding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                        viewModel.stopObserving()

                    }
                    else -> {
                        billerBinding.btnAccLookUP.isEnabled=true
                        billerBinding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()


                    }
                }
            }
        })
        billerBinding.apply {
            btnAccLookUP.setOnClickListener {
                val meterNo=etAccNo.text.toString()
                if (meterNo.isEmpty()){
                    tlAccNo.error=getString(R.string.required)
                }else {
                    billerBinding.btnAccLookUP.isEnabled=false
                    tlAccNo.error = ""
                    billerBinding.progressr.visibility=View.VISIBLE
                    billerBinding.progressr.tv_pbTitle.visibility=View.GONE
                    billerBinding.progressr.tv_pbTex.text= getString(R.string.we_are_processing_requesrt)
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