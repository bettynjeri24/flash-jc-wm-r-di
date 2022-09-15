package com.ekenya.lamparam.utilities

import android.content.Context
import android.os.Bundle
import com.ekenya.lamparam.R
import com.ekenya.lamparam.ui.dashboard.MainDashboard
import com.ekenya.lamparam.ui.receiveMoney.navOptions
import com.ekenya.lamparam.ui.schoolfees.SchoolListModel
import com.ekenya.lamparam.ui.schoolfees.SchoolTypeModel

class StaticData {

    fun mainDashBoard(context:Context):List<MainDashboard>{
        var dashboardList = ArrayList<MainDashboard>()
        var menu1 = MainDashboard(1,context.resources.getString(R.string.str_receive_money), ". ",R.drawable.cashpickup_custom)
        var menu2 = MainDashboard(2,context.resources.getString(R.string.str_send_money), ". ",R.drawable.sendmoney_custom)

        dashboardList.add(menu1)
        dashboardList.add(menu2)

        return dashboardList
    }

    fun payBillsList():List<MainDashboard>{
        var dashboardList = ArrayList<MainDashboard>()
        var menu1 = MainDashboard(1,"NSIA Seguros",":It Serves Everyone ", R.drawable.nsia)
        var menu2 = MainDashboard(2,"GUINEBIS Seguros",". ", R.drawable.guinebis_segoro)
        var menu3 = MainDashboard(3,"CANAL +",". ", R.drawable.canal_black)
        var menu4 = MainDashboard(4,"TSR",". ", R.mipmap.kcc)
        var menu5 = MainDashboard(5,"EAGB",". ", R.drawable.electritade)
        var menu6 = MainDashboard(6,"CMB",". ", R.drawable.cidade_de_bissau)
        var menu7 = MainDashboard(7,"FCR",". ", R.drawable.consavacado)
        var menu8 = MainDashboard(8,"INSS",". ", R.drawable.inss)
        var menu9 = MainDashboard(9,"DGA",". ", R.drawable.cado)
        var menu10 = MainDashboard(10,"DGCI",". ", R.drawable.dgci)

        dashboardList.add(menu1)
        dashboardList.add(menu2)
        dashboardList.add(menu3)
        dashboardList.add(menu4)
        dashboardList.add(menu5)
        dashboardList.add(menu6)
        dashboardList.add(menu7)
        dashboardList.add(menu8)
        dashboardList.add(menu9)
        dashboardList.add(menu10)

        return dashboardList
    }
    /**
     * Send money temporaryMenu
     */

    fun sendMoneyList():List<MainDashboard>{
        var dashboardList = ArrayList<MainDashboard>()
        var menu1 = MainDashboard(1,"EcoBank","", R.mipmap.ecobank)
        var menu2 = MainDashboard(2,"Money Gram",":Bringing you closer" ,R.mipmap.moneygram)
        var menu3 = MainDashboard(3,"MTN Money","", R.mipmap.mtnmoney)
        var menu4 = MainDashboard(4,"Monamon","" ,R.drawable.monnamon)
        var menu5 = MainDashboard(5,"Orange Money","", R.mipmap.orangemoney)
        var menu6 = MainDashboard(6,"Ria Money", "",R.mipmap.riamoney)
        var menu7 = MainDashboard(7,"Western Union",".", R.mipmap.westernunion)
        var menu8 = MainDashboard(8,"Wari",":It Serves Everyone " ,R.mipmap.wari)
        var menu9 = MainDashboard(9,"Real Transfer","", R.drawable.real_transfer)
        var menu10 = MainDashboard(10,"Small World",". ", R.mipmap.smallworld)
        var menu11 = MainDashboard(11,"World Remit", ". ",R.mipmap.worldremmit2)
        var menu12 = MainDashboard(12,"X-press Money", "",R.drawable.xpress_money)
        var menu13 = MainDashboard(13,"EZ Remit","" ,R.drawable.ez_remit)
        var menu14 = MainDashboard(14,"Dubai Remit","", R.drawable.dubai_remit)
        var menu15 = MainDashboard(15,"Trans fast",". ", R.drawable.transfast)
        var menu16 = MainDashboard(16,"Money Trans",". ", R.drawable.money_trans)
        /////
        var menu17 = MainDashboard(17,"Dahabshill", ". ",R.drawable.dahabshill)
        var menu18 = MainDashboard(18,"Asgori", ". ",R.drawable.asgori)
//        var menu19 = MainDashboard(19,"T&Y", R.drawable.xpress_money)
        var menu20 = MainDashboard(20,"Al-Alansari",". ", R.drawable.al_ansari)
        var menu21 = MainDashboard(21,"Send Exchange",". ", R.drawable.send_exchange)
        var menu22 = MainDashboard(22,"Pay Pal",". ", R.drawable.pay_pal)
        var menu23 = MainDashboard(23,"Doha Bank",". ", R.drawable.doha_bank)

        //dashboardList.add(menu3)
        //dashboardList.add(menu4)
        //dashboardList.add(menu5)
        dashboardList.add(menu6)
        dashboardList.add(menu7)
        //dashboardList.add(menu8)
        //dashboardList.add(menu9)
        //dashboardList.add(menu10)
        dashboardList.add(menu11)
        //dashboardList.add(menu1)
        dashboardList.add(menu2)
        dashboardList.add(menu12)
        dashboardList.add(menu13)
        dashboardList.add(menu14)
        dashboardList.add(menu15)
        dashboardList.add(menu16)
        dashboardList.add(menu17)
        dashboardList.add(menu20)
        dashboardList.add(menu21)
        dashboardList.add(menu22)
        dashboardList.add(menu23)
        return dashboardList
    }

