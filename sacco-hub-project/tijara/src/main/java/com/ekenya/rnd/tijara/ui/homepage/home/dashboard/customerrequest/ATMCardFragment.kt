package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ChequeBranchAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.CancelDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentAtmCardBinding
import com.ekenya.rnd.tijara.requestDTO.ATMDTO
import com.ekenya.rnd.tijara.requestDTO.MCardDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import javax.inject.Inject

class ATMCardFragment : BaseDaggerFragment() {
private lateinit var binding:FragmentAtmCardBinding
 private lateinit var viewModel: MemberShipCardViewModel
 private lateinit var cardBinding: CancelDialogLayoutBinding
    var charges=""
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private val dashboardModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }
    var orgBranchName=""
    var orgBranchId=""
    var saveName=""
    var saveId=""
    private var isCardSelected = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAtmCardBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(MemberShipCardViewModel::class.java)
        viewModel._charges.observe(viewLifecycleOwner,{
            charges= FormatDigit.formatDigits(it)
        })
        if (isCardSelected==1){
            isCardSelected=1
            Timber.d("CARD  $isCardSelected")
            binding.rbMyself.isChecked=true
            binding.rbOthers.isChecked=false

        }else{
            isCardSelected=2
            binding.rbMyself.isChecked=false
            binding.rbOthers.isChecked
        }
        binding.apply {
            rbMyself.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    rbOthers.isChecked=false
                    isCardSelected=1
                }

            }
            rbOthers.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    rbMyself.isChecked=false
                    isCardSelected=2
                }

            }
            btnSubmit.setOnClickListener {
                if (orgBranchName.isEmpty()){
                    toastyInfos("select branch")
                }else if (saveName.isEmpty()){
                    toastyInfos("select account")
                }else{
                    val atmdto=ATMDTO()
                    atmdto.branchId=orgBranchId
                    atmdto.accountNoToLink=saveId
                    atmdto.requestType="ATM_CARD"
                    if (isCardSelected==1){
                        atmdto.isNew=1
                    }
                    if (isCardSelected==2){
                        atmdto.isNew=0
                    }
                    btnSubmit.isEnabled=false
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    viewModel.createATM(atmdto)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.savingPrimeProperties.observe(viewLifecycleOwner,  {
            val adapter= SavingAccountAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.spAcc.adapter=adapter
            binding.spAcc.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    saveId= it[position].accountNo
                    saveName= it[position].accountName
                }
            }
        })
        viewModel.chequeBranch.observe(viewLifecycleOwner,  {
            val adapter= ChequeBranchAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.spBranch.adapter=adapter
            binding.spBranch.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    orgBranchId= it[position].id.toString()
                    orgBranchName= it[position].name
                    viewModel.setBankName(it[position].name)
                }
            }
        })
        viewModel.statusATM.observe(viewLifecycleOwner,  {
            if (null != it) {
                binding.btnSubmit.isEnabled=true
                binding.progressr.makeGone()
                viewModel.stopObserving()
                when (it) {
                    1 -> {
                        binding.progressr.makeGone()
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding = CancelDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            if (isCardSelected==1){
                                tvHeading.text="Confirm Payment"
                                tvName.text="You are about to request for an ATM card"
                                tvFromValur.text= String.format(getString(R.string.kesh),charges)

                            }else{
                                tvHeading.text="Confirm Payment"
                                tvName.text="You are about to request for an ATM card replacement"
                                tvFromValur.text= String.format(getString(R.string.kesh),charges)

                            }

                        }

                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            dialog.hide()
                            findNavController().navigate(R.id.action_ATMCardFragment_to_pinFragment)
                            viewModel.stopObserving()
                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)

                    }
                    0 -> {

                        binding.btnSubmit.isEnabled=true
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(),viewModel.atmMessage.value)
                        viewModel.stopObserving()
                    }

                    else -> {

                        binding.btnSubmit.isEnabled=true
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()
                    }
                }
            }
        })
        pinViewModel.authSuccess.observe(viewLifecycleOwner, {
            if (it == true) {

                pinViewModel.unsetAuthSuccess()
                binding.progressr.visibility=View.VISIBLE
                binding.progressr.tv_pbTitle.visibility=View.GONE
                binding.progressr.tv_pbTex.text= getString(R.string.we_are_processing_requesrt)
                viewModel.commitMemCard()
                pinViewModel.stopObserving()
                viewModel.stopObserving()

            }
        })
        //observe commit
        viewModel.commitCheque.observe(viewLifecycleOwner,  {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        viewModel.stopObserving()
                        binding.progressr.visibility=View.GONE
                        if (isCardSelected==2){
                            val builder = AlertDialog.Builder(requireContext())
                                .setTitle("ATM card replacement \nrequest successful!")
                                .setMessage("Your request has been received and is being processed")
                                .setPositiveButton("OK") { _, which ->
                                    findNavController().navigateUp()
                                }
                                .setIcon(R.drawable.ic_check_sucess)
                                .setCancelable(false)
                            builder .show()
                        }else{
                            val builder = AlertDialog.Builder(requireContext())
                                .setTitle("ATM card request successful!")
                                .setMessage("Your request has been received and is being processed")
                                .setPositiveButton("OK") { _, which ->
                                    findNavController().navigateUp()
                                }
                                .setIcon(R.drawable.ic_check_sucess)
                                .setCancelable(false)
                            builder .show()

                        }

                        viewModel.stopObserving()
                        dashboardModel.setRefresh(true)
                    }
                    0 -> {
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewModel.commiitCheMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()

                    }
                }
            }
        })
        setupNavUp()

    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "ATM Card Request"
    }

}