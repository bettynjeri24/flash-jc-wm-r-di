package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyCustomerData {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("national_id")
    @Expose
    private String nationalId;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("geolocation")
    @Expose
    private String geolocation;
    @SerializedName("user_agent_version")
    @Expose
    private String userAgentVersion;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;

    public VerifyCustomerData(String userId, String firstName, String lastName, String middleName, String nationalId, String dob, String gender, String geolocation, String userAgentVersion, String userAgent) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.nationalId = nationalId;
        this.dob = dob;
        this.gender = gender;
        this.geolocation = geolocation;
        this.userAgentVersion = userAgentVersion;
        this.userAgent = userAgent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getUserAgentVersion() {
        return userAgentVersion;
    }

    public void setUserAgentVersion(String userAgentVersion) {
        this.userAgentVersion = userAgentVersion;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
