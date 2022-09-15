package com.ekenya.rnd.common.utils.custom

object ApiEndpointObj {
    //Mobile Gateway
    val gateWayEndpoint = "apigateway/mobileapp/"
    //FARMER PROFILE
    var addBeneficiaryAcc = "${gateWayEndpoint}cashoutchannelrequest"
    var verifyAddTelcoAccount = "${gateWayEndpoint}verifychannel"
    var removeBeneficiaryAcc = "${gateWayEndpoint}removebeneficiary"//
    //var balanceinquiry = "${gateWayEndpoint}balanceinquiry"
    var getBeneficiaryAccList = ""
    var getChannelList = "${gateWayEndpoint}getchannels"
    var myCashoutChannels = "${gateWayEndpoint}mycashoutchannels"
    var transferToTelco = "${gateWayEndpoint}farmercashout"
    var transferToCargillWalet = "${gateWayEndpoint}wallet2walletTransfer"
    var recentTransactions = "${gateWayEndpoint}latestTransactions"
    var walletBalanceEnquiry = "${gateWayEndpoint}balanceinquiry"

    //BUYER PROFILE
    var agentRequestFund = "${gateWayEndpoint}fundsrequest"
    var buyerFarmersList = "${gateWayEndpoint}getfarmerslist"
    var buyerPayFarmer = "${gateWayEndpoint}cocoapurchase"
    var buyerTopupRequest = "${gateWayEndpoint}buyertopuprequests"
    var ffPendingPayment = "${gateWayEndpoint}ffPendingPayments"
    var payPendingffPayment = "${gateWayEndpoint}ffPayments"
    //COOPERATIVE ENDPOINT
    var coopBuyersList = "${gateWayEndpoint}cooperativebuyerslist"
    var coopBookEvalue = "${gateWayEndpoint}bookevalue"
    var coopApproveEvalue ="${gateWayEndpoint}approvebooking"
    var coopBookedEvalueList = "${gateWayEndpoint}bookingslist"
    var approveFundsRequest =  "${gateWayEndpoint}approvebuyertopups"
    var getFundsRequestList = "${gateWayEndpoint}topuprequests"
    var individualBuyerTopup = "${gateWayEndpoint}individualbuyertopups"

    //verifychannel
    var customerLookup = "auth/accountlookupmobile"
    var verifyLookupOtp = "auth/verifyotp"
    var changeAccPin = "${gateWayEndpoint}changepin"
    var createAccPin = "auth/createpin"
    var userLogin = "auth/mobileapplogin"
    var providerIdLookup = "auth/accountidlookupmobile"


    //Ussd login
    var ussdLogin = 11
    var fundsrequest = 12
    var buyerFarmerPayment = 13
    var transferTotelco = 14
    var balanceEnquiry = 15
    var ussdCoopBookEvalue = 16
    var ussdindividualBuyerTopup = 17
    var wallet2wallet = 18


    //Sample
    //buyerPayFarmer = 1370168637932504110317512345
    //farmertransfer = 147030358502104110517512345
    //walet2wallet = 187030358502104110517512345





  /*  fun updateRequest(json:JSONObject):JSONObject{
        json.put("deviceUUId", UtillPreference.deviceSessionUUID)
        json.put("deviceId",UtillPreference().getDeviceId())
        json.put("language", PreferenceProvider(AppCargillDigital.applicationContext()).getLanguage())
        return json
    }*/

   /* fun getHashToken():HashMap<String,String>{
        *//*var credentials: String = Credentials.basic("ADMIN", "Ya6J&eT@")
        var authString = "Basic "+ Base64.encodeToString(("ADMIN:Ya6J&eT@").toByteArray(), Base64.NO_WRAP)
        LoggerHelper.loggerError("authe","creds $credentials \n bas $authString")
        var hashToken = HashMap<String,String>()//authtoken token
        hashToken.put("Authorization",credentials)*//*
        var userJson = UtillPreference().getUserData(AppCargillDigital.applicationContext())
        var userData:UserDetailsObj = NetworkUtility.jsonResponse(userJson)
        var hashToken = HashMap<String,String>()
         hashToken.put("Authorization","Bearer "+ userData.token)
        //hashToken.put("x-app-id","252378bf")
        // hashToken.put("x-app-key","aa9a122425b16c707b2860d2935b9712")//-
        return hashToken
    }*/
}