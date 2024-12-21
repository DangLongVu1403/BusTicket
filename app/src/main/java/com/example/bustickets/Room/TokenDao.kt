package com.example.bustickets.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(tokenEntity: TokenEntity)

    @Query("SELECT * FROM tokens LIMIT 1")
    suspend fun getToken(): TokenEntity?

    @Query("DELETE FROM tokens")
    suspend fun deleteToken()
}

