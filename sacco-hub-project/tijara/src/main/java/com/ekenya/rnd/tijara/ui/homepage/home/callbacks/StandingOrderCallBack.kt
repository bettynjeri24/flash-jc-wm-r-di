package com.ekenya.rnd.tijara.ui.homepage.home.callbacks

import com.ekenya.rnd.tijara.network.model.StandingOrderData

interface StandingOrderCallBack {
    fun onItemSelected(stData:StandingOrderData)
}