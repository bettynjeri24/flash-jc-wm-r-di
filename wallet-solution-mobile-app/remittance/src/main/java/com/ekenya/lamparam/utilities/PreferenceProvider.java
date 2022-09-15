package com.ekenya.lamparam.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences file class
 */
public class PreferenceProvider {

    private String ONBOARDING_DATA = "onboarding";
    private SharedPreferences preference;
    private String USER_NAME = "userName";

    public PreferenceProvider(Context context) {
        preference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void onboardUser(){
        preference.edit().putBoolean(ONBOARDING_DATA, true).apply();
    }

    public boolean userOnboarded(){
        return preference.getBoolean(ONBOARDING_DATA, false);
    }

    public void setUserName(String name){
        preference.edit().putString(USER_NAME, name).apply();
    }

    public String getUserName(){
        return preference.getString(USER_NAME, "Lennox Brown");
    }

}