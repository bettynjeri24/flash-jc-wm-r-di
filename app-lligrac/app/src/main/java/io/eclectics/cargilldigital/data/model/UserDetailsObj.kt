package io.eclectics.cargilldigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import io.eclectics.cargill.utils.NetworkUtility

class UserDetailsObj
    (
    @SerializedName("firstName") val firstName:String?,
    @SerializedName("lastName") val  lastName:String?,
    @SerializedName("userId") val  userId:String?,
    @SerializedName("userIndex") val  userIndex:Int?,
    @SerializedName("cooperativeIndex") val cooperativeIndex:String?,
    @SerializedName("role") val  role:String?,
    @SerializedName("providedUserId") val  providedUserId:String?,
    @SerializedName("cooperativeId") val  cooperativeId:String?,
    @SerializedName("emailAddress") val  emailAddress:String?,
    @SerializedName("phoneNumber") val  phoneNumber:String?,
    @SerializedName("section") var  section:JsonObject?,
    @SerializedName("bankAccount") var bankAccount:JsonObject?,
    @SerializedName("region") val  region:String?,
    @SerializedName("walletBalance") val  walletBalance: String,
    @SerializedName("transactions") var transactions: JsonArray?,
    @SerializedName("trials") val trials:String?,
    @SerializedName("token") val token:String?,
    @SerializedName("locked") val locked:String?
){
    fun getTransactionlist(): JsonArray {

        /*val list: List<Employee> = mapper.readValue(
            jsonString,
            TypeFactory.defaultInstance().constructCollectionType(
                MutableList::class.java,
                Employee::class.java
            )
        )*/
       // var array = JSONObject(transactions.toString())
        return transactions!!
    }
    fun getSection():Section{
        var section:Section = NetworkUtility.jsonResponse(section.toString())
        return section
    }
    }

/**
 * I need to create object for other profile
 * needs remodification to have section as nullable
 */

class OtherUserObj
    (
    @SerializedName("firstName") val firstName:String,
    @SerializedName("lastName") val  lastName:String,
    @SerializedName("userId") val  userId:String,
    @SerializedName("userIndex") val  userIndex:Int,
    @SerializedName("role") val  role:String,
    @SerializedName("providedUserId") val  providedUserId:String,
    @SerializedName("cooperativeId") val  cooperativeId:String,
    @SerializedName("emailAddress") val  emailAddress:String,
    @SerializedName("phoneNumber") val  phoneNumber:String,
   // @SerializedName("region") val  region:String?,
    @SerializedName("walletBalance") val  walletBalance: String,
    @SerializedName("transactions") var transactions: JsonArray?,
    @SerializedName("trials") val trials:String?,
    @SerializedName("token") val token:String,
    @SerializedName("locked") val locked:String?
){
    fun getTransactionlist(): JsonArray {

        /*val list: List<Employee> = mapper.readValue(
            jsonString,
            TypeFactory.defaultInstance().constructCollectionType(
                MutableList::class.java,
                Employee::class.java
            )
        )*/
        // var array = JSONObject(transactions.toString())
        return transactions!!
    }
}
@Entity(tableName = "userdata")
class UserLogginData(
    @SerializedName("firstName") val firstName:String,
    @SerializedName("lastName") val  lastName:String,
    @SerializedName("userId") val  userId:String,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("userIndex") val  userIndex:Int,
    @SerializedName("cooperativeIndex") val cooperativeIndex:String,
    @SerializedName("role") val  role:String,
    @SerializedName("providedUserId") val  providedUserId:String,
    @SerializedName("cooperativeId") val  cooperativeId:String,
    @SerializedName("emailAddress") val  emailAddress:String?,
    @SerializedName("phoneNumber") val  phoneNumber:String,
    @SerializedName("bankAccount") var bankAccount:String?,
    @SerializedName("section") var  section:String?,
    @SerializedName("region") val  region:String?,
    @SerializedName("walletBalance") val  walletBalance: String,
    @SerializedName("transactions") var transactions: String?,

)
class Section(
    @SerializedName("id") val  id:Int,
    @SerializedName("sectionName") val  sectionName:String,
    @SerializedName("sectionLat") val  sectionLat:String,
    @SerializedName("sectionLong") val  sectionLong: String
)
data class CoopBank(
    @SerializedName("idx") val  id:Int,
    @SerializedName("entityId") val  entityId:String,
    @SerializedName("accountName") val  accountName:String,
    @SerializedName("accountNumber") val  accountNumber: String,
    @SerializedName("bankId") val  bankId:Int
)
