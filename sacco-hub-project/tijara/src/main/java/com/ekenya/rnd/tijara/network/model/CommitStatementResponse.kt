package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class CommitStatementResponse(
    @SerializedName("data")
    val `data`: CommitStatementData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class CommitStatementData(
        @SerializedName("statementUrl")
        val statementUrl: String, // https://test-portal.ekenya.co.ke/tijara-api/uploads\organizations\013\statements\110\account-statement.pdf
        @SerializedName("transactionCode")
        val transactionCode: String // WIME0PNJ
    )
