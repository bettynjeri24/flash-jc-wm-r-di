package com.ekenya.rnd.onboarding.dataonboarding.Remote;


import com.ekenya.rnd.common.data.model.MainDataObject;
import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse2;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {



    //getotp



    @POST("MobileWebService")
    Call<RegistrationResponse2> registerUser(@Body MainDataObject user);
    @POST("MobileWebService")

    Call<RegistrationResponse2> changePassword(@Body MainDataObject user);

   /* @POST("verifyOtp")
    Call<RegistrationResponse> verifyOtp(@Body VerifyOtp otp);*/

   /* @POST("verifyCustomers")
    Call<RegistrationResponse> verifyCustomerData(@Body VerifyCustomerData customerData);

    @POST("customerLogin")
    Call<RegistrationResponse> CustomerLogin(@Body CustomerLogin customerLoginData);
    @POST("resetPin")
    Call<RegistrationResponse> resetPin(@Body ResetPin resetPinData);

    @POST("verifyCustomers")
    Call<RegistrationResponse> verifyCustomers(@Body VerifyCustomerData verifyCustomerData);*/

}