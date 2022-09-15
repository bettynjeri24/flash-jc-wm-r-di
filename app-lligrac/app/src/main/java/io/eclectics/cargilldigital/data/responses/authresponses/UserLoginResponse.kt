package io.eclectics.cargilldigital.data.responses.authresponses

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CARGILL_CURRENT_USER_ID = 0

data class CargillUserLoginResponse(
    @SerializedName("statusDescription")
    val statusDescription: String? = "",
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("data")
    val loginResponseData: CargillUserLoginResponseData
)

@Entity(tableName = "userLoginResponseData")
data class CargillUserLoginResponseData(
    @SerializedName("userIndex")
    val userIndex: Int?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("providedUserId")
    val providedUserId: String?,
    @SerializedName("cooperativeIndex")
    val cooperativeIndex: String?,
    @SerializedName("cooperativeId")
    val cooperativeId: String?,
    @SerializedName("emailAddress")
    val emailAddress: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("section")
    @Embedded(prefix = "section_")
    val section: CargillUserSectionData?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("walletBalance")
    val walletBalance: String?,
    @SerializedName("transactions")
    val transactions: List<CargillUserTransactionData>?,
    @SerializedName("channels")
    val channels: List<CargillUserChannelData>?,
    @SerializedName("bankAccount")
    val bankAccount: String?,
    @SerializedName("logos")
    val logos: List<CargillUserLogoData?>?,
    @SerializedName("token")
    val token: String?
) {
    @PrimaryKey(autoGenerate = false)
    var uid = CARGILL_CURRENT_USER_ID
}

data class CargillUserSectionData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("sectionName")
    val sectionName: String?,
    @SerializedName("sectionLat")
    val sectionLat: String?,
    @SerializedName("sectionLong")
    val sectionLong: String?
)

data class CargillUserTransactionData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("datecreated")
    val datecreated: String?,
    @SerializedName("walletTransactionType")
    val walletTransactionType: String?,
    @SerializedName("sendorPhoneNumber")
    val sendorPhoneNumber: String?,
    @SerializedName("recipientPhoneNumber")
    val recipientPhoneNumber: String?,
    @SerializedName("transMode")
    val transMode: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("reasons")
    val reasons: String?,
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("recipientName")
    val recipientName: String?,
    @SerializedName("sendorName")
    val sendorName: String?,
    @SerializedName("transactionCode")
    val transactionCode: String?
) {
    companion object {
        fun getDummyUserTransactionData(): List<CargillUserTransactionData> {
            return listOf<CargillUserTransactionData>(
                CargillUserTransactionData(
                    id = 1,
                    datecreated = "datecreated",
                    walletTransactionType = "walletTransactionType",
                    sendorPhoneNumber = "sendorPhoneNumber",
                    recipientPhoneNumber = "recipientPhoneNumber",
                    transMode = "recipientPhoneNumber",
                    status = true,
                    reasons = "reasons",
                    amount = "amount",
                    recipientName = "recipientName",
                    sendorName = "sendorName",
                    transactionCode = "transactionCode"
                )
            )
        }
    }
}

data class CargillUserChannelData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("channelName")
    val channelName: String?,
    @SerializedName("abbreviation")
    val abbreviation: String?,
    @SerializedName("type")
    val type: String?
)

data class CargillUserLogoData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("logoname")
    val logoname: String?,
    @SerializedName("path")
    val path: String?
)
