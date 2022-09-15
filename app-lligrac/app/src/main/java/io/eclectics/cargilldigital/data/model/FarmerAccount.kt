package io.eclectics.cargilldigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.eclectics.cargilldigital.R
import java.util.ArrayList

object FarmerAccount {

    fun getFarmerbeneficiary():List<Beneficiary>{
        var beneficiaryList = ArrayList<Beneficiary>()
        var providerlist = providerList()
        var ben1 = Beneficiary(1,"225708396044",providerlist[0].iconId,"Evans Gangla","1")
        var ben2 = Beneficiary(2,"225708396044",providerlist[1].iconId,"Mohamed Mwanza","2")
        var ben3 = Beneficiary(3,"225708396044",providerlist[2].iconId,"Gracious Jerry ","3")
        var ben4 = Beneficiary(4,"225708396044",providerlist[3].iconId,"Evans Gangla","3")
        var ben6 = Beneficiary(6,"",providerlist[7].iconId,"Visa card","5")

        var ben5 = Beneficiary(5,"",providerlist[4].iconId,"Wallet Account","4")
        beneficiaryList.add(ben5)
        beneficiaryList.add(ben1)
        beneficiaryList.add(ben2)
        beneficiaryList.add(ben3)
        beneficiaryList.add(ben4)
        beneficiaryList.add(ben6)


        return beneficiaryList

    }
    //get sample wallet data
    fun getWalletAccount():List<EMoneyProvider> {
        var beneficiaryList = ArrayList<EMoneyProvider>()
        var providerlist = providerList()
        var ben1 = EMoneyProvider(6, R.mipmap.otp_icon,"Cargill Wallet")
        var ben2 = EMoneyProvider(6, R.mipmap.wave,"Wave")
        beneficiaryList.add(ben1)
        beneficiaryList.add(ben2)
        return beneficiaryList
    }
    fun providerList():List<EMoneyProvider>{
        var providerList = ArrayList<EMoneyProvider>()
        var p1 = EMoneyProvider(1, R.mipmap.mtnmoney,"MTN Money")
        var p2 = EMoneyProvider(2, R.mipmap.orangemoney,"Orange Money")
        var p3 = EMoneyProvider(3, R.mipmap.riamoney,"Ria Bank")
        var p4 = EMoneyProvider(3, R.mipmap.ecobank,"Eco Bank")
        var p5 = EMoneyProvider(4, R.mipmap.otp_icon,"Wallet")
        var p6 = EMoneyProvider(6, R.mipmap.otp_icon,"Cargill Wallet")
        var p7 = EMoneyProvider(6, R.mipmap.wave,"Wave")
        var p8 = EMoneyProvider(7, R.mipmap.visaicon,"Card")
        providerList.add(p1)
        providerList.add(p2)
        providerList.add(p3)
        providerList.add(p4)
        providerList.add(p5)
        providerList.add(p6)
        providerList.add(p7)
        providerList.add(p8)
        return providerList
    }
    class Beneficiary(
        var catergoryId:Int,
        var accNo:String,
        var iconId:Int,
        var providersName:String,
        var providerId:String
    )

    class EMoneyProvider(
        var providerId:Int,
        var iconId:Int,
        var providersName:String
    )
    @Entity(tableName = "beneficiryacclist")
    class BeneficiaryAccObj(
        @SerializedName("beneficiaryName") val beneficiaryName:String,
        @SerializedName("accountholderphonenumber") val  accountholderphonenumber:String,
        @SerializedName("channelId") val  channelId:Int,
        @SerializedName("channelType") val  channelType:String,
        @SerializedName("channelName") val  channelName:String,
        @SerializedName("channelNumber") val  channelNumber:String,
        @SerializedName("channeAbbreviation") val  channeAbbreviation:String,
        @SerializedName("bankName") var  bankName: String?,
        @SerializedName("cardNumber") var  cardNumber:String?,
        @SerializedName("expiryDate") var  expiryDate:String?,
        @SerializedName("cvc") var  cvc: String?,
        @SerializedName("dateCreated") var dateCreated: String,
        @SerializedName("twofactorenabled") var twofactorenabled:String?,
        @SerializedName("status") var status:String,
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id") var id:Int//territoryCode
    )
}