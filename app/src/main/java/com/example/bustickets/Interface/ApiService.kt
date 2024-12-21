package com.example.bustickets.Interface

import com.example.bustickets.Model.BookTicketRequest
import com.example.bustickets.Model.BookTicketResponse
import com.example.bustickets.Model.CancelTicketRequest
import com.example.bustickets.Model.EditProfileRequest
import com.example.bustickets.Model.LoginRequest
import com.example.bustickets.Model.LoginResponse
import com.example.bustickets.Model.RegisterRequest
import com.example.bustickets.Model.RegisterResponse
import com.example.bustickets.Model.ResponseProfile
import com.example.bustickets.Model.ResponseStation
import com.example.bustickets.Model.TicketData
import com.example.bustickets.Model.TripByIdResponse
import com.example.bustickets.Model.TripResponse
import com.example.bustickets.Model.ticketResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/api/users/login")
    fun loginUser(@Body requestBody: LoginRequest): Call<LoginResponse>
    @POST("/api/users/register")
    fun registerUser(@Body requestBody: RegisterRequest): Call<RegisterResponse>

    @GET("/api/users/profile/")
    fun getProfile(@Header("Authorization") token: String): Call<ResponseProfile>

    @PUT("/api/users/profile/")
    fun editProfile(@Header("Authorization") token: String, @Body request: EditProfileRequest): Call<ResponseProfile>

    @POST("/api/tickets/book")
    fun bookTicket(
        @Header("Authorization") token: String, // Bearer token
        @Body request: BookTicketRequest        // Request body
    ): Call<BookTicketResponse>

    // Lấy danh sách vé của người dùng
    @GET("/api/tickets")
    fun getUserTickets(@Header("Authorization") token: String): Call<ticketResponse>

    // Hủy vé
    @DELETE("/api/tickets/{id}")
    fun cancelTicket(@Header("Authorization") token: String, @Path("id") ticketId: String): Call<String>

    @GET("/api/station")
    fun getStation(): Call<ResponseStation>

    @GET("/api/trips")
    fun getTrips(
        @Query("startLocation") startLocation: String?,
        @Query("endLocation") endLocation: String?,
        @Query("time") time: String?
    ): Call<TripResponse>

    @GET("api/trips/{id}")
    fun getTripById(
        @Path("id") tripId: String
    ): Call<TripByIdResponse>
}