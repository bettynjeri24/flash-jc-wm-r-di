package io.eclectics.cargilldigital.viewmodel;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestUSSD {
    Map<String, Integer> SIM_map;
    ArrayList<String> simcardNames;

    TelephonyManager telephonyManager;
    TelephonyManager.UssdResponseCallback ussdResponseCallback;
    //Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendUssd(Activity activity){
        SIM_map = new HashMap<>();
        simcardNames = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
               activity. requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 234);
                return;
            }

            }
            telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ussdResponseCallback = new TelephonyManager.UssdResponseCallback() {
                    @Override
                    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                        //if our request is successful then we get response here

                       // Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {

                        //request failures will be catched here

                        Log.e("mainjava", "java" + failureCode + "\t" + request);
                        //Toast.makeText(MainActivity.this, failureCode, Toast.LENGTH_SHORT).show();

                        //here failure reasons are in the form of failure codes
                    }
                };
            }
        dialUssdToGetPhoneNumber("",0,activity);
        }

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg); //no need to change anything here
            }
        };
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dialUssdToGetPhoneNumber(String ussdCode, int sim, Activity activity) {

        if (ussdCode.equalsIgnoreCase("")) return;



        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 234);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            TelephonyManager manager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
            TelephonyManager manager2 = manager.createForSubscriptionId(2);

            TelephonyManager managerMain = (sim == 0) ? manager : manager2;

            managerMain.sendUssdRequest(ussdCode, new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);

                    Log.e("TAG", "onReceiveUssdResponse:  Ussd Response = " + response.toString().trim() );



                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);

                    Log.e("TAG", "onReceiveUssdResponseFailed: " + "" + failureCode + request);
                }
            }, new Handler());
        }

    }
    }


