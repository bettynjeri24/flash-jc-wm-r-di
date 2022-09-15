package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.dashboard.datadashboard.model.MerchantPayload
import com.ekenya.rnd.dashboard.utils.setSharedElementTransition
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentdashboarditemscanningBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentdashboarditemscanningBinding
    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            findNavController().navigate(R.id.navigation_home)
            Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {

            val merchantPayload = MerchantPayload(qr_code = result.contents)
            //getMerchantDetails(merchantPayload)
            /*Toast.makeText(requireActivity(), "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()*/
        }
    }

/*
    private fun getMerchantDetails(merchantPayload: MerchantPayload) {

        //check on paymentsPayload

        val token = SharedPreferencesManager.getnewToken(requireContext())!!


        mobileWalletViewModel.scanMerchantQRCode(token, MainDataObject(merchantPayload))
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            // binding.avi.makeInvisible()
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {
                                    toastMessage("Session Expired Login to Continue")
                                    findNavController().navigate(R.id.loginFragment2)


                                }
                                "00" -> {

                                    binding.etMerchantid.setText(it.data!!.data.transaction_details.additional_data.bill_number)

                                }
                                "57" -> {

                                    store_label =
                                        it.data!!.data.transaction_details.additional_data.store_label.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.getDefault()
                                            ) else it.toString()
                                        }
                                    confirmSendingMoneyViewModel.setDstvAccountName(store_label)

                                    binding.etMerchantid.setText(it.data!!.data.transaction_details.additional_data.bill_number)

                                }
                                else -> {
                                    toastMessage("Merchant Not Found")
                                }
                            }

                        }
                        Status.ERROR -> {

                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })
    }
*/

    private fun accountLookUpMerchant() {}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentdashboarditemscanningBinding.inflate(inflater, container, false)
        barcodeLauncher.launch(ScanOptions())


        setSharedElementTransition()






        return binding.root
    }


    // Register the launcher and result handler


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()

    }


}