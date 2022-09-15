package com.ekenya.rnd.common

class Constants {
    companion object {
        /**
         * The Base Package ID path for the application module
         *
         * The app module and all other modules should be relative to this path
         * E.G if base = 'com.ekenya.rnd', then app id would be 'com.ekenya.rnd.app', then module id, like support should be 'com.ekenya.rnd.support'
         */
        @JvmField
        var BASE_PACKAGE_NAME = "com.ekenya.rnd"
        var BASE_URL: String = ""
        var PINNER_CERT = "sha256/QTlFQ0ZFQTQ3QUQyNjEyODM3OUJCQTA5NjdGNzk3NjMzNTQ4OUI5Qw=="
        var PINNER_URL = "test-portal.ekenya.co.ke"
        val ApplinkTest: String = "https://play.google.com/apps/internaltest/4698157621550810948"
        var token = ""
        var PHONENUMBER = ""
        var EMAILADDRESS = ""
        var isSacco = false
        var ORGID = ""
        var SIGNUPORGID = ""
        var GENDER_ID = ""
        var GENDER_NAME = ""
        var SELECTED_TYPE = 0
        var EMPLOYERNAME = ""
        var EMPLOYERID = ""
        var EMPTERMNAME = ""
        var EMPTERMID = ""
        var RSHIPID = ""
        var RSHIPNAME = ""

        /**personal Info*/
        var USERFULLNAME = ""
        var RegUsername = ""
        var userFname = ""
        var USERNAME = ""
        var SaccoName = ""
        var PIDNO = ""
        var FromID = 0
        var FIRSTLOGIN: Boolean = true
        var TIMEDIFF: Int = 0
        var ACCOUNTID: Int = 0
        var SAVINGPRODUCTID: String = ""
        var SAVINGPRODUCTNAME: String = ""
        var LOANID: Int = 0
        var PRODUCTID: String = ""
        var PRODUCTNAME: String = ""
        var SAVINGACCOUNTNAME: String = ""
        var SAVINGACCOUNTID: Int = -1
        var SAVINGACCOUNTNO: String = ""
        var SAVINGAID: Int = 0
        var SAVINGPID: String = ""
        var BANKBRANCHNAME: String = ""
        var BANKBRANCHID: String = ""
        var BANKNAME: String = ""
        var BANKID: String = ""
        var SPROVIDERID: String = ""
        var SPROVIDERNAME: String = ""
        var BILLER_NAME: String = ""
        var CONTACTFROM: Int = -1
        var DIALOGSELETED = 0
        var isFROMPESALINKPHONE = 0
        var AIRTIMEPHON = ""
        var AIRTIMEREFID = ""
        var AIRTIMEAMOUNT = ""
        var AIRTIMECHARGES = -1
        var AIRTIMEDUTY = -1
        var BILLERCODE = ""
        var BILLERURL = ""
        var ISFROMBLOOKUP = 0
        var SHAREMEMBERLOOKUP = ""
        var SHAREPHONELOOKUP = ""
        var SHARENAMELOOKUP = ""
        var SHAREREFCODE = ""
        var SHAREVALUE = 0
        var SHAREKES = ""
        var TOTALSVALUE = ""
        var CANCELCODE = ""
        var ACCEPTCODE = ""
        var DETAILCODE = ""
        var DTRANSTYPE = ""
        var DSHARETRANS = ""
        var DNAME = ""
        var DMEMNUMBER = ""
        var DPHONENUMBER = ""
        var DSTATUS = ""
        var TOREJECT = 0
        var DSentTRANSTYPE = ""
        var DSentSHARETRANS = ""
        var DSentNAME = ""
        var DSentMEMNUMBER = ""
        var DSentPHONENUMBER = ""
        var DSentSTATUS = ""
        var FROMSHARESENT = 0
        var STARTDATE = ""
        var ENDDATE = ""
        var SELFAMOUNT = ""
        var SaveNameFrom = ""
        var SaveNameTo = ""
        var SaveACNOTo = ""
        var SaveACNOFROM = ""
        var isMobileMClicked = false
        var isMore = false
        var isRemove = false
        var FULLSFORMID: Int = 0
        var FULLSREFCODE: String = ""
        var FULLSCHARGES: Int = 0
        var TOPIN = -1
        var isFromUpdate = 0
        var CHARGES = 0
        var EDUTY = 0
        var MEMBERACCNO = ""
        var MEMBERRECIPNAME = ""
        var FORMID = 0
        var SELFREFID = ""
        var VEHICLENAME = ""
        var VEHICLEID = ""
        var DURATIONID = 0
        var DURATIONNAME = ""
        var ZONEID = ""
        var ZONENAME = ""
        var CountyName = ""
        var GENERAL_AMOUNT = ""
        var GENERAL_NAME = ""
        var GENERAL_NUMBER = ""

        // git
        /* fun getRequestBody(requestJson: JSONObject): RequestBody {
             return RequestBody.create("application/json".toMediaTypeOrNull(), requestJson.toString())
         }*/
    }
}
