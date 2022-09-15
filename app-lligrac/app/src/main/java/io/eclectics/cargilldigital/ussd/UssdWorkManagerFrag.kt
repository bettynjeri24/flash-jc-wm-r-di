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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.LoggerHelper
import org.json.JSONObject
import javax.inject.Inject
import kotlin.properties.Delegates

    @AndroidEntryPoint
    class UssdWorkManagerFrag : Fragment() {
        var pdialog: SweetAlertDialog? = null
        private val REQ_CODE = 1 // just some integer to identify request


        lateinit var SIM_map: MutableMap<String, Int>
        var simcardNames: ArrayList<String>? = null
        var transIdPassedCode by Delegates.notNull<Int>()

        @Inject
        lateinit var navOptions: NavOptions

        var telephonyManager: TelephonyManager? = null
        var ussdResponseCallback: TelephonyManager.UssdResponseCallback? = null
        var handler: Handler? = null
        var passedCode: String? = null
        var shortCode1: String? = null
        var shortCode2: String? = null
        var callCode: String? = null
        var callPrefix: String? = null
        lateinit var rootView: View
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            SIM_map = mutableMapOf()
            simcardNames = java.util.ArrayList()

            telephonyManager = requireActivity().getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.fragment_ussd, container, false)
            pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
            callPrefix = "9144"//"605*214"
            passedCode = requireArguments().getString("ussdcode")
            callCode = callPrefix+"*991$passedCode"//"117016863793200288267512345" //callPrefix+"*"+shortCode1+shortCode2;
            pdialog!!.show()
            transIdPassedCode = passedCode!!.take(2).toInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ussdResponseCallback = object : TelephonyManager.UssdResponseCallback() {
                    override fun onReceiveUssdResponse(
                        telephonyManager: TelephonyManager,
                        request: String,
                        response: CharSequence
                    ) {
                        //if our request is successful then we get response here
                        Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
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
                        requireActivity(),
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQ_CODE)
                }
            } else {
                // dailNumber(callCode!!)
            }
            dialUssdToGetPhoneNumber("*$callCode#", 1)
            return rootView
        }

        companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment UssdFragment.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                UssdFragment().apply {
                    /* arguments = Bundle().apply {
                         putString(ARG_PARAM1, param1)
                         putString(ARG_PARAM2, param2)
                     }*/
                }
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        private fun sims_details() {

            //store all available sim connection info as list
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //request permission if its not granted already
                requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), REQ_CODE)
            }
            val subscriptionInfos = SubscriptionManager.from(
                requireActivity()
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
            val telecomManager = requireActivity().getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
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
            LoggerHelper.loggerError("ussdTag","tag $ussdCode")
            if (ussdCode.equals("", ignoreCase = true)) return
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 234)
                }
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = requireActivity().getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
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
                       /* val tx = rootView.findViewById<TextView>(R.id.tvUssd)
                        tx.text = response.toString().trim { it <= ' ' }*/
                        navigateToDestinationFragment(response.toString(),true)
                    }

                    override fun onReceiveUssdResponseFailed(
                        telephonyManager: TelephonyManager,
                        request: String,
                        failureCode: Int
                    ) {
                        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode)
                        pdialog!!.cancel()
                        navigateToDestinationFragment("Request failed kindly try again later",false)
                        //dailNumber(callCode!!)
                        Log.e("TAG", "onReceiveUssdResponseFailed: $failureCode$request")
                    }
                }, Handler())
            } else {
                //dailNumber(callCode!!)
            }
        }
        //"{ message = Successfuly transfered Funds , amount = 10, transId = 1470303585021028826712345, responseCode = 0 }"
//{"balance":1347980,"transId":"11701686379325028817512345","role":"Employee","responseCode":"0"}
        private fun navigateToDestinationFragment(response: String, status: Boolean) {
            //try updating current live data -> BUT LAZIMA KUKWE NA SIMPLER WAY
            if (status) {
                try {
                    var jsonResponse = JSONObject(response)

                    //check transaction code to navigate appropriately
                    checkTransId(jsonResponse)
                }catch (ex:Exception){
                    when(transIdPassedCode) {
                        ApiEndpointObj.USSDLOGIN -> {
                            (activity as MainActivity).updateCurrentLiveData(response, false)
                            findNavController().navigate(R.id.nav_loginAccountFragment)
                        }
                        else ->{
                            (activity as MainActivity).updateCurrentLiveData(response, false)
                            (activity as MainActivity).navigateUSSDWorkmanager()
                        }
                    }
                }
            }else{
                when(transIdPassedCode) {
                    ApiEndpointObj.USSDLOGIN -> {
                        (activity as MainActivity).updateCurrentLiveData(response, status)
                        findNavController().navigate(R.id.nav_loginAccountFragment)
                    }
                    else ->{
                        (activity as MainActivity).updateCurrentLiveData(response, status)
                        (activity as MainActivity).navigateUSSDWorkmanager()
                    }
                }
            }
        }

        private fun checkTransId(jsonResponse: JSONObject) {
            var transId = jsonResponse.getString("transId").take(2).toInt()
            var statusCode = jsonResponse.getString("responseCode")
            lateinit var message:String
            when(transId){
                ApiEndpointObj.USSDLOGIN->{
                    if(statusCode.contentEquals("0")) {
                        var role = jsonResponse.getString("role")
                        redirectToProfileAccount(role)
                        var userJson = UssdResponse.logginResponse(jsonResponse.toString())
                        LoggerHelper.loggerError("ussdformt","format $userJson")
                        (activity as MainActivity).updateCurrentLiveData(userJson, true)
                    }else{
                        message = "Failed"
                        try{
                            message = "${message}${jsonResponse.getString("message")}"
                        }catch (ex:Exception){}
                        (activity as MainActivity).updateCurrentLiveData(message, false)
                        findNavController().navigate(R.id.nav_loginAccountFragment)
                    }
                }
                ApiEndpointObj.BALANCEENQUIRY ->{
                    if(statusCode.contentEquals("0")){
                        var walletBal = jsonResponse.getInt("walletBalance").toString()
                        (activity as MainActivity).updateCurrentLiveData(walletBal, true)
                         (activity as MainActivity).navigateUSSDWorkmanager()
                    }
                }
                else->{
                    var message = jsonResponse.getString("message")
                    (activity as MainActivity).updateCurrentLiveData(message, true)
                    (activity as MainActivity).navigateUSSDWorkmanager()
                }
            }

        }
        private fun redirectToProfileAccount(role: String) {
            when (role) {
                "Buyers" -> {
                    findNavController().navigate(R.id.nav_agentProfile, null, navOptions)
                }
                "Farmer" -> {
                    var bundle = Bundle()
                    bundle.putString("menu", "mainmenu")
                    findNavController().navigate(R.id.nav_farmerDashboard, bundle, navOptions)
                }
                "Cooperative" -> {
                    findNavController().navigate(R.id.nav_cooperativeProfile, null, navOptions)
                }
                "Employee" -> {
                    UtilPreference().saveFloatBalance(requireActivity(), "50000")
                    findNavController().navigate(R.id.nav_generalWalletProfile, null, navOptions)
                }
            }
        }
    }