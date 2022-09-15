package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentCreateBudgetBinding

class CreateBudgetFragment : Fragment() {
    private lateinit var binding: FragmentCreateBudgetBinding
    private var amount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateBudgetBinding.inflate(inflater, container, false)
        initUI()
        setOnClickListeners()
        return binding.root


    }

    private fun setOnClickListeners() {
        binding.btnContinue.setOnClickListener {
            if (validDetails()) {
                findNavController().navigate(R.id.budgetAccountFragment)
            }
        }
    }

    private fun initUI() {
        val items = listOf("Daily", "Weekly", "Monthly")

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.textField2 as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    private fun validDetails(): Boolean {
        val etamount = binding.etAmount.text.toString().trim()
        if (etamount.isBlank()) {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()
            return false
        } else {
            amount = Integer.parseInt(etamount)
           // confirmSendingMoneyViewModel.setAmount(amount)
            SharedPreferencesManager.setAmount(requireContext(), amount.toString())
        }

        return true
    }


}