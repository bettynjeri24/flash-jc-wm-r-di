package com.ekenya.lamparam.ui.sendmoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.main.LampMainViewModel
import com.ekenya.lamparam.databinding.FragmentSendConfirmationBinding
import com.ekenya.lamparam.model.ModelSenderReceiverInfo
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_confirmation_screen.btnConfirmTrans
import kotlinx.android.synthetic.main.fragment_receive_money_confirmation.*
import kotlinx.android.synthetic.main.fragment_send_confirmation.*
import kotlinx.android.synthetic.main.fragment_send_money_general.*
import kotlinx.android.synthetic.main.header_layout.*


class SendConfirmation : Fragment() {

    lateinit var navOptions: NavOptions
    lateinit var lampMainViewModel: LampMainViewModel
    lateinit var sendMneyAdapter: SendMoneyInfoAdapter
    private var name = ""
    private var purpose = ""
    private var acct = ""
    private var gross = 0.0
    private lateinit var binding:FragmentSendConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lampMainViewModel = ViewModelProvider((activity as LampMainActivity)).get(LampMainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSendConfirmationBinding.inflate(inflater)

       // ((activity as LampMainActivity).hideActionBar())
        navOptions = UtilityClass().getNavoptions()
        binding.appbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        return binding.root

    }

    private fun initRView(name: String, accNumber: Long, idNumber: Long) {



        val list = ArrayList<ModelSenderReceiverInfo>()
        list.add(ModelSenderReceiverInfo("Sender","Daniel nzuma",111111,719147810))
        list.add(ModelSenderReceiverInfo("Recipient",name,idNumber,accNumber))

        binding.rvSenderInfo.layoutManager = GridLayoutManager(requireContext(), 2)

        sendMneyAdapter = list?.let { SendMoneyInfoAdapter(requireActivity(),list) }!!
        binding.rvSenderInfo.adapter = sendMneyAdapter
        sendMneyAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confBundle = requireArguments()
        tvSendMoney.text ="Send Money via ${confBundle.getString("title")}"
        tvRemittanceSendtitle.text = "Confirm & Send Money"

        val customerName = confBundle.getString("name")!!
        val phoneNo = confBundle.getString("phoneNumber")!!.toLong()
        val idNumber = confBundle.getString("idNumber")!!.toLong()

        initRView(customerName,phoneNo,idNumber)

        ////////////
        //tv_recipe_details.text = ""
        //tv_sendee_details.text = getString(R.string.sender_details_info, "John", "3023032", "0721147223")

        lampMainViewModel.expectedAmount.observe(viewLifecycleOwner) {
            gross = it
            updateFields()
        }

        lampMainViewModel.receiverName.observe(viewLifecycleOwner) {
            name = it
            //updataET();
        }
        lampMainViewModel.receiverPhoneNumber.observe(viewLifecycleOwner) {
            acct = it
            //updataET();
        }
        lampMainViewModel.purpose.observe(viewLifecycleOwner) {
            purpose = it
        }

        btnConfirmTrans.setOnClickListener {
            UtilityClass().confirmTransactionEnd(
                binding.root,
                "Info",
                "Your request has been received and is being processed you will receive an SMS confirmation",
                requireActivity(),
                ::toHomeScreen

            )
        }
    }


    private fun updateFields() {
        tvSendValue.text = "ZK $gross"
        tvSendTotal.text = "ZK ${gross + 40}"
    }

    fun toHomeScreen()
    {
        requireActivity().finish()

    }


}