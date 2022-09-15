package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ServiceProviderAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentDepositSuccessBinding
import com.ekenya.rnd.tijara.databinding.FragmentMakeDepositBinding
import com.ekenya.rnd.tijara.network.model.GenderItems
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.MakeDepositDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime.BuyAirtimeViewmodel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@Suppress("DEPRECATION")
class MakeDepositFragment : BaseDaggerFragment() {
    lateinit var binding: FragmentMakeDepositBinding
    lateinit var cardBinding: DepositDialogLayoutBinding
    lateinit var successBinding: FragmentDepositSuccessBinding
    var accountName=""
    var accountId=""
    var accountNumber=""
    var totalAmount=""
    var phone=""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(BuyAirtimeViewmodel::class.java)
    }
    private val dashboardModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= FragmentMakeDepositBinding.inflate(layoutInflater)
        setupNavUp()

        return  binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner=this
        binding.depositViewmodel=viewModel
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        /**spinner impl*/
        viewModel.savingAccountProperties.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                populateAccount(it)
            }else{
                toastyInfos("No saving account found")
            }
        })
        viewModel.loadingAccount.observe(viewLifecycleOwner) {
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

        viewModel.accountCode.observe(viewLifecycleOwner) {
            if (null != it) {
                when (it) {

                    1 -> {
                        binding.progressr.makeGone()
                        viewModel.stopObserving()

                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        binding.progressr.makeGone()
                        viewModel.stopObserving()}
                    else -> {
                        binding.progressr.makeGone()
                    viewModel.stopObserving()
                    }
                }
            }
        }
        viewModel.sProviderProperties.observe(viewLifecycleOwner, Observer {
            val adapter= ServiceProviderAdapter(requireContext(),it)
            adapter.notifyDataSetChanged()
            binding.content.providerSpinner.adapter=adapter
            binding.content.providerSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.SPROVIDERID= it[position].id.toString()
                    Constants.SPROVIDERNAME= it[position].name
                    Timber.d("SPROVIDERNAME is:  ${Constants.SPROVIDERNAME}")
                }
            }
        })
        binding.content.etPhone.setText(Constants.PHONENUMBER)
        binding.content.btnDeposit.setOnClickListener {
            with(binding.content) {

                val amount=etAmount.text.toString()
                val validMsg = FieldValidators.VALIDINPUT
                val phoneNumber = FieldValidators().formatPhoneNumber(binding.content.etPhone.text.toString())
                val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                if (amount.isEmpty()){
                    tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                }else if( accountName.isBlank()){
                    toastyInfos("Select Account")
                }else if (Constants.SPROVIDERNAME.toLowerCase(Locale.ROOT).isBlank() ) {
                    toastyInfos("Select Service Provider")
                }else if (!validPhone.contentEquals(validMsg)){
                    etPhone.requestFocus()
                    tlPhone.isErrorEnabled=true
                    tlPhone.error=validPhone
                }else{
                    tlEnterAmount.error=""
                    binding.content.btnDeposit.isEnabled=false
                    val makeDepositDTO= MakeDepositDTO()
                    makeDepositDTO.accountId= accountId.toInt()
                    makeDepositDTO.amount=amount
                    totalAmount=makeDepositDTO.amount
                    makeDepositDTO.providerId= Constants.SPROVIDERID
                    makeDepositDTO.providerPhone=phoneNumber
                    phone=makeDepositDTO.providerPhone
                    viewModel.setPhone(makeDepositDTO.providerPhone)
                    viewModel.setAmount(totalAmount)
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    viewModel.makeDepositToSavingAccount(makeDepositDTO)
                    viewModel.stopObserving()

                }
            }
        }
        viewModel.depositStatusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        binding.content.btnDeposit.isEnabled=false
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding= DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text=getString(R.string.confirm_deposit)
                            tvName.text=getString(R.string.deposit_from)
                            tvNameValue.text=phone
                            tvBank.text=getString(R.string.deposit_to)
                            val accNumber=accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                            tvBankValue.text="$accountName - A/C $accNumber"
                            tvACNO.text=getString(R.string.amoun)
                            tvACNOValue.text= String.format(getString(R.string.kesh),totalAmount)
                            tvAmount.text=getString(R.string.charges)
                            viewModel.charges.observe(viewLifecycleOwner) { charges ->
                                tvAmountValue.text = "KES $charges"
                            }
                            viewModel.duty.observe(viewLifecycleOwner){duty->
                                tvFromValur.text="KES $duty"
                            }

                            tvFrom.text=getString(R.string.excise_duty)
                            tvDutyValue.makeGone()
                            tvDuty.makeGone()
                        }

                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                            viewModel.stopObserving()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            findNavController().navigate(R.id.pinFragment)
                            viewModel.stopObserving()

                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)


                    }
                    0 -> {
                        binding.content.btnDeposit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.content.btnDeposit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()

                    }
                }
            }
        })
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                pinViewModel.unsetAuthSuccess()
                binding.progressr.visibility = View.VISIBLE
                binding.progressr.tv_pbTitle.visibility = View.GONE
                binding.progressr.tv_pbTex.text = getString(R.string.we_are_processing_requesrt)
                viewModel.commitDeposit()
                pinViewModel.stopObserving()
                viewModel.stopObserving()

            }
        }
        //observe commit
        viewModel.depositCommitCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        binding.content.btnDeposit.isEnabled=true
                        with(binding.content) {
                            etPhone.setText("")
                            etAmount.setText("")
                        }
                        binding.progressr.visibility=View.GONE
                        val directions=MakeDepositFragmentDirections.actionMakeDepositFragmentToSuccessDepositFragment(fragmentType = 1)
                        findNavController().navigate(directions)
                        viewModel.stopObserving()
                        dashboardModel.setRefresh(true)
                    }
                    0 -> {
                        binding.content.btnDeposit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewModel.statusCMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.content.btnDeposit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()

                    }
                }
            }
        })

    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.make_deposit)
    }
    private fun populateAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with( binding.content){
            accountSpinner.setAdapter(typeAdapter)
            accountSpinner.keyListener = null
            accountSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                accountName = selected.accountName
                accountId = selected.accountId.toString()
                accountNumber = selected.accountNo
                viewModel.setAccNumber(selected.accountNo)
                viewModel.setAccountName(selected.accountName)
            }
        }
    }

}