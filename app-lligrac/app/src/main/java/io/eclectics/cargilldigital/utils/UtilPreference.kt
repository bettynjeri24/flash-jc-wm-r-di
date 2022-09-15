package io.eclectics.cargilldigital.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import io.eclectics.cargilldigital.AppCargillDigital
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargill.utils.NetworkUtility

class UtilPreference {
    //TODO DELETE THIS AFTER 15/04 ITS for demo
    fun getUserCreds(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            "creds", Context.MODE_PRIVATE)
        val tellerJsonData=  sharedPref!!.getString( "creds","")
        return tellerJsonData!!
    }
    fun setUserCreds(activity: FragmentActivity?, userDataJson:String) {
        val sharedPref = activity?.getSharedPreferences(
            "creds", Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString( "creds", userDataJson)
            commit()
        }
    }

    /**
     * set printable flag for buyer printable transcations
     */
    fun isPrintableData(activity: Context?):Boolean{
        val sharedPref = activity?.getSharedPreferences(
            "isprint", Context.MODE_PRIVATE)
        val tellerJsonData=  sharedPref!!.getBoolean( "isprint",false)
        return tellerJsonData!!
    }
    fun setIsPrintableData(activity: Context?, userDataJson:Boolean) {
        val sharedPref = activity?.getSharedPreferences(
            "isprint", Context.MODE_PRIVATE
        )
        with(sharedPref!!.edit()) {
            putBoolean( "isprint", userDataJson)
            commit()
        }
    }

    fun getPrintData(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            "print", Context.MODE_PRIVATE)
        val tellerJsonData=  sharedPref!!.getString( "print","")
        return tellerJsonData!!
    }
    fun setPrintData(activity: Context?, userDataJson:String) {
        val sharedPref = activity?.getSharedPreferences(
            "print", Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString( "print", userDataJson)
            commit()
        }
    }

    /**
     * Save current logged profile
     */
    //TODO DELETE THIS AFTER 15/04 ITS for demo
    fun getCurrentProfile(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            "profile", Context.MODE_PRIVATE)
        val tellerJsonData=  sharedPref!!.getString( "profile","")
        return tellerJsonData!!
    }
    fun setCurentProfile(activity: FragmentActivity?, userDataJson:String) {
        val sharedPref = activity?.getSharedPreferences(
            "profile", Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString( "profile", userDataJson)
            commit()
        }
    }
    //SAVED LOGIN USER DATA
    fun getUserData(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            userdata, Context.MODE_PRIVATE)
        val tellerJsonData=  sharedPref!!.getString("userdata","")
        return tellerJsonData!!
    }
    fun setUserData(activity: FragmentActivity?, userDataJson:String) {
        val sharedPref = activity?.getSharedPreferences(
            userdata, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString("userdata", userDataJson)
            commit()
        }
    }
    //getUserObj
    fun getUserObj(activity: Context?):UserDetailsObj{
        var sharedPrefUser = getUserData(activity)
        var obj:UserDetailsObj = NetworkUtility.jsonResponse(sharedPrefUser)
        return obj
    }

    //simulate farmer wallet balance
    fun setLoggedPhoneNumber(activity: Context, dataJson: String) {

        val sharedPref = activity?.getSharedPreferences(
            phonenumber, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString("phonenumber", dataJson)
            commit()
        }
    }

    fun getLoggedPhoneNumber(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            phonenumber, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getString("phonenumber", "")
        return tellerJsonData!!
    }

    //simulate farmer wallet balance
    fun setTirstTimeLogin(activity: Context, dataJson: Boolean) {

        val sharedPref = activity?.getSharedPreferences(
            firstLogging, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putBoolean("firstLogging", dataJson)
            commit()
        }
    }

    fun getTirstTimeLogin(activity: Context?):Boolean{
        val sharedPref = activity?.getSharedPreferences(
            firstLogging, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getBoolean("firstLogging", true)
        return tellerJsonData!!
    }

    fun saveLoggedAgent(activity: Context, dataJson: String) {

        val sharedPref = activity?.getSharedPreferences(
            offlinenFlag, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString("userdata", dataJson)
            commit()
        }
    }
//get saved agent
fun getLoggedAgent(activity: Context?):String{
    val sharedPref = activity?.getSharedPreferences(
        offlinenFlag, Context.MODE_PRIVATE
    )
    val tellerJsonData=  sharedPref!!.getString("userdata", "")
    return tellerJsonData!!
}

    //SAVE DASAHBOARD WALLET BALANCE FROM BOTH API AND USSD CALLS
    fun saveWalletBalance(activity: Context, dataJson: String) {

        val sharedPref = activity?.getSharedPreferences(
            walletbalance, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString("walletbalance", dataJson)
            commit()
        }
    }

    fun getWalletBalance(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            walletbalance, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getString("walletbalance", "0")
        return tellerJsonData!!
    }
    fun saveCashBalance(activity: Context, dataJson: String) {

        val sharedPref = activity?.getSharedPreferences(
            cashbalance, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString("cashbalance", dataJson)
            commit()
        }
    }

    //track float cash
    fun getcashfloat(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            cashbalance, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getString("cashbalance", "0")
        return tellerJsonData!!
    }
//simulate wallet balance
//simulate farmer wallet balance
fun saveFloatBalance(activity: Context, dataJson: String) {

    val sharedPref = activity?.getSharedPreferences(
        floatbalance, Context.MODE_PRIVATE
    )

    with(sharedPref!!.edit()) {
        putString("floatbalance", dataJson)
        commit()
    }
}
    fun getFloatBalance(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            floatbalance, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getString("floatbalance", "0")
        return tellerJsonData!!

    }
    //SAVE AND TRACK ACTIVE DASHBOARD
    fun setActiveDashbaord(activity: Context, dataJson: Int) {

        val sharedPref = activity?.getSharedPreferences(
            activeDashboard, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putInt("activeDashboard", dataJson)
            commit()
        }
    }
    fun getActiveDashboard(activity: Context?):Int{
        val sharedPref = activity?.getSharedPreferences(
            activeDashboard, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getInt("activeDashboard", defaultPage)
        return tellerJsonData!!

    }


    //SAVE AND TRACK ACTIVE PROFILE
    fun saveActiveprofile(activity: Context, dataJson: Int) {

        val sharedPref = activity?.getSharedPreferences(
            activeProfile, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putInt("activeProfile", dataJson)
            commit()
        }
    }
    fun getActiveprofile(activity: Context?):Int{
        val sharedPref = activity?.getSharedPreferences(
            activeProfile, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getInt("activeProfile", defaultPage)
        return tellerJsonData!!

    }

    //simulate farmer wallet balance
    fun setLoggedUserName(activity: Context, dataJson: String) {

        val sharedPref = activity?.getSharedPreferences(
            loggedusername, Context.MODE_PRIVATE
        )

        with(sharedPref!!.edit()) {
            putString("loggedusername", dataJson)
            commit()
        }
    }

    fun getLoggedUserName(activity: Context?):String{
        val sharedPref = activity?.getSharedPreferences(
            loggedusername, Context.MODE_PRIVATE
        )
        val tellerJsonData=  sharedPref!!.getString("loggedusername", "")
        return tellerJsonData!!
    }
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        var context = AppCargillDigital.applicationContext()
        return Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
            .uppercase()
    }

    companion object {
        var firstLogging = "firstLogging"
        var offlinenFlag = "userdata"
        var walletbalance = "walletbalance"
        var floatbalance = "floatbalance"
        var cashbalance = "cashbalance"
        var activeProfile = "activeProfile"
        var activeDashboard = "activeDashboard"
        var defaultPage = R.id.nav_loginFragment
        var phonenumber = "phonenumber"
        var userdata = "userdata"
        var loggedusername = "loggedusername"
        var buyerUsername = ""
        var  userSection= ""
        var  sectionCode= ""
        val deviceSessionUUID: String = AppCargillDigital.deviceSessionUUID()
    }

}