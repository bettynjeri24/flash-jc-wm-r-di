package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.ekenya.rnd.tijara.utils.FieldValidators
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ServiceProviderAdapter
import com.ekenya.rnd.tijara.databinding.WithdrawSavingFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.WithdrawSavingDTO
import com.ekenya.rnd.tijara.utils.SpinKitLoading
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class WithdrawSavingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(WithdrawSavingViewModel::class.java)
    }
private lateinit var binding: WithdrawSavingFragmentBinding
    private lateinit var spinKitLoading: SpinKitLoading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        spinKitLoading= SpinKitLoading(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= WithdrawSavingFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.withdrawViewModel=viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                spinKitLoading.isDismiss()
                when (it) {

                    1 -> {
                        val successAlert: SweetAlertDialog = SweetAlertDialog(
                            context,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                        successAlert.setTitleText(getString(R.string.success))
                            .setContentText(getString(R.string.withdraw_is_successful))
                            .setConfirmClickListener { sDialog ->
                                findNavController().navigateUp()
                                viewModel.stopObserving()
                                sDialog.dismissWithAnimation()
                            }
                            .show()
                        successAlert.setCancelable(false)
                        viewModel.stopObserving()

                    }
                    0 -> {
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                    }
                    else -> {
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

                    }
                }
            }
        })
        /**spinner impl*/
        viewModel.savingAccountProperties.observe(viewLifecycleOwner, Observer {
            val adapter= SavingAccountAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.accountSpinner.adapter=adapter
            binding.accountSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.SAVINGACCOUNTID= it[position].accountId
                    Constants.SAVINGACCOUNTNO= it[position].accountNo
                    Constants.SAVINGACCOUNTNAME= it[position].accountName
                    Timber.d("SAVINGACCOUNTNAME is:  ${Constants.SAVINGACCOUNTNAME}")
                }
            }
        })
        viewModel.sProviderProperties.observe(viewLifecycleOwner, Observer {
            val adapter= ServiceProviderAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.providerSpinner.adapter=adapter
            binding.providerSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.SPROVIDERID= it[position].id.toString()
                    Constants.SPROVIDERNAME= it[position].name
                    Timber.d("SPROVIDERNAME is:  ${Constants.SPROVIDERNAME}")
                }
            }
        })
        binding.etPhone.setText(Constants.PHONENUMBER)
        binding.btnWithdraw.setOnClickListener {
            binding.apply {

                val amount=etAmount.text.toString()
                val validMsg = FieldValidators.VALIDINPUT
               val phoneNumber = FieldValidators().formatPhoneNumber(binding.etPhone.text.toString())
                val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                if (amount.isEmpty()){
                    tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                }else if (Constants.SAVINGACCOUNTNAME.toLowerCase(Locale.ROOT).contains("select") ) {
                    showToast(requireContext(),"Select Account")
                }else if (Constants.SPROVIDERNAME.toLowerCase(Locale.ROOT).contains("select") ) {
                    showToast(requireContext(),"Select Service Provider")
                }else if (!validPhone.contentEquals(validMsg)){
                    etPhone.requestFocus()
                    tlPhone.isErrorEnabled=true
                    tlPhone.error=validPhone
                }else{
                    tlEnterAmount.error=""
                    val withdrawSavingDTO= WithdrawSavingDTO()
                    withdrawSavingDTO.accountId= Constants.SAVINGACCOUNTID
                    withdrawSavingDTO.amount=amount
                    withdrawSavingDTO.providerId= Constants.SPROVIDERID
                    withdrawSavingDTO.providerPhone=phoneNumber
                    spinKitLoading.startLoading()
                    viewModel.withdrawCash(withdrawSavingDTO)

                }
            }
        }
        setupNavUp()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.withdraw)
    }

}