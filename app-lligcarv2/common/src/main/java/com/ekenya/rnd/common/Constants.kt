package com.ekenya.rnd.common

import android.util.Base64
import com.ekenya.rnd.common.Constants.getStringBaseUrl
import okhttp3.MediaType.Companion.toMediaType
import java.util.*

private var CREDENTIALS: String = "esb_clientid" + ":" + "Y2hhbWFzZWNyZXQ="
val BASIC = "Basic " + Base64.encodeToString(CREDENTIALS.toByteArray(), Base64.NO_WRAP)

var SECRET_KEY: String = ""

var AUTH_TOKEN = ""

// FROM FARMER MODULE
// val BASEURL = "http://102.37.14.127:5000/"
val BASEURL = getStringBaseUrl()
val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()

val CAPTURE_CAMERA = 2

var COUNTRY_CODE = "225"
var SELECT_SERVICE_PROVIDER = ""
var CURRENT_USER_PHONENUMBER: String = ""
var CARGILL_USER_ID: String = ""
var CARGILL_USERINDEX: String = ""
var CARGILL_COOPERATIVEID: String = ""
var CARGILL_COOPERATIVEINDEX: String = ""
var CARGILL_FULL_NAME: String = ""
var CARGILL_PROVIDEDUSERID: String = ""
var CARGILL_USER_NAME: String = ""
var CARGILL_SECTION: String = ""

var AVAILABLE_BALANCE: String = "000"
var TOTAL_MONTHLY_CASHOUT: String = "000"

// USSD LOGIN
var USSDLOGIN = 11
var FUNDSREQUEST = 12
var BUYERFARMERPAYMENT = 13
var TRANSFERTOTELCO = 14
var BALANCEENQUIRY = 15
var USSDCOOPBOOKEVALUE = 16
var USSDINDIVIDUALBUYERTOPUP = 17
var WALLET2WALLET = 18
var EVALUEBOOKINGUSSD = 20
var FARMFORCEFARMERDATA = 12

object Constants {
    /**
     * The Base Package ID path for the application module
     *
     * The app module and all other modules should be relative to this path
     * E.G if base = 'com.ekenya.rnd', then app id would be 'com.ekenya.rnd.app', then module id, like support should be 'com.ekenya.rnd.support'
     */
    @JvmField
    var BASE_PACKAGE_NAME = "com.ekenya.rnd"

    init {
        System.loadLibrary("native-lib")
    }

    @JvmStatic
    external fun getStringBaseUrl(): String
}

lateinit var SIM_MAP: MutableMap<String, Int>
var SIMCARDNAMES: ArrayList<String>? = null
