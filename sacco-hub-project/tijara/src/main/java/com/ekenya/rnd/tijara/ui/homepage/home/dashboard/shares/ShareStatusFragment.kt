package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.RequestSentAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.RequestReceivedAdapter
import com.ekenya.rnd.tijara.databinding.ShareOptionBottomDialogBinding
import com.ekenya.rnd.tijara.databinding.ShareStatusFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ShareStatusFragment : Fragment() {
    private lateinit var binding:ShareStatusFragmentBinding
    private lateinit var shareOptionBinding:ShareOptionBottomDialogBinding
    private lateinit var viewModel: ShareStatusViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShareStatusViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.buttonColor)
        binding= ShareStatusFragmentBinding.inflate(layoutInflater)
        setupNavUp()
        binding.shareViewmodel=viewModel
        binding.lifecycleOwner=this
        hideShowLayouts()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressSpinkit.visibility=View.VISIBLE
        binding.apply {
            viewModel.shareBalance.observe(viewLifecycleOwner, Observer {
                tvTotalShare.text=it.numberOfShares.toString()
                tvKESVALUE.text=it.currency +"  "+it.totalValue

            })
            val shareAdapter= RequestReceivedAdapter(RequestReceivedAdapter.OnClickListener {
            })
            binding.rvShareHistory.adapter=shareAdapter
            binding.rvShareHistory.layoutManager = GridLayoutManager(activity, 1)
            fabFullBtn.setOnClickListener {
                showShareOptionBottomSheetDialog()
            }
            addFab.setOnClickListener {
                showShareOptionBottomSheetDialog()
            }
            val shareSentAdapter= RequestSentAdapter(RequestSentAdapter.OnClickListener {
            })
            binding.rvShareSent.adapter=shareSentAdapter
            binding.rvShareSent.layoutManager = GridLayoutManager(activity, 1)
        }

        viewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null!=it){
                binding.progressSpinkit.visibility=View.GONE
                when(it){
                    1->{
                        binding.progressSpinkit.visibility=View.GONE
                    }
                    0->{
                        binding.progressSpinkit.visibility=View.GONE
                    }
                    else->{
                        binding.progressSpinkit.visibility=View.GONE
                    }

                }
            }
        })
    }
    private fun hideShowLayouts() {
        binding.requestReceived.setOnClickListener {
           /* viewModel.shareProperties.observe(viewLifecycleOwner, Observer {
                if (it!!.isNotEmpty()){
                    binding.tvRequest.text="Requests Received"
                }else{
                    binding.tvRequest.text="Requests "
                }
            })*/

           // binding.makeRequestsLayout.visibility = View.GONE
          //  binding.myRequestsLayout.visibility = View.VISIBLE
            binding.BtnAdd.visibility=View.GONE
            binding.ClRequestReceived.visibility=View.VISIBLE
            binding.ClRequestSent.visibility=View.GONE
            binding.requestReceived.setBackgroundColor(resources.getColor(R.color.blue_light))
            binding.requestSent.setBackgroundColor(resources.getColor(R.color.colorAccent))
            binding.requestSent.setTextColor(resources.getColor(R.color.hintColor))
            binding.requestReceived.setTextColor(resources.getColor(R.color.black))
        }
        binding.requestSent.setOnClickListener {
           /* viewModel.shareSentProperties.observe(viewLifecycleOwner, Observer {
                if (it!!.isNotEmpty()){
                    binding.tvRequest.text="Requests Sent"
                }else{
                    binding.tvRequest.text="Requests "
                }
            })*/
          //  binding.myRequestsLayout.visibility = View.GONE
          //  binding.makeRequestsLayout.visibility = View.VISIBLE
            binding.BtnAdd.visibility=View.GONE
           binding.requestReceived.setBackgroundColor(resources.getColor(R.color.colorAccent))
            binding.ClRequestReceived.visibility=View.GONE
            binding.ClRequestSent.visibility=View.VISIBLE
            binding.requestSent.setBackgroundColor(resources.getColor(R.color.blue_light))
            binding.requestSent.setTextColor(resources.getColor(R.color.black))
            binding.requestReceived.setTextColor(resources.getColor(R.color.hintColor))
        }
    }
    private fun showShareOptionBottomSheetDialog() {
        shareOptionBinding = ShareOptionBottomDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(),R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(shareOptionBinding.root)
        shareOptionBinding.CLSellShares.setOnClickListener {
            dialog.dismiss()
            Constants.DIALOGSELETED=1
            findNavController().navigate(R.id.sharePhoneLookupFragment)

        }
        shareOptionBinding.CLTransferShares.setOnClickListener {
            Constants.DIALOGSELETED=2
            dialog.dismiss()
            findNavController().navigate(R.id.sharePhoneLookupFragment)


        }

        dialog.show()


    }


    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.my_shares)
    }


}