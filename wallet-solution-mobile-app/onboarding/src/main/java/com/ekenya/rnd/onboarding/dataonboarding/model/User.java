package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("full_names")
    @Expose
    private String fullNames;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email")
    @Expose
    private String email;

    public User(String fullNames, String mobileNo, String email) {
        this.fullNames = fullNames;
        this.mobileNo = mobileNo;
        this.email = email;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
