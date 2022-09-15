package com.ekenya.rnd.cargillbuyer.ui.buyerprofile

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.databinding.FragmentBuyerHomeBinding
import com.ekenya.rnd.cargillbuyer.ui.tabs.BuyerHomeTabsAdapter
import com.ekenya.rnd.common.AVAILABLE_BALANCE
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.TOTAL_MONTHLY_CASHOUT
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonBuyerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.QRCodeGenerator
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.google.android.material.tabs.TabLayoutMediator
import com.synnapps.carouselview.ImageListener
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BuyerHomeFragment :
    BaseCommonBuyerCargillDIFragment<FragmentBuyerHomeBinding>(
        FragmentBuyerHomeBinding::inflate
    ) {
    @Inject
    lateinit var viewModel: BuyerCargillViewModel
    private lateinit var loginResponseData: CargillUserLoginResponseData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        inflateNavigationDrawer()
        getWalletAPiBalance()
        getUserData()
        showBalance()
       // setUpTabs()
        setUpCarouselView()

        binding.clFarmerPayments.setOnClickListener {
            findNavController().navigate(R.id.listOfFarmersFragment)
        }
        binding.clPendingPayments.setOnClickListener {
            findNavController().navigate(R.id.fFPendingPaymentListFragment)
        }

        binding.clFundRequest.setOnClickListener {
            findNavController().navigate(R.id.buyerRequestFundFragment)
        }
        binding.toolbar.ivProfile.setOnClickListener {
            val navDrawer: DrawerLayout = binding.root
            // If the navigation drawer is not open then open it, if its already open then close it.
            if (!navDrawer.isDrawerOpen(Gravity.START)) {
                navDrawer.openDrawer(Gravity.START)
            } else navDrawer.closeDrawer(
                Gravity.END
            )
        }
    }

    private fun getUserData() {
        viewModel.getUserVmRoom().observe(
            viewLifecycleOwner,
            Observer {
                loginResponseData = it
                if (it.firstName == null) {
                    val user = it?.firstName
                    binding.tvGreet.text =
                        "${resources.getString(com.ekenya.rnd.common.R.string.dashboard_greetings)} $user"
                } else {
                    binding.tvGreet.text =
                        "${resources.getString(com.ekenya.rnd.common.R.string.dashboard_greetings)} ${it.firstName}"
                    Timber.e(" transactions ${it.transactions}")
                }
            }
        )
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

                    binding.tvAvailableBal.text = "${response.data} CFA"
                    binding.tvTotalMonthlyCashOut.text = "${response.data} CFA"

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
                Timber.e(e.message.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                Timber.e(e.message.toString())
                dismissCustomDialog()
            } catch (e: Exception) {
                Timber.e(e.message.toString())
                dismissCustomDialog()
            }
        }
    }

    private fun showBalance() {
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

    private fun setUpTabs() {
        val fragmentAdapter = BuyerHomeTabsAdapter(childFragmentManager, lifecycle, context)
        binding.viewpagerMain.adapter = fragmentAdapter
        TabLayoutMediator(binding.tabsMain, binding.viewpagerMain) { tab, position ->
            tab.text = fragmentAdapter.getPageTitle(position)
        }.attach()
    }

    private fun setUpCarouselView() {
        var images = intArrayOf(
            com.ekenya.rnd.common.R.mipmap.cocoaimg,
            com.ekenya.rnd.common.R.drawable.my_account_wallet,
            com.ekenya.rnd.common.R.mipmap.cocoaimg,
            com.ekenya.rnd.common.R.drawable.my_account_wallet
        )
        binding.carouselView.setImageListener(
            ImageListener { position: Int, imageView: ImageView ->
                imageView.setImageResource(
                    images[position]
                )
            }
        )

        binding.carouselView.pageCount = images.size
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
                //
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
                findNavController().popBackStack(R.id.buyerHomeFragment, true)
                // findNavController().navigate(R.id.loginAccountFragment)
            }
        } catch (ex: Exception) {
        }
    }
}
