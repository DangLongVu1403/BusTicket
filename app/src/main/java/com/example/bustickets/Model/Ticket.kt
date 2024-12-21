package com.example.bustickets.Model

data class Ticket(
    val _id: String,
    val user: User,
    val trip: Trip,
    val seatNumber: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
