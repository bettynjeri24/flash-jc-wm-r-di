package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney.mobilemoneytransfer

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ServiceProviderAdapter
import com.ekenya.rnd.tijara.databinding.AirtimeConfirmDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.MobileMoneyTFragmentBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.BuyAirtimeDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime.BuyAirtimeViewmodel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MobileMoneyTFragment : BaseDaggerFragment() {
    var listPhone=""
    var phone=""
    var totalAmount=""
    var accountName=""
    var accId=-1
    var accountNumber=""
    private lateinit var cardDialog:AirtimeConfirmDialogLayoutBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewmodel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(BuyAirtimeViewmodel::class.java)
    }
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
private lateinit var mMoneyBinding:MobileMoneyTFragmentBinding
private var isCardSelected = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mMoneyBinding= MobileMoneyTFragmentBinding.inflate(layoutInflater)
        setupNavUp()
        mMoneyBinding.progressr.visibility=View.VISIBLE
        mMoneyBinding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
        mMoneyBinding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        viewmodel.loadingAccount.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    mMoneyBinding.progressr.makeVisible()
                }
                false -> {
                    mMoneyBinding.progressr.visibility = View.GONE
                }
                else -> {
                    mMoneyBinding.progressr.makeGone()
                }
            }
        }
        viewmodel.accountCode.observe(viewLifecycleOwner) {
            if (null != it) {
                mMoneyBinding.progressr.visibility = View.GONE
                when (it) {

                    1 -> {
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        viewmodel.stopObserving()
                    }
                    else -> {
                        viewmodel.stopObserving()
                    }
                }
            }

        }
        return mMoneyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("contact")?.observe(viewLifecycleOwner) {result ->
            mMoneyBinding.content.etPhone.setText(result)
            listPhone=result

        }
        with(mMoneyBinding.content){
        if (isCardSelected==1){
            isCardSelected=1
            Timber.d("CARD  $isCardSelected")
            rbMyself.isChecked=true
            rbOthers.isChecked=false
            etPhone.setText(Constants.PHONENUMBER)
           etPhone.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        }else {
            isCardSelected = 2
            rbMyself.isChecked = false
            rbOthers.isChecked
            Timber.d("CARD SELECTEEEEEEEEED $isCardSelected")
            etPhone.setCompoundDrawablesWithIntrinsicBounds(
                null, null,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_contacts_book), null
            )
            etPhone.onRightDrawableClicked {
                Constants.CONTACTFROM = 1
                findNavController().navigate(R.id.listContactFragment)
            }
        }
        }
        with(mMoneyBinding.content) {
            rbMyself.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    etPhone.setText(Constants.PHONENUMBER)
                    rbOthers.isChecked=false
                    isCardSelected=1
                    etPhone.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
                }

            }
            rbOthers.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    rbMyself.isChecked=false
                    isCardSelected=2
                    etPhone.setCompoundDrawablesWithIntrinsicBounds(null,null,
                        ContextCompat.getDrawable(requireContext(),R.drawable.ic_contacts_book),null)
                    etPhone.setText(listPhone)
                    etPhone.onRightDrawableClicked {
                        Constants.CONTACTFROM=1
                        findNavController().navigate(R.id.listContactFragment)
                    }

                }

            }
            /**spinner impl*/
            viewmodel.savingPrimeProperties.observe(viewLifecycleOwner, Observer {
                if (it!=null){
                    populateAccount(it)
                }else{
                    toastyInfos("No saving account found")
                }
            })
            viewmodel.sProviderProperties.observe(viewLifecycleOwner, Observer {
                val adapter= ServiceProviderAdapter(requireContext(),it)
                adapter.notifyDataSetChanged()
                mMoneyBinding.content.providerSpinner.adapter=adapter
                mMoneyBinding.content.providerSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        Constants.SPROVIDERID= it[position].id.toString()
                        Constants.SPROVIDERNAME= it[position].name
                        Timber.d("SPROVIDERNAME is:  ${Constants.SPROVIDERNAME}")
                    }
                }
            })
            mMoneyBinding.content.btnSendMoney.setOnClickListener {
                with(mMoneyBinding.content) {

                    val amount=etAmount.text.toString()
                    val validMsg = FieldValidators.VALIDINPUT
                    val phoneNumber = FieldValidators().formatPhoneNumber(etPhone.text.toString())
                    val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                    if (amount.isEmpty()){
                        tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                    }else if (!validPhone.contentEquals(validMsg)){
                        etPhone.requestFocus()
                        tlPhone.isErrorEnabled=true
                        tlPhone.error=validPhone
                    }else if (Constants.SPROVIDERNAME.toLowerCase(Locale.ROOT).isEmpty()) {
                        toastyInfos("Select Service Provider")
                    }else if (accountName.isEmpty() ) {
                        toastyInfos("Select Account")
                    } else {
                        tlEnterAmount.error=""
                        tlPhone.error=""
                        mMoneyBinding.content.btnSendMoney.isEnabled=false
                        val buyAirtimeDTO= BuyAirtimeDTO()
                        buyAirtimeDTO.amount=amount
                        buyAirtimeDTO.phoneNumber=phoneNumber
                        buyAirtimeDTO.providerId= Constants.SPROVIDERID
                        buyAirtimeDTO.accountId= accId
                        phone=buyAirtimeDTO.phoneNumber
                        totalAmount=buyAirtimeDTO.amount
                        buyAirtimeDTO.withdrawalType=2
                        viewmodel.setPhone(phone)
                        viewmodel.setAmount(totalAmount)
                        mMoneyBinding.progressr.visibility=View.VISIBLE
                        mMoneyBinding.progressr.tv_pbTitle.visibility=View.GONE
                        mMoneyBinding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                        viewmodel.buyAirtimePreview(buyAirtimeDTO)
                        viewmodel.stopObserving()

                    }
                }
            }
            viewmodel.statusCode.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    mMoneyBinding.progressr.visibility=View.GONE
                    when (it) {

                        1 -> {
                            mMoneyBinding.content.btnSendMoney.isEnabled=false
                            mMoneyBinding.progressr.visibility=View.GONE
                            val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                            cardDialog= AirtimeConfirmDialogLayoutBinding.inflate(LayoutInflater.from(context))
                            viewmodel.airtimeResponse.observe(viewLifecycleOwner, Observer {airtimeData->
                                cardDialog.apply {
                                    tvHeading.text=getString(R.string.confirm_mobile_money_transfer)
                                    if (Constants.SPROVIDERNAME=="MPESA"){
                                        tvName.text=getString(R.string.send_to_mpesa)
                                    }else if (Constants.SPROVIDERNAME=="AIRTEL MONEY"){
                                        tvName.text=getString(R.string.send_to_airtel)
                                    }else if (Constants.SPROVIDERNAME=="T-KASH"){
                                        tvName.text=getString(R.string.send_to_tcash)
                                    }
                                    tvNameValue.text=phone
                                    tvBank.text=getString(R.string.transfer_from)
                                    val accNumber=accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                                    tvBankValue.text="$accountName - A/C $accNumber"
                                    tvACNO.text=getString(R.string.amount)
                                    tvACNOValue.text=String.format(getString(R.string.kesh),totalAmount)
                                    tvAmount.text=getString(R.string.charges)
                                    tvAmountValue.text = String.format(getString(R.string.kesh),airtimeData.charges)
                                }
                                cardDialog.btnCancel.setOnClickListener {
                                    dialog.dismiss()
                                    viewmodel.stopObserving()
                                }
                                cardDialog.btnSubmit.setOnClickListener {
                                    dialog.dismiss()
                                    findNavController().navigate(R.id.pinFragment)
                                    viewmodel.stopObserving()

                                }
                            })

                            dialog.setContentView(cardDialog.root)
                            dialog.show()
                            dialog.setCancelable(false)


                        }
                        0 -> {
                            mMoneyBinding.content.btnSendMoney.isEnabled=true
                            viewmodel.stopObserving()
                            mMoneyBinding.progressr.visibility=View.GONE
                            onInfoDialog(requireContext(),viewmodel.statusMessage.value)
                        }
                        else -> {
                            mMoneyBinding.content.btnSendMoney.isEnabled=true
                            viewmodel.stopObserving()
                            mMoneyBinding.progressr.visibility=View.GONE
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))

                        }
                    }
                }
            })
            pinViewModel.authSuccess.observe(viewLifecycleOwner) {
                if (it == true) {
                    pinViewModel.unsetAuthSuccess()
                    mMoneyBinding.progressr.visibility = View.VISIBLE
                    mMoneyBinding.progressr.tv_pbTitle.visibility = View.GONE
                    mMoneyBinding.progressr.tv_pbTex.text =
                        getString(R.string.we_are_processing_requesrt)
                    viewmodel.airtimeCommit()
                    pinViewModel.stopObserving()
                    viewmodel.stopObserving()
                }
            }
            //observe commit
            viewmodel.status.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    mMoneyBinding.progressr.visibility=View.GONE
                    when (it) {

                        1 -> {
                            mMoneyBinding.content.btnSendMoney.isEnabled=true
                            mMoneyBinding.apply {
                                etAmount.setText("")
                                etPhone.setText("")
                            }
                            mMoneyBinding.progressr.visibility=View.GONE
                            val directions=MobileMoneyTFragmentDirections.actionMobileMoneyTFragmentToSendMoneySuccessFragment(fragmentType = 4)
                            findNavController().navigate(directions)
                            viewmodel.stopObserving()
                        }
                        0 -> {
                            mMoneyBinding.content.btnSendMoney.isEnabled=true
                            viewmodel.stopObserving()
                            mMoneyBinding.progressr.visibility=View.GONE
                            onInfoDialog(requireContext(),viewmodel.statusCMessage.value)
                        }
                        else -> {
                            mMoneyBinding.content.btnSendMoney.isEnabled=true
                            viewmodel.stopObserving()
                            mMoneyBinding.progressr.visibility=View.GONE
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))

                        }
                    }
                }
            })
        }
    }
    private fun populateAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with(mMoneyBinding.content) {
            accountSpinner.setAdapter(typeAdapter)
            accountSpinner.keyListener = null
            accountSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                accountName = selected.accountName
                accId = selected.accountId
                accountNumber = selected.accountNo
                viewmodel.setAccNumber(selected.accountNo)
                viewmodel.setAccountName(selected.accountName)
            }
        }
    }


    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        mMoneyBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        mMoneyBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.mobile_money_transfer)
    }




}