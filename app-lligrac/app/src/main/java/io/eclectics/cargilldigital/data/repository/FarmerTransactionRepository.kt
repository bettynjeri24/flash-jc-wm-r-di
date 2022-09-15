package io.eclectics.cargill.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.eclectics.cargill.model.FarmerTransactionsModel
import io.eclectics.cargill.utils.NetworkUtility

object FarmerTransactionRepository {

    private val transactions= listOf(
        FarmerTransactionsModel("26-02-2021","${NetworkUtility().cashFormatter("50000")}","6",4),
        FarmerTransactionsModel("20-08-2020","${NetworkUtility().cashFormatter("130000")}","7",3),
        FarmerTransactionsModel("19-03-2020","${NetworkUtility().cashFormatter("12000")}","3",2),
        FarmerTransactionsModel("07-07-2019","${NetworkUtility().cashFormatter("23000")}","1",4),
        FarmerTransactionsModel("23-02-2019","${NetworkUtility().cashFormatter("12000")}","6",3),
    )

    private val _transactionsList = MutableLiveData<List<FarmerTransactionsModel>>()
    val transactionsList: LiveData<List<FarmerTransactionsModel>>
        get() = _transactionsList

    init {
        _transactionsList.value = transactions
    }
}