package com.ekenya.rnd.dashboard.datadashboard.remote;


import static com.ekenya.rnd.common.Constants.BASE_URL_CBG_DEMO;

public class    APIUtils {
    private APIUtils() {}

    public static APIService getAPIService() {

        return RetrofitClient.Companion.getClient(BASE_URL_CBG_DEMO).create(APIService.class);

    }
    public static APIService getAPIServiceMock() {

        return RetrofitClient.Companion.retrofitMockInstance().create(APIService.class);

    }

}
