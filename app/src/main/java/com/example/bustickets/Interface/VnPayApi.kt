package com.example.bustickets.Interface

import com.example.bustickets.Model.PaymentRequest
import com.example.bustickets.Model.PaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VnPayApi {
    @POST("vnpay_api_endpoint")  // Điền endpoint API của VNPAY
    fun createPayment(@Body request: PaymentRequest): Response<PaymentResponse>
}
