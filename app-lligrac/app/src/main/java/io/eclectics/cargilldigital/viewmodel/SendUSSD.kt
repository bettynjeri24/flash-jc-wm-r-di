package io.eclectics.cargilldigital.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.ussd.USSDActivity
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargilldigital.utils.dk.getTrimPin
import io.eclectics.cargilldigital.utils.dk.getUserIndex
import io.eclectics.cargilldigital.utils.dk.trimAmount
import io.eclectics.cargilldigital.utils.dk.trimPhoneNumber

import org.json.JSONObject

object SendUSSD {

    lateinit var userData: UserDetailsObj



    /**
     * check login profile and perform the needful
     */
    fun makeLoginCall(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        endpoint: String,
        requireActivity: FragmentActivity
    ) {

        //respLiveData.value = ViewModelWrapper.error("Processing Request ..")
        try {
            var userdatajson = UtilPreference().getUserData(requireActivity)
            userData = NetworkUtility.jsonResponse(userdatajson)
            // check current profile
            // var activeProfile = UtillPreference().getActiveprofile(requireActivity)
            //(requireActivity as MainActivity).navigateUSSDFrag()
            prepareLogin(respLiveData, json, requireActivity)
        } catch (ex: Exception) {
            respLiveData.value =
                ViewModelWrapper.error(requireActivity.resources.getString(R.string.no_internet))
        }
        /* if(endpoint.contentEquals(ApiEndpointObj.transferToTelco)){
            //dailNumber(requireActivity,"*605*213*1234#")
            var ussdIntent = Intent(requireActivity, USSDActivity::class.java)
            ussdIntent.putExtra("code","2")
            requireActivity.startActivity(ussdIntent)
        }else {
            callIntent = Intent(requireActivity, USSDActivity::class.java)
            callIntent.putExtra("code","1")
           // prepareShortcode(endpoint,json)
            requireActivity.startActivity(callIntent)
        }*/
    }

    private fun prepareShortcode(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        endpoint: String,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        when (endpoint) {
            //  ApiEndpointObj.userLogin ->{prepareLogin( json, activity)}
            ApiEndpointObj.agentRequestFund -> {
                buyerRequestFunds(respLiveData, json, activity)
            }
            ApiEndpointObj.transferToTelco -> {
                transferFundsToTelco(respLiveData, json, activity)
            }
            ApiEndpointObj.buyerPayFarmer -> {
                buyerPayFarmer(respLiveData, json, activity)
            }
            ApiEndpointObj.individualBuyerTopup -> individualBuyerTopUp(
                respLiveData,
                json,
                activity
            )
            ApiEndpointObj.walletBalanceEnquiry -> walletBalanceEnquiry(
                respLiveData,
                json,
                activity
            )
            ApiEndpointObj.transferToCargillWalet -> wallet2WalletTransfer(
                respLiveData,
                json,
                activity
            )
            ApiEndpointObj.coopBookEvalue -> evalueBooking(
                respLiveData,
                json,
                activity
            )
            //evalueBooking

            //ApiEndpointObj.transferToTelco ->farmerTransferToTelco(json,activity)
        }
    }

    private fun wallet2WalletTransfer(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        try {
            var fundsAmount = trimAmount(json.getString("amount"))
            var senderPhoneNumber = trimPhoneNumber(json.getString("sendorPhoneNumber"))
            var farmerId = getUserIndex(json.getString("farmerId").toInt())
            var farmerPhoneNumber = trimPhoneNumber(json.getString("recipientPhoneNumber"))
            var pin = getTrimPin(json.getString("pin"))
            var userindex = getUserIndex(userData.userIndex!!)//123456
            var transId = ApiEndpointObj.WALLET2WALLET
            var userCode = "$transId$farmerPhoneNumber$fundsAmount"
            var usercode2 = "$userindex$farmerId$pin"
            var ussdCode = "$userCode$usercode2"
            (activity as MainActivity).navigateUSSDFrag(ussdCode, respLiveData)
        } catch (ex: Exception) {
            LoggerHelper.loggerError("ussdex", "wallet to wallet error ${ex.message}")
            respLiveData.value =
                ViewModelWrapper.error(activity.resources.getString(R.string.no_internet))
        }
    }

    private fun evalueBooking(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        try {
            var fundsAmount = trimAmount(json.getString("amount"))
            var senderPhoneNumber = trimPhoneNumber(json.getString("sendorPhoneNumber"))
            var farmerId = getUserIndex(json.getString("bankId").toInt())
            var farmerPhoneNumber = trimPhoneNumber(json.getString("sendorPhoneNumber"))
            var pin = getTrimPin(json.getString("pin"))
            var userindex = getUserIndex(userData.cooperativeIndex!!.toInt())//123456
            var transId = ApiEndpointObj.EVALUEBOOKINGUSSD
            var userCode = "$transId$farmerPhoneNumber$fundsAmount"
            var usercode2 = "$userindex$farmerId$pin"
            var ussdCode = "$userCode$usercode2"
            (activity as MainActivity).navigateUSSDFrag(ussdCode, respLiveData)
        } catch (ex: Exception) {
            LoggerHelper.loggerError("ussdex", "wallet to wallet error ${ex.message}")
            respLiveData.value =
                ViewModelWrapper.error(activity.resources.getString(R.string.no_internet))
        }
    }

