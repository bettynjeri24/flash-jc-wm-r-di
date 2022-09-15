package io.eclectics.cargilldigital.ussd

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.UssdResponseCallback
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import io.eclectics.cargilldigital.R

class USSDConversion : AppCompatActivity() {
    var pdialog: SweetAlertDialog? = null
    private val REQ_CODE = 1 // just some integer to identify request


    lateinit var SIM_map: MutableMap<String, Int>
    var simcardNames: ArrayList<String>? = null

    var telephonyManager: TelephonyManager? = null
    var ussdResponseCallback: UssdResponseCallback? = null
    var handler: Handler? = null
    var passedCode: String? = null
    var shortCode1: String? = null
    var shortCode2: String? = null
    var callCode: String? = null
    var callPrefix: String? = null
    //private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SIM_map = mutableMapOf()
        simcardNames = java.util.ArrayList()
        pdialog!!.show()
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ussdResponseCallback = object : UssdResponseCallback() {
                override fun onReceiveUssdResponse(
                    telephonyManager: TelephonyManager,
                    request: String,
                    response: CharSequence
                ) {
                    //if our request is successful then we get response here
                    Toast.makeText(this@USSDConversion, response, Toast.LENGTH_SHORT).show()
                }

                override fun onReceiveUssdResponseFailed(
                    telephonyManager: TelephonyManager,
                    request: String,
                    failureCode: Int
                ) {

                    //request failures will be catched here
                    Log.e("mainjava", "java$failureCode\t$request")
                    //Toast.makeText(MainActivity.this, failureCode, Toast.LENGTH_SHORT).show();

                    //here failure reasons are in the form of failure codes
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    this@USSDConversion,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQ_CODE)
            }
        } else {
            dailNumber(callCode!!)
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun sims_details() {

        //store all available sim connection info as list
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //request permission if its not granted already
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), REQ_CODE)
        }
        val subscriptionInfos = SubscriptionManager.from(
            applicationContext
        ).activeSubscriptionInfoList
        //this requires READ_PHONE_STATE permission

        //loop through all info objects and store info we store 2 details here carrier name and subscription Id fro each active SIM card
        //so we need map and an array to set SIM card names to Spinner
        for (subscriptionInfo in subscriptionInfos) {
            SIM_map.put(subscriptionInfo.carrierName.toString(), subscriptionInfo.subscriptionId)

            simcardNames!!.add(subscriptionInfo.carrierName.toString())
        }
        //we create arrayadapter to set it to spinner

        /* ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,simcardNames);
        spinner.setAdapter(arrayAdapter);*/
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sims_details()
            //calling function again once we get permissions granted
        }
    }

    private fun dailNumber(code: String) {
        pdialog!!.cancel()
        val ussdCode = "*" + code + Uri.encode("#")

        startActivity(Intent("android.intent.action.CALL", Uri.parse("tel:$ussdCode")))
    }

    @SuppressLint("NewApi")
    private fun directCall() {
        //0 or 1
        val simSelected = 0
        val telecomManager = this.getSystemService(TELECOM_SERVICE) as TelecomManager
        @SuppressLint("MissingPermission") val phoneAccountHandleList =
            telecomManager.callCapablePhoneAccounts
        val intent = Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse("tel:" + "PHONE_NUMBER")
        intent.putExtra("com.android.phone.force.slot", true)
        if (simSelected == 0) {   //0 for sim1
            intent.putExtra("com.android.phone.extra.slot", 0) //0 or 1 according to sim.......
            if (phoneAccountHandleList != null && phoneAccountHandleList.size > 0) intent.putExtra(
                "android.telecom.extra.PHONE_ACCOUNT_HANDLE",
                phoneAccountHandleList[0]
            )
        } else {    //0 for sim1
            intent.putExtra("com.android.phone.extra.slot", 1) //0 or 1 according to sim.......
            if (phoneAccountHandleList != null && phoneAccountHandleList.size > 1) intent.putExtra(
                "android.telecom.extra.PHONE_ACCOUNT_HANDLE",
                phoneAccountHandleList[1]
            )
        }
        startActivity(intent)
    }
    fun dialUssdToGetPhoneNumber(ussdCode: String, sim: Int) {
        if (ussdCode.equals("", ignoreCase = true)) return
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 234)
            }
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val manager2 = manager.createForSubscriptionId(2)
            val managerMain = if (sim == 0) manager else manager2
            managerMain.sendUssdRequest(ussdCode, object : UssdResponseCallback() {
                override fun onReceiveUssdResponse(
                    telephonyManager: TelephonyManager,
                    request: String,
                    response: CharSequence
                ) {
                    super.onReceiveUssdResponse(telephonyManager, request, response)
                    pdialog!!.cancel()
                    Log.e(
                        "TAG",
                        "onReceiveUssdResponse:  Ussd Response = " + response.toString()
                            .trim { it <= ' ' })
                    val tx = findViewById<TextView>(R.id.tvUssd)
                    tx.text = response.toString().trim { it <= ' ' }
                    //navigateToDestinationFragment(response.toString())
                }

                override fun onReceiveUssdResponseFailed(
                    telephonyManager: TelephonyManager,
                    request: String,
                    failureCode: Int
                ) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode)
                    pdialog!!.cancel()
                    dailNumber(callCode!!)
                    Log.e("TAG", "onReceiveUssdResponseFailed: $failureCode$request")
                }
            }, Handler())
        } else {
            dailNumber("*1000#")
        }
    }
}