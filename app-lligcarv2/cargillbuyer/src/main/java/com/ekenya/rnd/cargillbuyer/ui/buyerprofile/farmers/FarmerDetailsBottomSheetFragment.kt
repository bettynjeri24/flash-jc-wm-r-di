package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.ekenya.rnd.cargillbuyer.databinding.FragmentFarmerDetailsBottomSheetBinding
import com.ekenya.rnd.common.utils.base.BaseCommonCargillBottomSheetDialogFragment
import com.ekenya.rnd.common.utils.custom.setTransparentBackground

class FarmerDetailsBottomSheetFragment :
    BaseCommonCargillBottomSheetDialogFragment<FragmentFarmerDetailsBottomSheetBinding>(
        FragmentFarmerDetailsBottomSheetBinding::inflate
    ) {

    private val args: FarmerDetailsBottomSheetFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentBackground()
        handleDetails()
    }

    private fun handleDetails() {
        val farmerDetailsData = args.farmerDetailsData // FarmerDetailsRes.getFarmerData()
        binding.tvBuyer.text =
            getString(
                com.ekenya.rnd.common.R.string.buyer,
                "${farmerDetailsData.firstName} ${farmerDetailsData.lastName}"
            )
        binding.tvLocationValue.text = farmerDetailsData.address
        binding.tvCertificateNumber.text = "${farmerDetailsData.certificationnumber} "
        binding.tvPaymentDate.text = "${farmerDetailsData.datecreated} "
        binding.tvTotalWeightCollected.text = farmerDetailsData.datecreated
        binding.tvMostRecentPayment.text = farmerDetailsData.datecreated
        binding.tvDateJoinedValue.text = farmerDetailsData.datecreated
        binding.tvEmailAddressdata.text = farmerDetailsData.emailAddress

        binding.rating.rating = 3.toFloat()

        when (binding.rating.rating.toInt()) {
            1 ->
                binding.tvQualityValue.text =
                    getString(com.ekenya.rnd.common.R.string.very_poor)
            2 -> binding.tvQualityValue.text = getString(com.ekenya.rnd.common.R.string.poor)
            3 -> binding.tvQualityValue.text = getString(com.ekenya.rnd.common.R.string.good)
            4 ->
                binding.tvQualityValue.text =
                    getString(com.ekenya.rnd.common.R.string.very_good)
            5 ->
                binding.tvQualityValue.text =
                    getString(com.ekenya.rnd.common.R.string.excellent)
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.ivClose.setOnClickListener { dismiss() }
    }
}
