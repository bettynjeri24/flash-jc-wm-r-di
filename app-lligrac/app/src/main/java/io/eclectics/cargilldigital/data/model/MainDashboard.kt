package io.eclectics.cargill.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.eclectics.cargilldigital.R

object MainDashboard {
    data  class BillPayment (
        var menuId:Int,
        var menuTitle:String,
        var imageDrawable:Int
    )

    fun payBillsList():List<BillPayment>{
        var dashboardList = ArrayList<BillPayment>()
        var menu1 = BillPayment(1,"CIE electricity  ", R.mipmap.cie)
        var menu2 = BillPayment(2,"SODECI", R.mipmap.sodeci)
        var menu12 = BillPayment(12,"BUY AIRTIME", R.drawable.buy_airtime)
        var menu22 = BillPayment(22,"DISBURSEMENT", R.drawable.send_money)
        var menu212 = BillPayment(212,"Pay Merchant", R.mipmap.scan)
        var menu3 = BillPayment(3,"NWSC", R.mipmap.nwsc)
        var menu4 = BillPayment(4,"KCC", R.mipmap.kcc)
        var menu5 = BillPayment(5,"Schools", R.mipmap.school)
        //var menu6 = BillPayment(6,"OTT TAX", R.mipmap.taxcopy)
        var menu7 = BillPayment(7,"NSSF", R.mipmap.nssf)
        var menu8 = BillPayment(8,"Pay ST", R.mipmap.payst)
        var menu9 = BillPayment(9,"PAY GOTV", R.mipmap.gotv)
        dashboardList.add(menu1)
        dashboardList.add(menu2)
        dashboardList.add(menu12)
        dashboardList.add(menu212)
        dashboardList.add(menu22)
      /*  dashboardList.add(menu3)
        dashboardList.add(menu4)
        dashboardList.add(menu5)
       // dashboardList.add(menu6)

        dashboardList.add(menu7)
        dashboardList.add(menu8)
        dashboardList.add(menu9)*/
        return dashboardList
    }

    //send money
    fun sendMoneyList():List<BillPayment>{
        var dashboardList = ArrayList<BillPayment>()
        var menu1 = BillPayment(1,"Bank Transfer", R.mipmap.ecobank)
        var menu12 = BillPayment(12,"Cooperative", R.drawable.logo)
        var menu5 = BillPayment(5,"Orange Money", R.mipmap.orangemoney)

        var menu3 = BillPayment(3,"MTN Money", R.mipmap.mtnmoney)
        var menu4 = BillPayment(4,"NSIA AVS", R.mipmap.nsia)
        var menu6 = BillPayment(6,"Ria Money", R.mipmap.riamoney)
        var menu7 = BillPayment(7,"Western Union", R.mipmap.westernunion)
        var menu8 = BillPayment(8,"Wari", R.mipmap.wari)
        var menu10 = BillPayment(10,"Small World", R.mipmap.smallworld)
        var menu11 = BillPayment(11,"World Remit", R.mipmap.worldremmit2)
        dashboardList.add(menu1)
        dashboardList.add(menu12)
        dashboardList.add(menu5)
        dashboardList.add(menu3)
        dashboardList.add(menu4)

        dashboardList.add(menu6)

        dashboardList.add(menu7)
        dashboardList.add(menu8)
        dashboardList.add(menu10)
        dashboardList.add(menu11)
        return dashboardList
    }

    private val _transactionsList = MutableLiveData<List<MainDashboard.BillPayment>>()
    val transactionsList: LiveData<List<BillPayment>>
        get() = _transactionsList

    private val _ftList = MutableLiveData<List<MainDashboard.BillPayment>>()
    val ftransferList: LiveData<List<BillPayment>>
        get() = _ftList

    init {
        _transactionsList.value = payBillsList()
        _ftList.value = sendMoneyList()
    }
}