package com.ekenya.rnd.dashboard.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.ekenya.rnd.onboarding.R;
import com.ekenya.rnd.onboarding.dataonboarding.Remote.APIService;
import com.ekenya.rnd.onboarding.dataonboarding.Remote.APIUtils;
import com.ekenya.rnd.common.data.model.MainDataObject;
import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse2;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletRepository {
//
    private Context context;
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;


    private APIService mAPIService;


    private Thread mThread;


    public WalletRepository(Context context) {
        this.context = context;
        mAPIService = APIUtils.getAPIService();
        /*mainDb = MainDb.getDatabase(context);
        mobiAfyaDao=mainDb.membersDao();*/

        // Stop the thread if its initialized
        // If the thread is not working interrupt will do nothing
        // If its working it stops the previous work and starts the
        // new runnable
        if (mThread != null) {
            mThread.interrupt();
        }
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    }


    public void registerUser(MainDataObject userData, View v) {


        mAPIService.registerUser(userData).enqueue(new Callback<RegistrationResponse2>() {
            @Override
            public void onResponse(Call<RegistrationResponse2> call, Response<RegistrationResponse2> response) {
                Log.d( "onsuccesful Response: ", String.valueOf(response));

                if (response.isSuccessful() /*& response.body().getData() != null*/){
                    if (response.body().getData().getResponse().getResponseCode().toString().equals("00")){
                        v.setVisibility(View.GONE);

                         Navigation.findNavController(v).navigate(R.id.action_finalDetailsVerificationFragment_to_otpVerificationFragment);

                    }
                    else{
                        v.setVisibility(View.GONE);
                        Toast.makeText(context, "Some Error message occured", Toast.LENGTH_SHORT).show();

                    }
                    //update viewmodel


            }else{
                    Toast.makeText(context, "Some Error message occured", Toast.LENGTH_SHORT).show();
                }}

            @Override
            public void onFailure(Call<RegistrationResponse2> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();

                Log.d( "onsuccesful Response: ", String.valueOf(t.toString()));

            }
            //}




    });









}
    public void changePassword(MainDataObject changepindata){
    mAPIService.changePassword(changepindata).enqueue(new Callback<RegistrationResponse2>() {
        @Override
        public void onResponse(Call<RegistrationResponse2> call, Response<RegistrationResponse2> response) {

        }

        @Override
        public void onFailure(Call<RegistrationResponse2> call, Throwable t) {

        }
    });
    }}
