package com.ekenya.rnd.onboarding.dataonboarding.Remote;


import static com.ekenya.rnd.common.Constants.BASE_URL2;

public class    APIUtils {
    private APIUtils() {}

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL2).create(APIService.class);

    }
  /*  public static Api getAPIService2() {

        return RetrofitClient.getClient(BASE_URL).create(Api.class);

    }*/
}
