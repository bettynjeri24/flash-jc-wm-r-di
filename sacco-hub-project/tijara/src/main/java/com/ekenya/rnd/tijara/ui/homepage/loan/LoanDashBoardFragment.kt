package com.ekenya.rnd.tijara.ui.homepage.loan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.LoanDashboardItemsAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.SendMoneyItemsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentLoanDashBoardBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.UserProfileList
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class LoanDashBoardFragment : Fragment() {
    private lateinit var loanBinding:FragmentLoanDashBoardBinding
    private lateinit var loanDashBoardItems:LoanDashboardItemsAdapter
    private  var itemLists:ArrayList<BillPaymentMerchant> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loanBinding= FragmentLoanDashBoardBinding.inflate(layoutInflater)
        loanBinding.toolbar.custom_toolbar.custom_toolbar_title.text=getString(R.string.we_keep_loan_simple)
        return loanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loanBinding.apply {
            rvLoanItemsr.layoutManager = GridLayoutManager(requireContext(), 1)
            loanDashBoardItems= LoanDashboardItemsAdapter(LoanDashboardItemsAdapter.OnClickListener {


            },itemLists)
            rvLoanItemsr.adapter=loanDashBoardItems

        }
        addLoanDashboardItems()
    }
    private fun addLoanDashboardItems() {
        itemLists.add(BillPaymentMerchant(R.drawable.l_get_loans,getString(R.string.get_loan), R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.l_repay_loans,getString(R.string.my_loans),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.pending_loans,getString(R.string.loan_balances),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.l_gurantors,getString(R.string.my_guarontors),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.loans_guaranteed,getString(R.string.loans_guaranteed),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant(R.drawable.l_statements,getString(R.string.loan_statements), R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant(R.drawable.l_calc,getString(R.string.loans_calculator), R.drawable.arrow_right))

    }


}