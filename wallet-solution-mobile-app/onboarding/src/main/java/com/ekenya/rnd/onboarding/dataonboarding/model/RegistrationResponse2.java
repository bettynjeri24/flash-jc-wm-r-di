package com.ekenya.rnd.onboarding.dataonboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse2 {


        @SerializedName("data")
        @Expose
        private UserResponseData data;
        @SerializedName("txntimestamp")
        @Expose
        private Object txntimestamp;
        @SerializedName("esb_ref")
        @Expose
        private Integer esbRef;
        @SerializedName("xref")
        @Expose
        private Object xref;

        public UserResponseData getData() {
            return data;
        }

        public void setData(UserResponseData data) {
            this.data = data;
        }

        public Object getTxntimestamp() {
            return txntimestamp;
        }

        public void setTxntimestamp(Object txntimestamp) {
            this.txntimestamp = txntimestamp;
        }

        public Integer getEsbRef() {
            return esbRef;
        }

        public void setEsbRef(Integer esbRef) {
            this.esbRef = esbRef;
        }

        public Object getXref() {
            return xref;
        }

        public void setXref(Object xref) {
            this.xref = xref;
        }}