    /**
     *individual coperative topup
     */
    private fun individualBuyerTopUp(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        var fundsAmount = trimAmount(json.getString("amount"))

        var buyerIndex = getUserIndex(json.getString("buyerIndex").toInt())
        var buyerPhoneNumber = trimPhoneNumber(json.getString("phonenumber"))
        var buyerId = json.getString("buyerid")
        // var coopId = getUserIndex(json.getString("coopIndex").toInt())
        var pin = json.getString("pin")
        var userindex = getUserIndex(userData.userIndex!!)//123456
        var transId = ApiEndpointObj.USSDINDIVIDUALBUYERTOPUP
        var userCode = "$transId$buyerPhoneNumber$fundsAmount"
        var usercode2 = "$userindex$buyerIndex$pin"

        var ussdCode = "$userCode$usercode2"
        (activity as MainActivity).navigateUSSDFrag(ussdCode, respLiveData)
        /* callIntent = Intent(AppCargillDigital.applicationContext(), USSDActivity::class.java)
        callIntent.putExtra("code","1")
        callIntent.putExtra("code1",userCode)
        callIntent.putExtra("code2",usercode2)
        activity.startActivity(callIntent)*/
    }

    /* private fun buyerFundsRequest(json: JSONObject, activity: FragmentActivity) {
         var fundsAmount = json.getString("amountRequested")
         var phoneNumber = json.getString("phonenumber")
         var coopId = json.getString("coopIndex")
         var pin = json.getString("pin")
         var channelId = ApiEndpointObj.fundsrequest
         var userPhoneNumber = userData.phoneNumber
         var userindex = userData.userIndex//123456
         var transId = ApiEndpointObj.transferTotelco
         var userCode = "$transId$pin$phoneNumber"
         var usercode2 = "$fundsAmount$channelId$coopId"

         callIntent = Intent(AppCargillDigital.applicationContext(), USSDActivity::class.java)
         callIntent.putExtra("code","1")
         callIntent.putExtra("code1",userCode)
         callIntent.putExtra("code2",usercode2)
         activity.startActivity(callIntent)
     }*/

    private fun transferFundsToTelco(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        var fundsAmount = trimAmount(json.getString("amount"))
        var phoneNumber = trimPhoneNumber(json.getString("cashoutNumber"))
        var channelId = json.getString("channelId")
        var coopId = getUserIndex(json.getString("beneficiaryId").toInt())
        var pin = json.getString("pin")
        var userPhoneNumber = userData.phoneNumber
        var userindex = getUserIndex(userData.userIndex!!)//123456
        var transId = ApiEndpointObj.TRANSFERTOTELCO
        var userCode = "$transId$phoneNumber$fundsAmount"
        var usercode2 = "$userindex$coopId$pin"

        var ussdCode = "$userCode$usercode2"
        (activity as MainActivity).navigateUSSDFrag(ussdCode, respLiveData)
        /*callIntent = Intent(AppCargillDigital.applicationContext(), USSDActivity::class.java)
        callIntent.putExtra("code","1")
        callIntent.putExtra("code1",userCode)
        callIntent.putExtra("code2",usercode2)
        activity.startActivity(callIntent)*/
    }

    private fun buyerRequestFunds(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        var fundsAmount = trimAmount(json.getString("amountRequested"))
        var phoneNumber = trimPhoneNumber(json.getString("phonenumber"))
        var buyerId = json.getString("buyerid")
        var coopId = getUserIndex(json.getString("coopIndex").toInt())
        var pin = json.getString("pin")
        var userindex = getUserIndex(userData.userIndex!!)//123456
        var transId = ApiEndpointObj.FUNDSREQUEST
        var userCode = "$transId$phoneNumber$fundsAmount"
        var usercode2 = "$userindex$coopId$pin"

        var ussdCode = "$userCode$usercode2"
        (activity as MainActivity).navigateUSSDFrag(ussdCode, respLiveData)
        /* callIntent = Intent(AppCargillDigital.applicationContext(), USSDActivity::class.java)
        callIntent.putExtra("code","1")
        callIntent.putExtra("code1",userCode)
        callIntent.putExtra("code2",usercode2)
        activity.startActivity(callIntent)*/
    }

