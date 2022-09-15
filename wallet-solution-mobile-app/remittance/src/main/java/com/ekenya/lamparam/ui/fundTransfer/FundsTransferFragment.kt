package com.ekenya.lamparam.ui.fundTransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.databinding.FragmentFundsTransferBinding
import kotlinx.android.synthetic.main.fragment_cash_withdrawal.*
import kotlinx.android.synthetic.main.header_layout.*

/**
 * A simple [Fragment] subclass.
 * Use the [FundsTransferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FundsTransferFragment : Fragment() {

    private var _binding: FragmentFundsTransferBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFundsTransferBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // ((activity as LampMainActivity).hideActionBar())

        tvActionTitle.text = getString(R.string.funds_transfer)
        //btn back
        ((activity as LampMainActivity).onBackClick(btn_back, view))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var banks = listOf("BAO", "BDU", "Ecobank", "Orabank", "Atlantique").toMutableList()
        var microfinances = listOf("Bambaram", "Mana MPC").toMutableList()
        mAdapter = ArrayAdapter<String>(requireContext(), R.layout.text_item, banks)

        binding.etBankName.setAdapter(mAdapter)
        binding.etBankName.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.etBankName.showDropDown()
            false
        })

        binding.btnTransfer.setOnClickListener { findNavController().navigate(R.id.nav_confirmPin) }

        binding.rgTransferOption.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_lamparam -> {
                    binding.cvMyAccounts.visibility = View.GONE
                    binding.tvSendTo.text = getString(R.string.transfer_money_note)
                    binding.rbBank.isChecked = true
                }
                R.id.rb_my_account -> {
                    binding.cvMyAccounts.visibility = View.VISIBLE
                    binding.clMyAccounts.visibility = View.VISIBLE
                    binding.clOtherAccounts.visibility = View.GONE
                    binding.tvSendTo.text = getString(R.string.transfer_money_note1)
                    binding.rbBank.isChecked = true
                }
                R.id.rb_other_account -> {
                    binding.cvMyAccounts.visibility = View.VISIBLE
                    binding.clMyAccounts.visibility = View.GONE
                    binding.clOtherAccounts.visibility = View.VISIBLE
                    binding.tvSendTo.text = getString(R.string.transfer_money_note2)
                    binding.rbBank.isChecked = true
                }
                else -> {
                    binding.cvMyAccounts.visibility = View.GONE
                    binding.rbBank.isChecked = true
                }
            }
        }

        binding.rgBankType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_bank) {
//                binding.etBankName.setText("")
                binding.tvBankName.text = getString(R.string.select_bank)
                banks = listOf("BAO", "BDU", "Ecobank", "Orabank", "Atlantique").toMutableList()
                updateData(banks)
            } else {
//                binding.etBankName.setText("")
                microfinances = listOf("Bambaram", "Mana MPC").toMutableList()
                updateData(microfinances)
                binding.tvBankName.text = getString(R.string.select_microfinance_institution)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateData(itemsArrayList: List<String>) {
        mAdapter.clear()
        for (`object` in itemsArrayList) {
            mAdapter.insert(`object`, mAdapter.count)
        }
        mAdapter.notifyDataSetChanged()
    }
}