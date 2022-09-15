package io.eclectics.cargilldigital.ussd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.eclectics.cargilldigital.R;

public class USSDActivity extends AppCompatActivity {
    SweetAlertDialog pdialog;
    private static final int REQ_CODE = 1; // just some integer to identify request

    Map<String, Integer> SIM_map;
    ArrayList<String> simcardNames;

    TelephonyManager telephonyManager;
    TelephonyManager.UssdResponseCallback ussdResponseCallback;
    String passedCode;
    String shortCode1;
    String shortCode2;
    String callCode;
    String callPrefix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ussd_empty_layout);

        Intent intent = getIntent();
        passedCode = intent.getStringExtra("code");
        shortCode1 = intent.getStringExtra("code1");
        shortCode2 = intent.getStringExtra("code2");
        callPrefix = "9144";
        callCode =callPrefix+"*"+"991"+shortCode1+shortCode2;//"127030358504100012345897";//
       // callCode = callPrefix+"*"+"254798997948";//callPrefix+"*"+shortCode1+""+shortCode2;//callPrefix+"*"+"254798997948";//
        Log.e("ussdtag",callCode);
        pdialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        SIM_map = new HashMap<>();
        simcardNames = new ArrayList<>();
        pdialog.show();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ussdResponseCallback = new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    //if our request is successful then we get response here

                    Toast.makeText(USSDActivity.this, response, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                       }
            };
            if (ActivityCompat.checkSelfPermission(USSDActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},REQ_CODE);

            }
        }else {
            dailNumber(callCode);
        }


        if (passedCode.contentEquals("2")){
            dailNumber(callCode); //"605*214*1234*1244"
        }else {
            dialUssdToGetPhoneNumber("*"+callCode+"#", 1);//*605*214*1234*1244#//*100*0*3# //"*"+callCode+"#"
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sims_details() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //request permission if its not granted already
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},REQ_CODE);
        }
        List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();

        for(SubscriptionInfo subscriptionInfo:subscriptionInfos){

            SIM_map.put(subscriptionInfo.getCarrierName().toString(),subscriptionInfo.getSubscriptionId());
            simcardNames.add(subscriptionInfo.getCarrierName().toString());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sims_details();
            //calling function again once we get permissions granted
        }
    }
    private void dailNumber(String code) {
        pdialog.cancel();
        String ussdCode = "*" + code + Uri.encode("#");
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));
    }

    public void dialUssdToGetPhoneNumber(String ussdCode, int sim) {

        if (ussdCode.equalsIgnoreCase("")) return;
   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 234);
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            TelephonyManager manager2 = manager.createForSubscriptionId(2);

            TelephonyManager managerMain = (sim == 0) ? manager : manager2;

            managerMain.sendUssdRequest(ussdCode, new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);
                    pdialog.cancel();
                    Log.e("TAG", "onReceiveUssdResponse:  Ussd Response = " + response.toString().trim() );
                    TextView tx = findViewById(R.id.tvUssd);
                    tx.setText(response.toString().trim());
                  //  navigateToDestinationFragment(response.toString());
                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                    pdialog.cancel();
                    dailNumber(callCode);
                    Log.e("TAG", "onReceiveUssdResponseFailed: " + "" + failureCode + request);
                }
            }, new Handler());
        }else {
            dailNumber("*1000#");
        }

    }


}