    //school
    fun schoolType():List<SchoolTypeModel>{
        var dashboardList = ArrayList<SchoolTypeModel>()
        var menu1 = SchoolTypeModel("Ensino Secundário","1")
        var menu2 = SchoolTypeModel("Infantário","1")
        var menu3 =SchoolTypeModel("Ensino Básico","1")
        var menu5 =SchoolTypeModel("Ensino Superior","1")
        var menu6 =SchoolTypeModel("Básico, Primário e Scundário","1")
        var menu7 =SchoolTypeModel("Ensino Médio","1")

        dashboardList.add(menu1)
        dashboardList.add(menu2)
        dashboardList.add(menu3)
        dashboardList.add(menu5)
        dashboardList.add(menu6)
        dashboardList.add(menu7)

        return dashboardList
    }
    fun schoolList():List<SchoolListModel>{
        val bankViewList = ArrayList<SchoolListModel> ()
        val banklist1 = SchoolListModel("Liceu João XXIII","265********65","55","kcb", R.drawable.liceu_diao)
        val banklist2 = SchoolListModel("Aldeia SOS Jardim de Infância","265********65","55","kcb", R.drawable.aldeias)
        val banklist3 = SchoolListModel("Aldeia SOS Escola Básica","265********65","55","kcb", R.drawable.aldeias)
        val banklist4 = SchoolListModel("Liceu Politécnico Hermann Gmeiner","265********65","55","kcb", R.drawable.aldeias)
        //
        val banklist5 = SchoolListModel("ISG - Insituto Superior de Gestão","265********65","55","kcb", R.drawable.isg_insituto)
        val banklist6 = SchoolListModel("Escola BETEL","265********65","55","kcb", R.drawable.escola_adventista)
        val banklist7 = SchoolListModel("Universidade Colinas de Boé","265********65","55","kcb", R.drawable.universidade_colinas)
        val banklist8 = SchoolListModel("Liceu Domingos Ramos","265********65","55","kcb", R.drawable.liceu_domingos)
        val banklist9 = SchoolListModel("Universidade Lusófona","265********65","55","kcb", R.drawable.universidade_lusofona)
        val banklist10 = SchoolListModel("Centro de Formação 12 Pedras","265********65","55","kcb", R.drawable.centro_de)

        bankViewList.add(banklist1)
        bankViewList.add(banklist2)
        bankViewList.add(banklist3)
        bankViewList.add(banklist4)

        bankViewList.add(banklist5)
        bankViewList.add(banklist6)
        bankViewList.add(banklist7)
        bankViewList.add(banklist8)
        bankViewList.add(banklist9)
        bankViewList.add(banklist10)

        return bankViewList
    }
    //transaction type
    fun paymentModeList():ArrayList<String>{
        val pList = ArrayList<String>()
        pList.add("Lamparam Agent")
        pList.add("ATM")

        return pList
    }
}