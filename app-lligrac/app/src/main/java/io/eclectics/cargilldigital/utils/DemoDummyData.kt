package io.eclectics.cargilldigital.utils

import android.app.Activity
import io.eclectics.cargilldigital.printer.PrinterDataAdapter
import io.eclectics.cargill.model.FarmerModelObj
import io.eclectics.cargill.utils.NetworkUtility

object DemoDummyData {

    fun printListx(activity: Activity):List<PrinterDataAdapter.PrinterData>{
            var printerArrayList = ArrayList<PrinterDataAdapter.PrinterData>()
            var print1 = PrinterDataAdapter.PrinterData("Name \t\t:\tfarmername",1,1,true)
            var print2 = PrinterDataAdapter.PrinterData("Phone Number\t:\tfarmerPhonenumber",1,1,true)
            var print3 = PrinterDataAdapter.PrinterData("Amount \t\t:\t\tamount",1,1,true)
            var print4 = PrinterDataAdapter.PrinterData("Payment type \t:\t\tpaymentType",1,1,true)
            var print5 = PrinterDataAdapter.PrinterData("Reason \t:\treasons",1,1,true)

            printerArrayList.add(print1)
            printerArrayList.add(print2)
            printerArrayList.add(print3)
            printerArrayList.add(print4)
            printerArrayList.add(print5)
        var jsonList = NetworkUtility.getJsonParser().toJson(printerArrayList).toString()
        UtilPreference().setPrintData(activity,jsonList)
        UtilPreference.buyerUsername = "Buyer username"
        UtilPreference.userSection = "Section name"
        UtilPreference.sectionCode = ""
        return  printerArrayList
    }
    fun printList(activity:Activity):List<PrinterDataAdapter.PrinterData>{
        var listPass = UtilPreference().getPrintData(activity.applicationContext)
        var listdata:List<PrinterDataAdapter.PrinterData> = NetworkUtility.jsonResponse(listPass)
        return listdata
    }

    fun getFarmersList():List<FarmerModelObj>{
        var farmList = ArrayList<FarmerModelObj>()
        /*
        var f1 = FarmerModelObj("Georgina","Kwaba","225708396045","Katwasa","CBO-455-545")
        var f2 = FarmerModelObj("Botifla"," Natiss","225708396045","Katwasa","CBO-455-545")
        var f3 = FarmerModelObj("Koama","Sedata","225708396045","Katwasa","CBO-455-545")
        var f4= FarmerModelObj("Lilyanne","Ikua","225708396045","Katwasa","CBO-455-545")
        var f5 = FarmerModelObj("Naama","Kiama","225708396045","Katwasa","CBO-455-545")

        farmList.add(f1)
        farmList.add(f2)
        farmList.add(f3)
        farmList.add(f4)
        farmList.add(f5)
        */
        return farmList
    }



}