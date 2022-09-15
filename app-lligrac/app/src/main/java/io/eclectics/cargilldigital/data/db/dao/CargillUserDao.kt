package io.eclectics.cargilldigital.data.db.dao

import androidx.room.*
import io.eclectics.cargilldigital.data.responses.authresponses.CARGILL_CURRENT_USER_ID
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLoginResponseData
import kotlinx.coroutines.flow.Flow

@Dao
interface CargillUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(user: CargillUserLoginResponseData)

    @Query("SELECT * FROM userLoginResponseData WHERE uid=$CARGILL_CURRENT_USER_ID")
    fun getUser(): Flow<CargillUserLoginResponseData>

    @Delete
    fun delete(user: CargillUserLoginResponseData)
}
