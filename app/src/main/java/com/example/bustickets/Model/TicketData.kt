package com.example.bustickets.Model

data class TicketData(
    val id: String,
    val startPoint: String,
    val endPoint: String,
    val bookingDate: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val status: Int
)
