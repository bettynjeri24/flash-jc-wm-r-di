package io.eclectics.cargilldigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.eclectics.cargilldigital.R

object SendMoney {
    class BeneficiaryAccList(
        @SerializedName("lastName") val  lastName:String,
        @SerializedName("companyRole") val  companyRole:String,
        @SerializedName("gender") val  gender:String,
        @SerializedName("town") val  town:String,
        @SerializedName("userInstitution_ID") val  userInstitutionID:String,
        @SerializedName("userInstitutionCode") val  userInstitutionCode:String,
        @SerializedName("firstLogin") val  firstLogin:String,
        @SerializedName("mobileNumber") val  mobileNumber:String
    )

    @Entity(tableName = "channellist")
    class ChannelListObj(
        @PrimaryKey(autoGenerate = true)
        var dbId:Int,
        @SerializedName("id") val  channelId:String,
        @SerializedName("channelName") val  channelName:String,
        @SerializedName("abbreviation") val  channelDesc:String,
        @SerializedName("type") val  type:String,
        //type
    )
    class AddbeneficiaryResponse(
        @SerializedName("message") val  message:String,
        @SerializedName("accountholdersNumber") val  accountholdersNumber:String,
        @SerializedName("newNumber") val  newNumber:String,
        @SerializedName("otp") val  otp:String
    )
     fun getImageResource(channelName: String):Int{
        when(channelName){
            "Orange"->{
               return R.mipmap.orangemoney
            }
            "MTN"->  return R.mipmap.mtnmoney
            "Orabank"-> return R.mipmap.orabank
            "Wave"-> return R.mipmap.wave
            "Moov"->return R.mipmap.moovafrica
            else ->R.mipmap.otp_icon
        }
         return  R.mipmap.otp_icon
    }

}