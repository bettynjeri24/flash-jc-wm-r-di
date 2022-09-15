package com.ekenya.rnd.tijara.ui.homepage.home.callbacks

import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.network.model.ContactModel

interface ContactCallBack {
    fun onItemSelected(cont: ContactModel)
}