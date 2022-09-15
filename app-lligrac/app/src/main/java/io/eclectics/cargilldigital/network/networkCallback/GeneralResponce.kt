package io.eclectics.cargill.network.networkCallback

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import io.eclectics.cargill.utils.NetworkUtility

class GeneralResponce
    (
    @SerializedName("statusCode") var statusCode: Int,
    @SerializedName("statusDescription") var statusMsg:String,
    @SerializedName("data") val data: JsonElement
    //@SerializedName("roles") var roles: JsonArray,
    //@SerializedName("profiles") var profiles: JsonArray
) {


    fun getResponseDataObj(): JsonElement {
        return data
    }

    fun getErrorMsg():String{
        var errorObj:ErrorModel = NetworkUtility.jsonResponse(data.toString())
        return errorObj.message
    }
}
class ErrorModel(
    @SerializedName("message") var message:String,
)
/*

    inner class ApiResponse(
        @SerializedName("status") val status: String,
        @SerializedName("message") val message: String
    )

    fun getProfile():String {

        return ""
    }
 */

