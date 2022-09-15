package com.ekenya.rnd.cargillcoop.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillcoop.R
import com.ekenya.rnd.cargillcoop.databinding.FragmentCoopHomeBinding
import com.ekenya.rnd.cargillcoop.ui.recenttransaction.OnCoopTransactionItemListener
import com.ekenya.rnd.cargillcoop.ui.recenttransaction.RecentTransactionAdapter
import com.ekenya.rnd.common.AVAILABLE_BALANCE
import com.ekenya.rnd.common.TOTAL_MONTHLY_CASHOUT
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.db.entity.CargillUserTransactionData
import com.ekenya.rnd.common.utils.base.BaseCommonCargillCoopFragment
import com.ekenya.rnd.common.utils.custom.QRCodeGenerator
import com.synnapps.carouselview.ImageListener
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CoopMainFragment :
    BaseCommonCargillCoopFragment<FragmentCoopHomeBinding>(
        FragmentCoopHomeBinding::inflate
    ) {
    private lateinit var loginResponseData: CargillUserLoginResponseData
    private lateinit var recentTransactionAdapter: RecentTransactionAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
    }

    private fun setUpUi() {
        setUpCarouselView()

        binding.clEvalueProvisions.setOnClickListener {
            findNavController().navigate(R.id.action_coopMainFragment_to_evalueRequestListFragment)
        }

        binding.clBuyerLists.setOnClickListener {
            findNavController().navigate(R.id.action_coopMainFragment_to_coopBuyerDetailsListFragment)
        }

        binding.clFundRequest.setOnClickListener {
            findNavController().navigate(R.id.action_coopMainFragment_to_fundRequestsByBuyerFragment)
        }
        binding.tvAvailableBal.text =
            "$AVAILABLE_BALANCE ${getString(com.ekenya.rnd.common.R.string.currency)}"
        binding.tvTotalMonthlyCashOut.text =
            "$TOTAL_MONTHLY_CASHOUT ${getString(com.ekenya.rnd.common.R.string.currency)}"
        binding.tvViewAll.setOnClickListener {
            findNavController().navigate(R.id.action_coopMainFragment_to_recentTransactionFragment)
        }

        binding.tvGreet.text =
            getString(com.ekenya.rnd.common.R.string.dashboard_greetings)
    }

    private fun inflateRecentTransaction(itemsRV: List<CargillUserTransactionData>) {
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvTransactins.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.tvErrorResponse.text = itemsRV.toString()
            recentTransactionAdapter =
                RecentTransactionAdapter(itemsRV, onCoopTransactionItemListener)
            binding.rvTransactins.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = recentTransactionAdapter
                setHasFixedSize(true)
            }
            recentTransactionAdapter.notifyDataSetChanged()
        }
    }

    private val onCoopTransactionItemListener = object : OnCoopTransactionItemListener {
        override fun onItemClicked(view: View, model: CargillUserTransactionData) {
            when (view.id) {
                else -> {
                    findNavController().navigate(R.id.action_coopMainFragment_to_recentTransactionFragment)
                }
            }
        }
    }

    private fun inflateNavigationDrawer() {
        try {
            if (loginResponseData != null) {
                val userData = loginResponseData
                val phoneNumber = userData.phoneNumber
                val coopId = userData.cooperativeId
                val userId = userData.userId
                val userIndex = "${userData.userIndex}"
                val name = "${userData.firstName} ${userData.lastName}"
                val stringToGenerate = "$userId~$userIndex~$phoneNumber~$coopId~$name"
                val qrBitmap = QRCodeGenerator.generateQr(stringToGenerate)
                binding.profileQRCode.setImageBitmap(qrBitmap)
                binding.tvName.text = "${userData.firstName} ${userData.lastName}"
                binding.tvPrivacynsecurity.text =
                    this.resources.getString(com.ekenya.rnd.common.R.string.privacy_n_security)
                binding.tvfarmNoTitle.text =
                    this.resources.getString(com.ekenya.rnd.common.R.string.farmer_id)
                binding.tvScanTitle.text =
                    this.resources.getString(com.ekenya.rnd.common.R.string.scan_qr_code)
                binding.tvAccPhoneNumber.text = userData.phoneNumber
                binding.tvCompanyId.text = userData.providedUserId
                var section = userData.section
                binding.tvSectionName.text = section!!.sectionName
            }
            binding.btnLogout.setOnClickListener {
                // findNavController().popBackStack(R.id.farmerHomeFragment, true)
                // findNavController().navigate(R.id.loginAccountFragment)
            }
        } catch (ex: Exception) {
        }
    }

    private var images = intArrayOf(
        com.ekenya.rnd.common.R.mipmap.cocoaimg,
        com.ekenya.rnd.common.R.drawable.my_account_wallet,
        com.ekenya.rnd.common.R.mipmap.cocoaimg,
        com.ekenya.rnd.common.R.drawable.my_account_wallet
    )

    private fun setUpCarouselView() {
        binding.carouselView.setImageListener(
            ImageListener { position: Int, imageView: ImageView ->
                imageView.setImageResource(
                    images[position]
                )
            }
        )

        binding.carouselView.pageCount = images.size
    }
}
