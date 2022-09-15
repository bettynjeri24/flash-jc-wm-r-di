package com.ekenya.rnd.dashboard.datadashboard.remote;


import com.ekenya.rnd.common.data.model.MainDataObject;
import com.ekenya.rnd.dashboard.datadashboard.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("MobileWebService")
    Call<LoginResponse> login(@Body MainDataObject user);


}