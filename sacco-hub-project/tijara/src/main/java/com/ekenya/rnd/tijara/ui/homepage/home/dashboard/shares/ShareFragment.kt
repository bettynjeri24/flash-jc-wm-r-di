package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

import com.ekenya.rnd.tijara.databinding.FragmentShareBinding
import com.ekenya.rnd.tijara.databinding.ShareDialogLayoutBinding

import com.ekenya.rnd.tijara.requestDTO.ShareDTO
import com.ekenya.rnd.tijara.utils.SpinKitLoading
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.util.*
import javax.inject.Inject

class ShareFragment : BaseDaggerFragment() {
    private lateinit var binding: FragmentShareBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewmodel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(ShareViewModel::class.java)
    }
    private lateinit var cardShareBinding: ShareDialogLayoutBinding
    private lateinit var spinKitLoading: SpinKitLoading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spinKitLoading= SpinKitLoading(requireActivity())

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.textColor)
        binding= FragmentShareBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner=this
        binding.viewmodel=viewmodel
        setupNavUp()
        viewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                spinKitLoading.isDismiss()
                when (it) {

                    1 -> {
                        val successAlert: SweetAlertDialog = SweetAlertDialog(
                            context,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                        successAlert.setTitleText(getString(R.string.success))
                            .setContentText(getString(R.string.share_bought_successfully))
                            .setConfirmClickListener { sDialog ->
                                findNavController().navigateUp()
                                viewmodel.stopObserving()
                                sDialog.dismissWithAnimation()
                            }
                            .show()
                        successAlert.setCancelable(false)
                        viewmodel.stopObserving()

                    }
                    0 -> {
                        onInfoDialog(requireContext(),viewmodel.statusMessage.value)
                    }
                    else -> {
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

                    }
                }
            }
        })
        binding.shareBtn.setOnClickListener {
          /*  binding.apply {

                val amount=etPhone.text.toString()
                if (amount.isEmpty()){
                    tlPhone.error=getString(R.string.amount_cannot_be_empty)
                }else if (Constants.SAVINGACCOUNTNAME.toLowerCase(Locale.ROOT).contains("select") ) {
                    showToast(requireContext(),"Select Share Account")
                }else{
                    tlPhone.error=""
                    val buyShareDTO= ShareDTO()
                    buyShareDTO.accountId= Constants.SAVINGACCOUNTID
                    buyShareDTO.amount=amount
                    spinKitLoading.startLoading()
                    viewmodel?.buyShare(buyShareDTO)
                }
            }*/
        }

    }



    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        if (Constants.DIALOGSELETED == 1) {
            binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.sell_shares)
            binding.tvTitleShare.text=getString(R.string.how_much_would_you_like_to_sell)

        } else {
            binding.toolbar.custom_toolbar.custom_toolbar_title.text =
                getString(R.string.transfer_shares)
            binding.tvTitleShare.text=getString(R.string.how_much_fund_would_you_like_to_transfer)

        }
    }


}