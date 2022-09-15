package com.ekenya.rnd.tijara.ui.homepage.home.callbacks

import com.ekenya.rnd.tijara.network.model.Accounts

interface AccountsCallBack {
    fun onItemSelected(accounts: Accounts,pos:Int)
}