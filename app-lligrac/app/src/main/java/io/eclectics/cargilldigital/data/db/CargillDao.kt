package io.eclectics.cargilldigital.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.eclectics.cargilldigital.data.model.*
import io.eclectics.cargill.model.FarmerModelObj

@Dao
interface CargillDao {

    //FARMER PROFILE
    /**
     * Save and view beneficiary account list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    @JvmSuppressWildcards
    fun insertBeneficiaryAcc(channel:List<FarmerAccount.BeneficiaryAccObj>)
    @Query("DELETE FROM beneficiryacclist")
    fun deleteAllBeneficiaryAcc()
    @Query("SELECT * FROM beneficiryacclist")
    fun getBeneficiaryAccList(): LiveData<List<FarmerAccount.BeneficiaryAccObj>>
    /**
     * save farmer channel object list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    @JvmSuppressWildcards
    fun insertChannelList(channel:List<SendMoney.ChannelListObj>)
    @Query("DELETE FROM channellist")
    fun deleteChannelList()
    @Query("SELECT * FROM channellist")
    fun getChannelList(): LiveData<List<SendMoney.ChannelListObj>>

    // BUYER PROFILE
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    @JvmSuppressWildcards
    fun insertFarmerList(userdata:List<FarmerModelObj>)
    @Query("DELETE FROM farmermodelobj")
    fun deleteFarmerList()
    @Query("SELECT * FROM farmermodelobj")
    fun getFarmersList(): LiveData<List<FarmerModelObj>>

    /*
    buyer pending payment list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertPendingPayments(pendingPayments: List<BuyerPendingPayment>)
    @Query("DELETE FROM buyerpendingpayment")
    fun deleteAllPendingPayment()
    @Query("SELECT * FROM buyerpendingpayment")
    fun getAllPendingPayments():LiveData<List<BuyerPendingPayment>>

    /**
     * save and querry buyer funds request history
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    @JvmSuppressWildcards
    fun insertBuyerFundsReqList(userdata:List<CoopFundsRequestList>)
    @Query("DELETE FROM coopFundsRequestList")
    fun deleteFundsReqList()
    @Query("SELECT * FROM coopFundsRequestList")
    fun getBuyerFundsReqList(): LiveData<List<CoopFundsRequestList>>


    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun insertUserdata(userdata: UserLogginData)

    @Query("SELECT * FROM userdata")
    fun getUserDetails(): LiveData<UserLogginData>
    @Query("SELECT *  FROM userdata WHERE userId LIKE '%1%'  LIMIT 1") //= 'Road Side Sale'
    fun selectUser():LiveData<UserLogginData>

    /**
     * Cooperative buyerlist
     */
    // BUYER PROFILE
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    @JvmSuppressWildcards
    fun insertCoopBuyerList(userdata:List<CoopBuyer.BuyerList>)
    @Query("DELETE FROM coopbuyerlist")
    fun deleteCoopBuyerList()
    @Query("SELECT * FROM coopbuyerlist")
    fun getCoopBuyerList(): LiveData<List<CoopBuyer.BuyerList>>

    /**
     *Update db balance code
     * TODO QUERY1 WAS TO DELETE A SALE WITH ID
     * //@Query("DELETE  FROM directsale WHERE saleId = :saleIdsent")
     */
    @Query("UPDATE userdata SET walletBalance = :territoryCode")
    fun updateTerritoryCode(territoryCode:String)
   /* @Query("UPDATE directsale SET uploadStatus='1' WHERE saleId = :saleIdsent")
    fun deleteSingleSale(saleIdsent:Int)
    @Query("DELETE FROM discounttbl")
    fun deleteSaleDiscount()
    *//**
     * SAVE TRACK PRODUCTS OFFLINE
     *//*
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun insertSaleDiscount(model:List<DiscountModel>)
*/


}