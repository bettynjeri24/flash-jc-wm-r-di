package com.ekenya.rnd.tijara.utils.callbacks

import com.ekenya.rnd.tijara.network.model.ActivesLoan

interface SelectLoanCallBack {
    fun onItemSelected(item:ActivesLoan)
}