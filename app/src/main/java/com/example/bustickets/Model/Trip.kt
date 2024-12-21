package com.example.bustickets.Model

data class Trip(
    val _id: String,
    val bus: String,
    val startLocation: String?,
    val endLocation: String?,
    val departureTime: String,
    val arriveTime: String,
    val price: Int,
    val availableSeats: Int,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
)
