package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.BudgetCreatedBottomsheetBinding
import com.ekenya.rnd.onboarding.databinding.FragmentCategorizeBudgetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class CategorizeBudgetFragment : Fragment() {
    private lateinit var binding: FragmentCategorizeBudgetBinding

    private var amount: Int = 0
    private var progress: Double = 0.00

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategorizeBudgetBinding.inflate(inflater, container, false)
        initUI()
        initTextWatcher()
        initClickListeners()

        return binding.root
    }

    private fun initClickListeners() {
        binding.btnContinue.setOnClickListener {
            showSuccessDialog()

        }
    }

    private fun initTextWatcher() {

        binding.etAmount.setOnKeyListener { v, keyCode, event -> // You can identify which key pressed buy checking keyCode value
            // with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL) {

            }
            false
        }
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s != null) {
                    if (s.isNotBlank()) {
                        amount = 0

                        amount += s.toString().toInt()
                        val budgetAmount = SharedPreferencesManager.getAmouttoTopUP(requireContext())!!.toInt()

                         progress = //(amount.toDouble().div(20000))*100
                             ((amount/budgetAmount)*100).toDouble()
                            toastMessage("amount is $amount budget is ${SharedPreferencesManager.getAmouttoTopUP(requireContext())!!.toInt()} and progress is $progress")

                        initUI()
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                if (binding.etAmount.text.length == 1) {
                    amount = 0
                }
                initUI()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun initUI() {
        val budgetAmount = SharedPreferencesManager.getAmouttoTopUP(requireContext())
        binding.tvRatioAmount.text =
            "GHS $amount.00 /$budgetAmount.00"

        //toastMessage("progress is $progress amount is $amount and $budgetAmount ")
        /*if (progress > 0) {
            binding.progressbar.progress = 30
        }*/
        binding.progressbar.progress = progress.toInt()

    }


    fun showSuccessDialog() {

        val bottomsheetlayoutBinding = BudgetCreatedBottomsheetBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        dialog.setContentView(bottomsheetlayoutBinding.root)
        dialog.show()


    }


}