package com.ekenya.rnd.tmd.ui.fragments.confirmation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.baseapp.di.injectables.ViewModelFactory
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentConfirmBinding
import com.ekenya.rnd.tmd.data.network.CBKApi
import com.ekenya.rnd.tmd.data.network.request.BidRequestRequest
import com.ekenya.rnd.tmd.data.network.response.ContentItem
import com.ekenya.rnd.tmd.utils.getAccessToken
import com.ekenya.rnd.tmd.utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfirmFragment : BaseDaggerFragment() {

    @Inject
    lateinit var cbkApi: CBKApi

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewmodel by lazy {
        ViewModelProvider(this, viewModelFactory)[ConfirmViewModel::class.java]
    }

    private lateinit var binding: FragmentConfirmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentConfirmBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        val contentItem = arguments?.getParcelable<ContentItem>("item")
        val amount = arguments?.getString("amount")
        val rate = arguments?.getString("rate")

        val list = mutableListOf<ConfirmItem>().apply {
            add(ConfirmItem("Issue", contentItem?.bondReferenceNumber!!))
            add(ConfirmItem("Amount", amount!!))
            add(ConfirmItem("Rate", rate!!))
        }

        binding.recyclerView.apply {
            val adapterCon = ConfirmListAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterCon
            adapterCon.submitList(list)
        }

        binding.apply {
            button7.setOnClickListener {
                lifecycleScope.launch {
                    try {
                        showHideProgress("Submitting Details")
                        delay(3000)
                        val response = cbkApi.bidRequest(
                            "Bearer " + sharedPreferences.getAccessToken()!!,
                            BidRequestRequest(
                                providedRate = contentItem?.interestRate,
                                biddingRate = rate?.toDouble(),
                                biddingAmount = amount?.toInt(),
                                bidId = contentItem?.id
                            )
                        )
                        if (response.status == 200) {
                            findNavController().navigate(R.id.action_confirmFragment_to_homeFragment)
                        }
                        showHideProgress(null)
                    } catch (e: Exception) {
                        toast(e.localizedMessage)
                        showHideProgress(null)
                    }
                }
            }
        }
    }
}
