package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegistrationResponse {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("txntimestamp")
    @Expose
    private Object txntimestamp;
    @SerializedName("esb_ref")
    @Expose
    private Integer esbRef;
    @SerializedName("xref")
    @Expose
    private Object xref;
}
