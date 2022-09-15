package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtp {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("otp")
    @Expose
    private String otp;

    public VerifyOtp(String userId, String otp) {
        this.userId = userId;
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
