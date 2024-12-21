package com.example.bustickets.Model

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String,val email: String, val password: String,val phone: String, val role: String )
data class BookTicketRequest(val tripId: String, val seatNumber: Int)
data class CancelTicketRequest(val ticketId: String, val userId: String)
data class EditProfileRequest(val userId: String, val user: RegisterRequest)
data class PaymentRequest(val amount: Double, val bankCode: String, val orderId: String, val orderInfo: String, val returnUrl: String)

class Request(
)
