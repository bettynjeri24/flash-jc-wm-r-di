package io.eclectics.cargilldigital.ussd

import com.google.gson.JsonArray
import io.eclectics.cargilldigital.AppCargillDigital
import io.eclectics.cargilldigital.data.model.CoopBank
import io.eclectics.cargilldigital.data.model.Section
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject

object UssdResponse {

    fun generalMsgResponse(json:String){

    }
    fun logginResponse(json:String):String{
        var formartJson = JSONObject(json)

        var userJson = getPrefuser(json)
        return userJson.toString()//formartJson.toString()
    }
    fun getPrefuser(json:String):JSONObject {
        var dummySection = Section(1,"dummy","dummy","dummy")
        var jsonResponse = JSONObject(json)
        var balance = jsonResponse.getString("balance")
        var dummyBank = CoopBank(1,"pkau","oak","oak",1)
        var bankJson = NetworkUtility.getJsonParser().toJson(dummyBank)
        var bankJsonObject = JSONObject(bankJson)
        var userJson  = UtilPreference().getUserData(AppCargillDigital.applicationContext().applicationContext)
        var userData:UserDetailsObj = NetworkUtility.jsonResponse(userJson)
        var transactions=JsonArray()
        if(userData.transactions == null) {
                transactions = JsonArray()
            }else{
                transactions = userData.transactions!!
        }

       var loginJson = "{\n" +
               "    \"userIndex\": ${userData.userIndex},\n" +
               "    \"userId\": \"${userData.userId}\",\n" +
               "    \"firstName\": \"${userData.firstName}\",\n" +
               "    \"lastName\": \"${userData.lastName}\",\n" +
               "    \"providedUserId\": \"${userData.providedUserId}\",\n" +
               "    \"cooperativeIndex\": \"${userData.cooperativeIndex}\",\n" +
               "    \"walletBalance\": \"$balance\",\n" +
               "    \"transactions\": $transactions,\n "+
               "    \"bankAccount\": $bankJsonObject,\n "+
               "    \"cooperativeId\": \"${userData.cooperativeId}\",\n" +
               "    \"emailAddress\": \"${userData.emailAddress}\",\n" +
               "    \"phoneNumber\": \"${userData.phoneNumber}\",\n" +
               "    \"role\": \"${userData.role}\",\n" +
               "    \"section\": ${userData.section}\n" +
               "}"
        return   JSONObject(loginJson)
    }
}