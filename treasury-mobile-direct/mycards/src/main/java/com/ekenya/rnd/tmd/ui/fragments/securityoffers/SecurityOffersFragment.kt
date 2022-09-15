package com.ekenya.rnd.tmd.ui.fragments.securityoffers

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentSecurityOffersBinding
import com.ekenya.rnd.tmd.data.network.CBKApi
import com.ekenya.rnd.tmd.data.network.request.BondsRequest
import com.ekenya.rnd.tmd.utils.getAccessToken
import com.ekenya.rnd.tmd.utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SecurityOffersFragment : BaseDaggerFragment() {

    @Inject
    lateinit var cbkApi: CBKApi

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: FragmentSecurityOffersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentSecurityOffersBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            val rcadapter = SecurityOffersAdapter {
                findNavController().navigate(R.id.action_securityOffersFragment_to_securityBidFragment, bundleOf("item" to it))
            }

            recyclerView.apply {
                adapter = rcadapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            lifecycleScope.launch {
                showHideProgress("Loading Offers")
                delay(3000)
                try {
                    val response = cbkApi.getbondsAndBills(
                        "Bearer " + sharedPreferences.getAccessToken()!!,
                        BondsRequest(
                            size = 100,
                            page = 0
                        )
                    )
                    val type = arguments?.getString("type")
                    val bondsList = response.data?.content?.filter {
                        true
                    }
                    rcadapter.submitList(bondsList)
                    showHideProgress(null)
                } catch (e: Exception) {
                    toast(e.message!!)
                    Log.e(TAG,e.localizedMessage)
                    showHideProgress(null)
                }
            }
        }
    }
    private  val TAG = "SecurityOffersFragment"
}
