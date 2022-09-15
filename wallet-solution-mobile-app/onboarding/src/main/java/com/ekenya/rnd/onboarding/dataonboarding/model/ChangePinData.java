package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePinData {

    public ChangePinData(String serviceName, String phoneNumber, String password, String otpToken, String grantType, String geolocation, String userAgentVersion, String userAgent) {
        this.serviceName = serviceName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.otpToken = otpToken;
        this.grantType = grantType;
        this.geolocation = geolocation;
        this.userAgentVersion = userAgentVersion;
        this.userAgent = userAgent;
    }

    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("otp_token")
    @Expose
    private String otpToken;
    @SerializedName("grant_type")
    @Expose
    private String grantType;
    @SerializedName("geolocation")
    @Expose
    private String geolocation;
    @SerializedName("user_agent_version")
    @Expose
    private String userAgentVersion;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtpToken() {
        return otpToken;
}}
