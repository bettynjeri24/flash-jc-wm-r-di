package com.ekenya.rnd.cargillbuyer.data.responses

data class FarmerDetailsRes(
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val location: String?,
    val farmSize: String?,
    val lastCollectionDate: String?,
    val totalWeightCollected: String?,
    val lastWeightCollected: String?,
    val dateJoined: String?,
    val emailAddress: String?,
    val produceQuality: Int
) {
    companion object {
        fun getFarmerData(): FarmerDetailsRes = FarmerDetailsRes(
            firstName = "Farmer Name",
            lastName = "LastName",
            phoneNumber = "071234567",
            location = "Basam",
            farmSize = "24 Acres",
            lastCollectionDate = "",
            totalWeightCollected = "34Kg",
            lastWeightCollected = "50KG",
            dateJoined = "2022-01-12",
            emailAddress = "d@gmail.com",
            produceQuality = 0
        )

        fun getFarmerListData(): ArrayList<FarmerDetailsRes> = arrayListOf(
            FarmerDetailsRes(
                firstName = "Farmer Name",
                lastName = "LastName",
                phoneNumber = "071234567",
                location = "Basam",
                farmSize = "24 Acres",
                lastCollectionDate = "",
                totalWeightCollected = "34Kg",
                lastWeightCollected = "50KG",
                dateJoined = "2022-01-12",
                emailAddress = "d@gmail.com",
                produceQuality = 0
            ),
            FarmerDetailsRes(
                firstName = "Farmer Name",
                lastName = "LastName",
                phoneNumber = "071234567",
                location = "Basam",
                farmSize = "24 Acres",
                lastCollectionDate = "",
                totalWeightCollected = "34Kg",
                lastWeightCollected = "50KG",
                dateJoined = "2022-01-12",
                emailAddress = "d@gmail.com",
                produceQuality = 0
            )
        )
    }
}
