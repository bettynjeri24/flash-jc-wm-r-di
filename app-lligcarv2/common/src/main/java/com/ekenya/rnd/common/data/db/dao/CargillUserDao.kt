package com.ekenya.rnd.common.data.db.dao

import androidx.room.*
import com.ekenya.rnd.common.data.db.entity.CARGILL_CURRENT_USER_ID
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
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