    private fun buyerPayFarmer(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        var fundsAmount = trimAmount(json.getString("amount"))
        var phoneNumber = trimPhoneNumber(json.getString("phonenumber"))
        var farmerId = getUserIndex(json.getString("farmerId").toInt())
        var farmerPhoneNumber = trimPhoneNumber(json.getString("farmerPhonenumber"))
        var buyerId = json.getString("buyerid")
        var coopId = getUserIndex(json.getString("coopIndex").toInt())
        var pin = json.getString("pin")
        var userindex = getUserIndex(userData.userIndex!!)//123456
        var transId = ApiEndpointObj.BUYERFARMERPAYMENT
        var userCode = "$transId$farmerPhoneNumber$fundsAmount"
        var usercode2 = "$userindex$farmerId$pin"
        var ussdCode = "$userCode$usercode2"
        (activity as MainActivity).navigateUSSDFrag(ussdCode, respLiveData)
        /*callIntent = Intent(AppCargillDigital.applicationContext(), USSDActivity::class.java)
        callIntent.putExtra("code","1")
        callIntent.putExtra("code1",userCode)
        callIntent.putExtra("code2",usercode2)
        activity.startActivity(callIntent)*/
    }

    private fun prepareLogin(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        //sample  *605*214*1264874*154574578#
        var pin = trimAmount(json.getString("pin"))//1234
        var accPhoneNumber = json.getString("phonenumber") //2250703035850
        var trimmedNumber = trimPhoneNumber(accPhoneNumber)
        //from
        var fundsAmount = trimAmount("250")
        var coopId = getUserIndex(userData.cooperativeIndex!!.toInt())
        //to
        var userindex = getUserIndex(userData.userIndex!!)//123456
        var transId = ApiEndpointObj.USSDLOGIN
        var userCode = "$transId$trimmedNumber$fundsAmount"
        var usercode2 = "$userindex$coopId$pin"
        //var userIndexnLength = getUserIndex(userindex!!)
        //119
        // var transCode = "$userindex"
        var ussdCode = "$userCode$usercode2"
        (activity as MainActivity).navigateUSSDFrag(ussdCode, respLiveData)
        /*callIntent = Intent(AppCargillDigital.applicationContext(), USSDActivity::class.java)
        callIntent.putExtra("code","1")
        callIntent.putExtra("code1",userCode)
        callIntent.putExtra("code2",usercode2)
        // prepareShortcode(endpoint,json)
       activity.startActivity(callIntent)*/
        //( activity as MainActivity).dialUssdToGetPhoneNumber(userCode+usercode2,1)
        //ussdsampe
        //transId =2,pin = 4,phone = 9
        //recipient,phone = 9,amt = 6
    }

    /**
     *individual coperative topup
     */
    private fun walletBalanceEnquiry(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        activity: FragmentActivity
    ) {
        try {
            var fundsAmount = trimAmount("250")

            var buyerIndex = getUserIndex(json.getString("coopIndex").toInt())
            var buyerPhoneNumber = trimPhoneNumber(json.getString("phonenumber"))

            // var coopId = getUserIndex(json.getString("coopIndex").toInt())
            var pin = "1234"//json.getString("pin")
            var userindex = getUserIndex(userData.userIndex!!)//123456
            var transId = ApiEndpointObj.BALANCEENQUIRY
            var userCode = "$transId$buyerPhoneNumber$fundsAmount"
            var usercode2 = "$userindex$buyerIndex$pin"

            var ussdCode = "$userCode$usercode2"
            (activity as MainActivity).navigateWorkManagerUSSDFrag(ussdCode, respLiveData)
        } catch (ex: Exception) {
            respLiveData.value =
                ViewModelWrapper.error(activity.resources.getString(R.string.no_internet))
        }
    }


    fun phonelookUp(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        json: JSONObject,
        endpoint: String,
        requireActivity: FragmentActivity
    ) {

        respLiveData.value = ViewModelWrapper.error("Sending USSD ..")
        if (endpoint.contentEquals(ApiEndpointObj.transferToTelco)) {
            //dailNumber(requireActivity,"*605*213*1234#")
            var ussdIntent = Intent(requireActivity, USSDActivity::class.java)
            ussdIntent.putExtra("code", "2")
            requireActivity.startActivity(ussdIntent)
        } else {
            var ussdIntent = Intent(requireActivity, USSDActivity::class.java)
            ussdIntent.putExtra("code", "1")
            requireActivity.startActivity(ussdIntent)
        }
    }


    private fun dailNumber(activity: FragmentActivity, code: String) {
        val ussdCode = "*" + code + Uri.encode("#")
        val UssdCodeNew = code + Uri.encode("#")
        var intents = Intent("android.intent.action.dial", Uri.parse("tel:$ussdCode"))
        intents.putExtra("code", "1")
        activity.startActivity(intents)
    }

    fun sendFarmergetReq(
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        endpoint: String
    ) {

        respLiveData.value = ViewModelWrapper.error("Sending USSD ..")
    }

    fun extractTransId(id: String) {
        var transaid = id.take(2)
        //check if this is login
    }

}