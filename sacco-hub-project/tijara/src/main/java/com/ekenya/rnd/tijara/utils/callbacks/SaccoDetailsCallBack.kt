package com.ekenya.rnd.tijara.utils.callbacks

import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity

interface SaccoDetailsCallBack {
    fun onItemSelected(item: SaccoDetailEntity)
}