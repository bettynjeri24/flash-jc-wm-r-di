package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.dashboard

import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.cargillfarmer.R
import com.ekenya.rnd.cargillfarmer.data.responses.FarmerLatestTransactionData
import com.ekenya.rnd.cargillfarmer.databinding.FarmerHomeFragmentBinding
import com.ekenya.rnd.cargillfarmer.ui.adapters.FarmerTransactionAdapter
import com.ekenya.rnd.cargillfarmer.ui.adapters.OnFarmerTransactionItemListener
import com.ekenya.rnd.common.*
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.db.entity.CargillUserTransactionData
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonFarmerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.QRCodeGenerator
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.maskString
import com.synnapps.carouselview.ImageListener
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class FarmerHomeFragment :
    BaseCommonFarmerCargillDIFragment<FarmerHomeFragmentBinding>(FarmerHomeFragmentBinding::inflate),
    OnFarmerTransactionItemListener {

    private var showTransaction = false

    @Inject
    lateinit var viewModel: FarmerViewModel

    private lateinit var loginResponseData: CargillUserLoginResponseData
    private val LANGUAGE = "fr" // fr
    private val COUNTRY = "CI" // CI
    private lateinit var farmerTransactionAdapter: FarmerTransactionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showTransaction = true
        animate(binding.root, binding)
        // setUpUi()
    }

    private fun setUpUi() {
        inflateNavigationDrawer()
        setUpCarouselView()
        //
        getWalletAPiBalance()
        getUserData()
        showBalance()

        // Set OnClick Listener
        binding.clTransferMoney.setOnClickListener {
            findNavController().navigate(R.id.sendMoneyAccountListFragment) // sendMoneyMenu
        }
        binding.clMyAccount.setOnClickListener {
            findNavController().navigate(R.id.farmerLinkedAccountFragment)
        }
        binding.clStatements.setOnClickListener {
            findNavController().navigate(R.id.farmerMiniStatementCargillFragment)
        }

        binding.toolbar.ivProfile.setOnClickListener {
            val navDrawer: DrawerLayout = binding.root
            // If the navigation drawer is not open then open it, if its already open then close it.
            if (!navDrawer.isDrawerOpen(GravityCompat.START)) {
                navDrawer.openDrawer(GravityCompat.START)
            } else navDrawer.closeDrawer(
                GravityCompat.END
            )
        }

        binding.btnViewBalance.setOnClickListener {
            binding.tvAvailableBal.text = "$AVAILABLE_BALANCE CFA"
            binding.tvTotalMonthlyCashOut.text = "$TOTAL_MONTHLY_CASHOUT CFA"

            /* if (binding.btnViewBalance.text.toString() == getString(com.ekenya.rnd.common.R.string.hide_balance)) {
                 binding.btnViewBalance.text = getString(com.ekenya.rnd.common.R.string.show_balance)

                 binding.tvAvailableBal.text = "${maskString(AVAILABLE_BALANCE)} CFA"
                 binding.tvTotalMonthlyCashOut.text = "${maskString(TOTAL_MONTHLY_CASHOUT)} CFA"


             } else if (binding.btnViewBalance.text.toString() == getString(com.ekenya.rnd.common.R.string.show_balance)) {
                 binding.btnViewBalance.text = getString(com.ekenya.rnd.common.R.string.hide_balance)

                 binding.tvAvailableBal.text =
                     "${UtilPreferenceCommon().getCashBalance(requireActivity())} CFA"
                 binding.tvTotalMonthlyCashOut.text =
                     "${UtilPreferenceCommon().getCashBalance(requireActivity())} CFA"

             }*/
        }

        binding.btnMiniStatement.setOnClickListener {
            findNavController().navigate(R.id.farmerMiniStatementCargillFragment)
        }

        binding.tvViewAll.setOnClickListener {
            findNavController().navigate(R.id.moreRecentTransaction)
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
                findNavController().popBackStack(R.id.farmerHomeFragment, true)
                // findNavController().navigate(R.id.loginAccountFragment)
            }
        } catch (ex: Exception) {
        }
    }

    private fun getUserData() {
        viewModel.getUserVmRoom().observe(
            viewLifecycleOwner
        ) {
            loginResponseData = it
            if (it.firstName == null) {
                val user = it?.firstName
                binding.tvGreet.text =
                    "${resources.getString(com.ekenya.rnd.common.R.string.dashboard_greetings)} $user"
            } else {
                binding.tvGreet.text =
                    "${resources.getString(com.ekenya.rnd.common.R.string.dashboard_greetings)} ${it.firstName}"
                Timber.e(" transactions ${it.transactions}")
                inflateRecentTransaction(it.transactions!!)
            }
        }
    }

    private fun inflateRecentTransaction(itemsRV: List<CargillUserTransactionData>) {
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvTransactins.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.tvErrorResponse.text = itemsRV.toString()
            farmerTransactionAdapter =
                FarmerTransactionAdapter(itemsRV, this)
            binding.rvTransactins.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = farmerTransactionAdapter
                setHasFixedSize(true)
            }
            farmerTransactionAdapter.notifyDataSetChanged()
        }
    }

    /**
     * BalanceInquiry
     */
    private fun getWalletAPiBalance() {
        val bIRequestJson = JSONObject()
        bIRequestJson.put("phonenumber", CURRENT_USER_PHONENUMBER)
        lifecycleScope.launch {
            try {
                val response = viewModel.requestFarmerBalanceInquiryResponse(
                    bIRequestJson.toString().toRequestBody(MEDIA_TYPE_JSON)
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    Timber.e("RESPONSE: ${response.data}")

                    AVAILABLE_BALANCE = response.data.toString()
                    TOTAL_MONTHLY_CASHOUT = response.data.toString()

                    UtilPreferenceCommon().saveCashBalance(
                        requireActivity(),
                        response.data.toString()
                    )
                } else {
                    dismissCustomDialog()
                    // snackBarCustom(it.value.statusDescription)
                    Timber.e("******************registered==1** ${response.statusDescription}")
                }
            } catch (e: ApiExceptions) {
                toast(e.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                toast(e.toString())
                dismissCustomDialog()
            }
        }
    }

    private fun showBalance() {
//         AVAILABLE_BALANCE =
//             NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY))
//                 .format(
//                     UtillPreference().getCashBalance(
//                         requireActivity()
//                     ).toInt()
//                 )
//         TOTAL_MONTHLY_CASHOUT =
//             NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY))
//                 .format(
//                     UtillPreference().getCashBalance(
//                         requireActivity()
//                     ).toInt()
//                 )

        if (binding.btnViewBalance.text.toString() == getString(com.ekenya.rnd.common.R.string.hide_balance)) {
            binding.btnViewBalance.text = getString(com.ekenya.rnd.common.R.string.show_balance)

            binding.tvAvailableBal.text = "${maskString(AVAILABLE_BALANCE)} CFA"
            binding.tvTotalMonthlyCashOut.text = "${maskString(TOTAL_MONTHLY_CASHOUT)} CFA"
        } else if (binding.btnViewBalance.text.toString() == getString(com.ekenya.rnd.common.R.string.show_balance)) {
            binding.btnViewBalance.text = getString(com.ekenya.rnd.common.R.string.hide_balance)

            binding.tvAvailableBal.text =
                "${UtilPreferenceCommon().getCashBalance(requireActivity())} CFA"
            binding.tvTotalMonthlyCashOut.text =
                "${UtilPreferenceCommon().getCashBalance(requireActivity())} CFA"
        }
    }
    /**
     * LatestTransactionsResponse
     */

    /**
     * LatestTransactionsResponse
     */
    private fun getFetchAPiRecentTransaction() {
        val requestLatestTransactions = JSONObject()
        requestLatestTransactions.put("phonenumber", CURRENT_USER_PHONENUMBER)
        requestLatestTransactions.put("userId", CARGILL_USER_ID)

        lifecycleScope.launch {
            try {
                val response = viewModel.requestLatestTransactionsResponse(
                    requestLatestTransactions.toString().toRequestBody(MEDIA_TYPE_JSON)
                )
                if (response.statusCode == 0) {
                    Timber.e(
                        "LatestTransactionsResponse: ${
                        response.farmerLatestTransactionData
                        }"
                    )
                } else {
                    Timber.e(
                        "******************registered==1** ${
                        response.statusDescription
                        }"
                    )
                }
            } catch (e: ApiExceptions) {
                toast(e.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                toast(e.toString())
                dismissCustomDialog()
            }
        }
    }

    private fun animate(parent: ViewGroup, binding: FarmerHomeFragmentBinding) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 1500
        transition.addTarget(binding.clTransaction)

        TransitionManager.beginDelayedTransition(parent, transition)
        binding.clTransaction.visibility = if (showTransaction) View.VISIBLE else View.GONE
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

    override fun onItemClicked(view: View, model: CargillUserTransactionData) {
        when (view.id) {
            else -> {
                findNavController().navigate(R.id.moreRecentTransaction)
                // Timber.e("FarmerHomeFragment")
            }
        }
    }
}

fun FarmerLatestTransactionData.parcelizeToUserTransactionData(): CargillUserTransactionData {
    return CargillUserTransactionData(
        id = id,
        datecreated = datecreated,
        walletTransactionType = walletTransactionType.toString(),
        sendorPhoneNumber = sendorPhoneNumber,
        recipientPhoneNumber = recipientPhoneNumber,
        transMode = transactiontype.toString(),
        status = status,
        reasons = reasons,
        amount = amount,
        recipientName = recipientName,
        sendorName = sendorName,
        transactionCode = transactionCode
    )
}
