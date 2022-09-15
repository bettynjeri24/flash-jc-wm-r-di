package com.ekenya.lamparam.utilities

import android.graphics.Color
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class NetworkUtility {

    /*fun getHeaderJson():JSONObject{
        var headjson = JSONObject()
        headjson.put("token", "gsghdhsfjjsdjkajskkakkd")
        headjson.put("user_agent", "Android")
        headjson.put("channel", " MOBILE_APP")
        headjson.put("user", "tovo")
        return headjson
    }*/



    fun sendRequest(pDialog: SweetAlertDialog){

        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Processing .."
        pDialog.setCancelable(false)
        pDialog.show()
    }

    fun transactionWarning(activity: FragmentActivity, message:String) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Warning!"
            contentText = message
            setCancelable(false)
            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    // progressDialog.dismissWithAnimation()
                    /* activity.finish()
                     activity.startActivity(activity.intent)*/
                    //activity.recreate()
                    //it?.findNavController()?.navigate(destination)
                }
            }
            show()
            //}

            //show()

        }
    }

    fun confirmTransactionEnd(
        it: View?,
        message: String,
        activity: FragmentActivity,
        destination: Int
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener {
                    SweetAlertDialog  ->  run{
                SweetAlertDialog.dismiss()
                SweetAlertDialog.dismissWithAnimation()
               // it?.findNavController()?.navigate(destination)
                it?.findNavController()?.popBackStack(destination,false)
            }
            }
            setCancelable(false)

            show()

        }
    }
    //escapes
    //error/warning
    fun confirmStock(
        it: View?,
        message: String,
        activity: FragmentActivity,
        destination: Int
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Confirm Stock"
            contentText = message

            setConfirmClickListener {
                    SweetAlertDialog  ->  run{
                SweetAlertDialog.dismiss()
                SweetAlertDialog.dismissWithAnimation()
               // it?.findNavController()?.navigate(destination)
                it?.findNavController()?.popBackStack(destination,false)
            }
            }
            setCancelable(false)

            show()

        }
    }
    fun confirmPopbackDialog(
        it: View?,
        message: String,
        activity: FragmentActivity
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener {
                    SweetAlertDialog  ->  run{
                SweetAlertDialog.dismiss()
                SweetAlertDialog.dismissWithAnimation()
                it?.findNavController()?.popBackStack()
            }
            }
            setCancelable(false)

            show()

        }
    }
    // currentFragment!!.findNavController().popBackStack(navDestination,false)
    fun transactionError(
        it: View?,
        message: String,
        activity: FragmentActivity,
        destination: Int
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Warning"
            contentText = message

            setConfirmClickListener {
                    SweetAlertDialog  ->  run{
                SweetAlertDialog.dismiss()
                SweetAlertDialog.dismissWithAnimation()
                // it?.findNavController()?.navigate(destination)
                it?.findNavController()?.popBackStack(destination,false)
            }
            }
            setCancelable(false)

            show()

        }
    }
   /* inline fun <reified T> jsonResponse(json: String): T {
        return getJsonParser().fromJson(json, object: TypeToken<T>(){}.type)
    }*/

    companion object{
        lateinit var gson: Gson
        @JvmStatic
        fun getJsonParser():Gson{
            gson=Gson()
            if (gson == null){
                val builder= GsonBuilder()
                gson = builder.create()
            }
            return gson
        }

        inline fun <reified T> jsonResponse(json: String): T {
            return getJsonParser().fromJson(json, object: TypeToken<T>(){}.type)
        }

    }
}