package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class SavingAccounts(
    @SerializedName("status")
    val status: Int, // 200
    @SerializedName("data")
    val `data`: ArrayList<SavingAccountData>
)
    data class SavingAccountData(
        @SerializedName("accountId")
        val accountId: Int, // 355
        @SerializedName("accountName")
        val accountName: String, // ALPHA DEPOSIT
        @SerializedName("accountNo")
        val accountNo: String, // BS002021/02
        @SerializedName("allowCredit")
        val allowCredit: Int, // 0
        @SerializedName("allowDebit")
        val allowDebit: Int, // 0
        @SerializedName("isTransactional")
        val isTransactional: Int, // 0
        @SerializedName("productId")
        val productId: Int // 34
    ){
        override fun toString(): String {
            return accountName
        }
    }
