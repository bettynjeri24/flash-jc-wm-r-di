package co.ekenya.pepsidistributor.networkCallback.respcallback

import com.ekenya.lamparam.utilities.NetworkUtility
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.json.JSONArray

class GeneralResponce
    (
    @SerializedName("statusCode") var statusCode: String,
    @SerializedName("statusDescription") var statusMsg:String,
    @SerializedName("data") val data: dataClass,
    @SerializedName("roles") var roles: JsonArray,
    @SerializedName("profiles") var profiles: JsonArray
) {

    inner class ApiResponse(
        @SerializedName("response_message") val message: String,
        @SerializedName("response_code") val code: String
       // @SerializedName("login_trials") val trials: Int
        //login_trials
    )

    inner class dataClass(
            @SerializedName("response") val response: JsonElement,
            @SerializedName("transaction_details") val transaction_details: JsonElement
    )
    fun getProfile():String {

        return ""
    }
    /*fun getApiResponse(): ApiResponse {
        return response
    }*/

   /* fun listData(): Any {
        return data
    }
*/
    fun getResponseDataObj(): JsonElement {
        return data.response
    }
    fun getResponseCode():String{
   var resp:ApiResponse = NetworkUtility.jsonResponse(data.response.asString)
        return  resp.code
    }
}
/* fun <T> getResponseData(respdata: Class<T>): T{
wallet account TA2459275000029
     return Gson().fromJson(JSONObject(Gson().toJson(data)).toString(),respdata)
 }
 fun <T> getData(returnType: Class<T>?): T {
     val dataObject: Any? = null
     var jsonObject: JSONObject? = null
     try {
         jsonObject = JSONObject(Gson().toJson(data))
     } catch (ex: Exception) {
     }
     try {
         jsonObject = JSONObject(data.toString())
     } catch (ex: Exception) {
     }
     return Gson().fromJson(jsonObject.toString(), returnType)
 }*/
