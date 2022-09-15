package com.ekenya.rnd.tijara.ui.homepage.home.callbacks

import com.ekenya.rnd.tijara.network.model.LoanProduct

interface LoanProductCallBack {
    fun onItemSelected(loanProduct:LoanProduct)
}