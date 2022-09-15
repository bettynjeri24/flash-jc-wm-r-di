package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val `data`: LoginData,
    @SerializedName("message")
    val message: String, // Login Success
    @SerializedName("status")
    val status: Int // 1
)
data class LoginData(
    @SerializedName("changePassword")
    val changePassword: Boolean, // false
    @SerializedName("is_first_login")
    val isFirstLogin: Boolean, // false
    @SerializedName("last_login")
    val lastLogin: String, // 2021-08-12 10:29:01
    @SerializedName("token")
    val token: String, // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImp0aSI6ImQ2NDkyODU2YTRhNGMxMDQ2ODFkZTRmM2EyZTBmOTg5MSJ9.eyJuYW1lIjoiTW9zZXMgT2Nob2xhIiwidXNlcm5hbWUiOiJtb3NlcyIsImlzcyI6Imh0dHBzOlwvXC90ZXN0LXBvcnRhbC5la2VueWEuY28ua2UiLCJhdWQiOiJodHRwczpcL1wvdGVzdC1wb3J0YWwuZWtlbnlhLmNvLmtlIiwianRpIjoiZDY0OTI4NTZhNGE0YzEwNDY4MWRlNGYzYTJlMGY5ODkxIiwiaWF0IjoxNjI4NzUzMzQxLCJleHAiOjE2Mjg4Mzk3NDEsInVpZCI6NTZ9.vOTWRKC4HKcXCwe0exmlnXQHr2aMlBdzRKFxR2bTZ_w
    @SerializedName("user")
    val user: User
)
data class User(
    @SerializedName("email")
    val email: String, // ocholamoses@gmail.com
    @SerializedName("name")
    val name: String, // Moses Ochola
    @SerializedName("org_id")
    val orgId: Int, // 12
    @SerializedName("phone")
    val phone: String // 0718194920
)