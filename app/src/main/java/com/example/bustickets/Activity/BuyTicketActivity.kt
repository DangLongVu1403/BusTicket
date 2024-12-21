package com.example.bustickets.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bustickets.Model.BookTicketRequest
import com.example.bustickets.Model.BookTicketResponse
import com.example.bustickets.Model.Trip
import com.example.bustickets.Model.TripByIdResponse
import com.example.bustickets.Model.TripResponse
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.Room.ProcessAuth
import com.example.bustickets.databinding.ActivityBuyTicketBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class BuyTicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        if (id != null) {
            RetrofitInstance.apiService.getTripById(id)
                .enqueue(object : Callback<TripByIdResponse> {
                    override fun onResponse(call: Call<TripByIdResponse>, response: Response<TripByIdResponse>) {
                        if (response.isSuccessful) {
                                val trip = response.body()!!.data
                                binding.textViewStartPoint.text = trip.startLocation
                                binding.textViewEndPoint.text = trip.endLocation
                                binding.textViewTimeStart.text = formatToHourMinute(trip.departureTime)
                                binding.textViewTimeEnd.text = formatToHourMinute(trip.arriveTime)
                                binding.textViewPrice.text = "${trip.price} VND"
                        } else {
                            // Handle error response from the API
                            Toast.makeText(this@BuyTicketActivity, "Lỗi: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            Log.d("aaa", "Lỗi: ${response.errorBody()?.string()}")
                        }
                    }


                    override fun onFailure(call: Call<TripByIdResponse>, t: Throwable) {
                        Toast.makeText(this@BuyTicketActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.imgAgree.setOnClickListener {
            val request = BookTicketRequest(tripId = id.toString(), seatNumber = 2)
            val processAuth = ProcessAuth()
            lifecycleScope.launch {
                val token = processAuth.getUserToken(this@BuyTicketActivity)
                RetrofitInstance.apiService.bookTicket("Bearer $token", request)
                    .enqueue(object : Callback<BookTicketResponse> {
                        override fun onResponse(call: Call<BookTicketResponse>, response: Response<BookTicketResponse>) {
                            if (response.isSuccessful) {
                                val bookingResponse = response.body()
                                val intent = Intent( this@BuyTicketActivity, MainActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(applicationContext, "Success: ${bookingResponse?.message}", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<BookTicketResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

    }

    fun formatToHourMinute(input: String): String {
        return try {
            // Định dạng chuỗi đầu vào
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

            // Chuyển chuỗi sang ZonedDateTime
            val dateTime = ZonedDateTime.parse(input, inputFormatter)

            // Định dạng giờ: phút
            val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

            // Trả về chuỗi giờ: phút
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            "Invalid input format" // Trả về thông báo nếu chuỗi không hợp lệ
        }
    }
}