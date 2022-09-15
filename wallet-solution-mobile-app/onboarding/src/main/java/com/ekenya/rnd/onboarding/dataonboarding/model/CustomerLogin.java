package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerLogin{
        @SerializedName("customer_no")
        @Expose
        private String customerNo;
        @SerializedName("pin")
        @Expose
        private String pin;
        @SerializedName("geolocation")
        @Expose
        private String geolocation;
        @SerializedName("user_agent_version")
        @Expose
        private String userAgentVersion;
        @SerializedName("user_agent")
        @Expose
        private String userAgent;

    public CustomerLogin(String customerNo, String pin, String geolocation, String userAgentVersion, String userAgent) {
        this.customerNo = customerNo;
        this.pin = pin;
        this.geolocation = geolocation;
        this.userAgentVersion = userAgentVersion;
        this.userAgent = userAgent;
    }

    public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
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
        }}
