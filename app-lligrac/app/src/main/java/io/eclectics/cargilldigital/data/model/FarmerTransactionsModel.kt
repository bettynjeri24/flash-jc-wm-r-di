package io.eclectics.cargill.model

data class FarmerTransactionsModel(
    var lastCollectionDate: String = "",
    var weight: String = "",
    var grade: String = "",
    var rating: Int = 0,
)
