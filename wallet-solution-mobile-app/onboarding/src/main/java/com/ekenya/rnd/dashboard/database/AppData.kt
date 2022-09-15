package com.ekenya.rnd.dashboard.database

import android.content.Context
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.datadashboard.model.DashboardItem
import com.ekenya.rnd.dashboard.datadashboard.model.TransactionItem
import com.ekenya.rnd.onboarding.R


object AppData {


    fun getBudgetCategories(context:Context): List<DashboardItem> {

        val itemlist= ArrayList<DashboardItem>()
        itemlist.add(DashboardItem(title = "All"))
        itemlist.add(DashboardItem(title = "Bills & Airtime "))
        itemlist.add(DashboardItem(title = "Send Money & Remittance"))
        itemlist.add(DashboardItem(title = "Delivery & Cash Withdrawal"))
        return itemlist



    }

    fun getBudgetBreadownData(context:Context): List<DashboardItem> {

        val itemlist= ArrayList<DashboardItem>()
        itemlist.add(DashboardItem(title = "Bills & Airtime",amount = "GHS 152.10",icon= 1))
        itemlist.add(DashboardItem(title = "Send money & Remittance",amount = "GHS 12,052.10",icon= 30))
       // itemlist.add(DashboardItem(title = "Delivery & Cash withdrawals",amount = "GHS 4,772.00",icon= 69))

        return itemlist



    }
    fun getDashBoardItems(context:Context): List<DashboardItem> {

        val itemlist= ArrayList<DashboardItem>()
        itemlist.add(DashboardItem(R.color.bg_orange,
            R.drawable.ic_mobilewallet,
            "Mobile Wallet",
            SharedPreferencesManager.getAccountBalance(context)))
        itemlist.add(DashboardItem(R.color.bg_blue,R.drawable.ic_mask_group_1_clipped,"Savings Wallet","0 Accounts"))
        itemlist.add(DashboardItem(R.color.bg_green,R.drawable.ic_group_16,"Expenditure Average","GHS 0.00 Daily"))
        itemlist.add(DashboardItem(R.color.bg_purple,R.drawable.ic_income,"Income Average","GHS 0.00 Daily"))


        return itemlist



    }

    fun gettransactionItems(): List<TransactionItem> {

        val itemlist= ArrayList<TransactionItem>()
        itemlist.add(TransactionItem("Ochola Tony","- GHS 21,000.00","10:45 am","REF: XVH89JQU"))
        itemlist.add(TransactionItem("Ochola Tony","- GHS 21,000.00","10:45 am","REF: XVH89JQU"))
        itemlist.add(TransactionItem("Ochola Tony","- GHS 21,000.00","10:45 am","REF: XVH89JQU"))
        itemlist.add(TransactionItem("Ochola Tony","- GHS 21,000.00","10:45 am","REF: XVH89JQU"))
        itemlist.add(TransactionItem("Ochola Tony","- GHS 21,000.00","10:45 am","REF: XVH89JQU"))



        return itemlist



    }
}