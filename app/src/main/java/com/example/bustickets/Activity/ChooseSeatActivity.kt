package com.example.bustickets.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bustickets.Adapter.SeatAdapter
import com.example.bustickets.Model.TripByIdResponse
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.databinding.ActivityChooseSeatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ChooseSeatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseSeatBinding
    private var selectedSeat: String? = null
    private var firstAdapter: SeatAdapter? = null
    private var secondAdapter: SeatAdapter? = null
    private var type = 20
    private var availableSeats = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseSeatBinding.inflate(layoutInflater)
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
                            binding.textViewDateStart.text = formatToDateMonthYear(trip.departureTime)
                            binding.textViewRoute.text = "${trip.startLocation} - ${trip.endLocation}"
                            binding.textViewTimeStart.text = formatToHourMinute(trip.departureTime)
                            binding.textViewTimeEnd.text = formatToHourMinute(trip.arriveTime)
                        } else {
                            // Handle error response from the API
                            Toast.makeText(this@ChooseSeatActivity, "Lỗi: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            Log.d("aaa", "Lỗi: ${response.errorBody()?.string()}")
                        }
                    }


                    override fun onFailure(call: Call<TripByIdResponse>, t: Throwable) {
                        Toast.makeText(this@ChooseSeatActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        if(type == 20) {
            val allPositions = (1..20).map { it.toString() }
            val firstRecyclerSeats = allPositions.filterIndexed { index, _ -> index % 4 < 2 }
            val secondRecyclerSeats = allPositions.filterIndexed { index, _ -> index % 4 >= 2 }
            firstAdapter = SeatAdapter(
                context = this,
                positions = firstRecyclerSeats,
                availableSeats = availableSeats
            ) { selected ->
                updateSelectedSeat(selected, firstAdapter, secondAdapter)
            }

            secondAdapter = SeatAdapter(
                context = this,
                positions = secondRecyclerSeats,
                availableSeats = availableSeats
            ) { selected ->
                updateSelectedSeat(selected, firstAdapter, secondAdapter)
            }
            binding.recyclerViewSeats1Floor.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewSeats1Floor.adapter = firstAdapter

            binding.recyclerViewSeats2Floor.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewSeats2Floor.adapter = secondAdapter
        }else if(type == 36){
            val allPositions = (1..36).map { it.toString() }
            val firstRecyclerSeats = allPositions.filterIndexed { index, _ -> index % 6 < 3 }
            val secondRecyclerSeats = allPositions.filterIndexed { index, _ -> index % 6 >= 3 }
            firstAdapter = SeatAdapter(
                context = this,
                positions = firstRecyclerSeats,
                availableSeats = availableSeats
            ) { selected ->
                updateSelectedSeat(selected, firstAdapter, secondAdapter)
            }

            secondAdapter = SeatAdapter(
                context = this,
                positions = secondRecyclerSeats,
                availableSeats = availableSeats
            ) { selected ->
                updateSelectedSeat(selected, firstAdapter, secondAdapter)
            }
            binding.recyclerViewSeats1Floor.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerViewSeats1Floor.adapter = firstAdapter

            binding.recyclerViewSeats2Floor.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerViewSeats2Floor.adapter = secondAdapter
        }else{
            val allPositions = (1..42).map { it.toString() }
            val firstRecyclerSeats = allPositions.filterIndexed { index, _ -> index % 6 < 3 }
            val secondRecyclerSeats = allPositions.filterIndexed { index, _ -> index % 6 >= 3 }
            firstAdapter = SeatAdapter(
                context = this,
                positions = firstRecyclerSeats,
                availableSeats = availableSeats
            ) { selected ->
                updateSelectedSeat(selected, firstAdapter, secondAdapter)
            }

            secondAdapter = SeatAdapter(
                context = this,
                positions = secondRecyclerSeats,
                availableSeats = availableSeats
            ) { selected ->
                updateSelectedSeat(selected, firstAdapter, secondAdapter)
            }
            binding.recyclerViewSeats1Floor.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerViewSeats1Floor.adapter = firstAdapter

            binding.recyclerViewSeats2Floor.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerViewSeats2Floor.adapter = secondAdapter
        }
    }

    private fun updateSelectedSeat(
        selected: String,
        firstAdapter: SeatAdapter?,
        secondAdapter: SeatAdapter?
    ) {
        when {
            firstAdapter?.containsSeat(selected) == true -> {
                secondAdapter?.clearSelection()
            }
            secondAdapter?.containsSeat(selected) == true -> {
                firstAdapter?.clearSelection()
            }
        }
        selectedSeat = selected
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
    fun formatToDateMonthYear(input: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val dateTime = ZonedDateTime.parse(input, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return dateTime.format(outputFormatter)
    }
}
