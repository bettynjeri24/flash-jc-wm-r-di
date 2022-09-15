package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("card_number")
    @Expose
    private String cardNumber;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;

    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("token")
    @Expose
    private String token;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
