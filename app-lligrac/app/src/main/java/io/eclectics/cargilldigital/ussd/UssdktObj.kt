package io.eclectics.cargilldigital.ussd

import androidx.appcompat.app.AppCompatActivity

object UssdktObj {
   
lateinit var appCompatActivity:AppCompatActivity
/*
val REQ_CODE = 1
    lateinit var callCode:String
    var SIM_map:MutableMap<String, Int> = mutableMapOf()
    var simcardNames:ArrayList<String>? = null//java.util.ArrayList()
    var ussdResponseCallback: TelephonyManager.UssdResponseCallback? = null
    fun USSDConversion (activity1: AppCompatActivity,ussdCode:String) {
        var pdialog: SweetAlertDialog? = null
        val REQ_CODE = 1 // just some integer to identify request
        activity = activity1
        callCode = ussdCode

        /* lateinit var SIM_map: MutableMap<String, Int>
        var simcardNames: ArrayList<String>? = null
 
         var telephonyManager: TelephonyManager? = null
         var ussdResponseCallback: TelephonyManager.UssdResponseCallback? = null
         var handler: Handler? = null
         var passedCode: String? = null
         var shortCode1: String? = null
         var shortCode2: String? = null
         var callCode: String? = null
         var callPrefix: String? = null*/
        //private lateinit var appBarConfiguration: AppBarConfiguration

       /* override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)*/
           /* var SIM_map:MutableMap<String, Int> = mutableMapOf()
            var simcardNames:ArrayList<String>? = null//java.util.ArrayList()
        var ussdResponseCallback: TelephonyManager.UssdResponseCallback? = null*/
           // pdialog!!.show()
           var telephonyManager = activity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager


            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ussdResponseCallback = object : TelephonyManager.UssdResponseCallback() {
                    override fun onReceiveUssdResponse(
                        telephonyManager: TelephonyManager,
                        request: String,
                        response: CharSequence
                    ) {
                        //if our request is successful then we get response here
                        Toast.makeText(activity1, response, Toast.LENGTH_SHORT).show()
                    }

                    override fun onReceiveUssdResponseFailed(
                        telephonyManager: TelephonyManager,
                        request: String,
                        failureCode: Int
                    ) {

                        //request failures will be catched here
                        Log.e("mainjava", "java$failureCode\t$request")
                        //Toast.makeText(MainActivity.activity, failureCode, Toast.LENGTH_SHORT).show();

                        //here failure reasons are in the form of failure codes
                    }
                }
                if (ActivityCompat.checkSelfPermission(
                      activity1,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    activity1.requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQ_CODE)
                }
            } else {
                dailNumber(callCode!!)
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        private fun sims_details() {

            //store all available sim connection info as list
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //request permission if its not granted already

                activity.requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), REQ_CODE)
            }
            val subscriptionInfos = SubscriptionManager.from(
                activity
            ).activeSubscriptionInfoList
            //activity requires READ_PHONE_STATE permission

            //loop through all info objects and store info we store 2 details here carrier name and subscription Id fro each active SIM card
            //so we need map and an array to set SIM card names to Spinner
            for (subscriptionInfo in subscriptionInfos) {
                SIM_map.put(subscriptionInfo.carrierName.toString(), subscriptionInfo.subscriptionId)

                simcardNames!!.add(subscriptionInfo.carrierName.toString())
            }
            //we create arrayadapter to set it to spinner

            /* ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.activity,R.layout.support_simple_spinner_dropdown_item,simcardNames);
            spinner.setAdapter(arrayAdapter);*/
        }

        //override on req permission granted here


        //override on req permission granted here
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
            val UssdCodeNew = code + Uri.encode("#")
            startActivity(Intent("android.intent.action.CALL", Uri.parse("tel:$ussdCode")))
        }

        @SuppressLint("NewApi")
        private fun directCall() {
            //0 or 1
            val simSelected = 0
            val telecomManager = activity.getSystemService(TELECOM_SERVICE) as TelecomManager
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
                    activity,
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
                managerMain.sendUssdRequest(ussdCode, object : TelephonyManager.UssdResponseCallback() {
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
    }*/
}