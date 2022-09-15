package io.eclectics.cargilldigital.data.model

import com.google.gson.annotations.SerializedName

class LookupModel
    (
    @SerializedName("message") var message:String,
    @SerializedName("phonenumber")var phonenumber :String
            )