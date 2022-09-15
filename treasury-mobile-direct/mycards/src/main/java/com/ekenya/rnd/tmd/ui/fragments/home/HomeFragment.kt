package com.ekenya.rnd.tmd.ui.fragments.home

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentHomeBinding
import com.ekenya.rnd.tmd.data.network.CBKApi
import com.ekenya.rnd.tmd.data.network.request.TransactionsRequest
import com.ekenya.rnd.tmd.utils.getAccessToken
import com.ekenya.rnd.tmd.utils.toast
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeFragment : BaseDaggerFragment() {

    private val seeBalance = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var cbkApi: CBKApi

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    private fun setUpResultListener() {
        setFragmentResultListener("requestKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            seeBalance.value = bundle.getBoolean("result")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpResultListener()
        setUpUI()
        val adapterrv = TransactionAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterrv
        }
        lifecycleScope.launchWhenCreated {
            showHideProgress("Fetching Details")
            delay(3000)
            try {
                val response = cbkApi.getTransActions(
                    "Bearer " + sharedPreferences.getAccessToken(),
                    TransactionsRequest(
                        page = 0,
                        size = 10
                    )
                )

                val list = response.data?.content
                adapterrv.submitList(list)
                showHideProgress(null)
                binding.parent.isVisible = true
            } catch (e: Exception) {
                toast(e.localizedMessage)
                Log.e(TAG, "onViewCreated: ")
                showHideProgress(null)
            }
        }
    }

    private fun setUpUI() {
        binding.apply {
            seeBalance.observe(viewLifecycleOwner) {
                if (it) {
                    textView33.text = "Ksh 108,099.00"
                    textView38.text = "Ksh 67,809.00"
                    textView44.text = "Ksh 40,290.00"
                } else {
                    textView33.text = "Ksh ********"
                    textView38.text = "Ksh ********"
                    textView44.text = "Ksh ********"
                }
            }
            buttonMiniStatemen.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_miniStatementFragment)
            }
            textView33.setOnClickListener {
                if (seeBalance.value!!) {
                    textView33.text = "Ksh ********"
                    textView38.text = "Ksh ********"
                    textView44.text = "Ksh ********"
                } else {
                    findNavController().navigate(
                        R.id.authFragment2,
                        Bundle().apply {
                            putString("intent", "auth")
                        }
                    )
                }
            }

            imageViewProfile.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment2)
            }
            imageView9.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_notificationsFragment)
            }
            materialCardViewAuctionBid.setOnClickListener {
                findNavController().navigate(
                    R.id.action_homeFragment_to_auctionBidFragment,
                    Bundle().apply {
                        putString("title", "Auction Bid")
                    }
                )
            }

            materialCardView8.setOnClickListener {
                findNavController().navigate(
                    R.id.action_homeFragment_to_auctionBidFragment,
                    Bundle().apply {
                        putString("title", "Rollover Bid")
                    }
                )
            }
        }
    }

    private val TAG = "HomeFragment"
}
