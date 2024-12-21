package com.example.bustickets.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tokens")
data class TokenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accessToken: String
)