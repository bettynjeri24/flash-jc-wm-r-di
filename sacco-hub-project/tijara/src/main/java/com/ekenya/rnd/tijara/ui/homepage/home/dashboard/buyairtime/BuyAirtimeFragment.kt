package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime

import android.app.Dialog
import android.os.Bundle
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
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ServiceProviderAdapter
import com.ekenya.rnd.tijara.databinding.AirtimeConfirmDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentBuyAirtimeBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.BuyAirtimeDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class BuyAirtimeFragment : BaseDaggerFragment() {
    var listPhone=""
    var phone=""
    var totalAmount=""
    var accountName=""
    var accId=-1
    var accountNumber=""
    private lateinit var binding: FragmentBuyAirtimeBinding
    private lateinit var cardDialog:AirtimeConfirmDialogLayoutBinding
    private var isCardSelected = 1
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewmodel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(BuyAirtimeViewmodel::class.java)
    }
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyAirtimeBinding.inflate(layoutInflater)
        setupNavUp()
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
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
        with(binding.content){
        if (isCardSelected == 1) {
            isCardSelected = 1
            Timber.d("CARD  $isCardSelected")
            rbMyself.isChecked = true
           rbOthers.isChecked = false
            etPhone.setText(Constants.PHONENUMBER)
           etPhone.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else {
            isCardSelected = 2
            rbMyself.isChecked = false
            rbOthers.isChecked
            Timber.d("CARD SELECTEEEEEEEEED $isCardSelected")
            etPhone.setCompoundDrawablesWithIntrinsicBounds(
                null, null,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_contacts_book), null
            )
            etPhone.onRightDrawableClicked {
                findNavController().navigate(R.id.listContactFragment)
            }
        }
    }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("contact")?.observe(viewLifecycleOwner) {result ->
            binding.content.etPhone.setText(result)
            listPhone=result

        }
        with(binding.content) {
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
                        Constants.CONTACTFROM=0
                        findNavController().navigate(R.id.listContactFragment)
                    }

                }

            }
        }
        viewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        binding.content. btnAirtime.isEnabled=false
                        binding.progressr.visibility=View.GONE
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardDialog= AirtimeConfirmDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        viewmodel.airtimeResponse.observe(viewLifecycleOwner, Observer {airtimeData->
                            cardDialog.apply {
                                val accNumber=accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                                tvNameValue.text="$accountName - A/C $accNumber"
                                tvBankValue.text=phone
                                tvACNOValue.text =String.format(getString(R.string.kesh),totalAmount)
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
                        binding.content. btnAirtime.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusMessage.value)
                    }
                    else -> {
                        binding.content. btnAirtime.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

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
                viewmodel.airtimeCommit()
                pinViewModel.stopObserving()
                viewmodel.stopObserving()

            }
        }
        //observe commit
        viewmodel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        binding.content. btnAirtime.isEnabled=true
                        binding.content.etAmount.setText("")
                        binding.content.etPhone.setText("")
                        binding.progressr.visibility=View.GONE
                        val direction=BuyAirtimeFragmentDirections.actionBuyAirtimeFragmentToSuccessDepositFragment(fragmentType = 0)
                        findNavController().navigate(direction)
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        binding.content. btnAirtime.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusCMessage.value)
                        viewmodel.stopObserving()

                    }
                    else -> {
                        binding.content. btnAirtime.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewmodel.stopObserving()
                    }
                }
            }
        })
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
        binding.content.btnAirtime.setOnClickListener {
            with(binding.content) {

                val amount=etAmount.text.toString()
                val validMsg = FieldValidators.VALIDINPUT
                val phoneNumber = FieldValidators().formatPhoneNumber( binding.content.etPhone.text.toString())
                val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                if (amount.isEmpty()){
                    tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                }else if (!validPhone.contentEquals(validMsg)){
                    etPhone.requestFocus()
                    tlPhone.isErrorEnabled=true
                    tlPhone.error=validPhone
                }else if (Constants.SPROVIDERNAME.toLowerCase(Locale.ROOT).isEmpty() ) {
                    toastyInfos("Select Service Provider")
                }else if (accountName.isEmpty() ) {
                    toastyInfos("Select Account")
                } else {
                    btnAirtime.isEnabled=false
                    tlEnterAmount.error=""
                    val buyAirtimeDTO= BuyAirtimeDTO()
                    buyAirtimeDTO.amount=amount
                    buyAirtimeDTO.phoneNumber=phoneNumber
                    buyAirtimeDTO.providerId= Constants.SPROVIDERID
                    buyAirtimeDTO.accountId= accId
                    buyAirtimeDTO.withdrawalType=1
                    phone=buyAirtimeDTO.phoneNumber
                    viewmodel.setPhone(phone)
                    totalAmount= buyAirtimeDTO.amount
                    viewmodel.setAmount(buyAirtimeDTO.amount)
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    viewmodel.buyAirtimePreview(buyAirtimeDTO)
                    viewmodel.stopObserving()

                }
            }
        }
    }


    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.buy_airtime)
    }
    private fun populateAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with(binding.content) {
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




}