package com.example.bustickets.Model

data class ResponseData(
    val user: User,
    val token: String
)

data class ResponseRegisterData(
    val user: User
)

data class ResponseProfile(
    val message: String,
    val data: ResponseRegisterData
)

data class LoginResponse(
    val message: String?,
    val data: ResponseData
)

data class RegisterResponse(
    val message: String?,
    val data: ResponseRegisterData
)

data class ResponseStation(
    val message: String,
    val data: DataStation
)

data class DataStation(
    val stations: List<Station>
)

data class TripByIdResponse(
    val message: String,
    val data: Trip
)

data class BookTicketResponse(
    val message: String,
    val data: TicketData
)

data class TripResponse(
    val message: String,
    val data: TripData
)

data class TripData(
    val trips: List<Trip>,
    val size: Int
)

data class informationTicket(
    val paymentStatus: String,
    val _id: String,
    val user: User,
    val trip: Trip,
    val seatNumber: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val v: String
)

data class ticketResponse(
    val message: String,
    val data: List<informationTicket>
)

data class PaymentResponse(
    val status: String,
    val transactionId: String,
    val paymentUrl: String
)

class Response {
}