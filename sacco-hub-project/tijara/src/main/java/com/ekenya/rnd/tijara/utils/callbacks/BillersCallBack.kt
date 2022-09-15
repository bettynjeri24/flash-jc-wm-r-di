package com.ekenya.rnd.tijara.utils.callbacks

import com.ekenya.rnd.tijara.network.model.BillerData

interface BillersCallBack {
    fun onItemSelected(item:BillerData)
}