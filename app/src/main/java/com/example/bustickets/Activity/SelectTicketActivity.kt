package com.example.bustickets.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustickets.Adapter.BusInfoAdapter
import com.example.bustickets.Bottomsheet.FilterBottomSheet
import com.example.bustickets.Interface.RecyclerViewOnClick
import com.example.bustickets.Model.Trip
import com.example.bustickets.Model.TripResponse
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.R
import com.example.bustickets.Room.ProcessAuth
import com.example.bustickets.databinding.ActivitySelectTicketBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

class SelectTicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectTicketBinding
    private lateinit var adapter: BusInfoAdapter
    private var busList: MutableList<Trip> = mutableListOf()
    private var filterBusList: MutableList<Trip> = mutableListOf()
    val processAuth = ProcessAuth()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val codeStartLocation = intent.getStringExtra("codePickUpPoint")
        val codeEndLocation = intent.getStringExtra("codeDropOffPoint")
        val dateString = intent.getStringExtra("date")?.trim()
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") // Định dạng đầu vào
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Định dạng đầu ra

        val date: String = try {
            // Chuyển chuỗi đầu vào thành LocalDate
            val inputDate = LocalDate.parse(dateString, inputFormatter)

            // Lấy ngày hiện tại
            val currentDate = LocalDate.now()

            // So sánh ngày và trả về kết quả
            if (inputDate.isEqual(currentDate)) {
                "" // Trả về chuỗi rỗng nếu trùng với ngày hiện tại
            } else {
                inputDate.format(outputFormatter) // Trả về chuỗi dạng "yyyy-MM-dd" nếu khác ngày hiện tại
            }
        } catch (e: DateTimeParseException) {
            "Invalid date format" // Trả về thông báo lỗi nếu định dạng ngày không hợp lệ
        }
        RetrofitInstance.apiService.getTrips(codeStartLocation, codeEndLocation, date)
            .enqueue(object : Callback<TripResponse> {
                override fun onResponse(call: Call<TripResponse>, response: Response<TripResponse>) {
                    if (response.isSuccessful) {
                        busList = response.body()!!.data.trips.toMutableList()

                        // Update UI based on the new busList
                        if (busList.isEmpty()) {
                            binding.textViewNoTrip.visibility = View.VISIBLE
                        } else {
                            binding.textViewNoTrip.visibility = View.GONE
                        }

                        // Notify adapter of data changes
                        adapter.updateList(busList)
                        binding.textViewTripNumber.text = "\t ${busList.size} chuyến"
                    } else {
                        Toast.makeText(this@SelectTicketActivity, "Lỗi: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                        Log.d("aaa", "Lỗi: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<TripResponse>, t: Throwable) {
                    Toast.makeText(this@SelectTicketActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


        if (busList.size == 0) {
            binding.textViewNoTrip.visibility = View.VISIBLE
        } else {
            binding.textViewNoTrip.visibility = View.GONE
        }

        binding.recyclerViewTripList.layoutManager = LinearLayoutManager(this)
        adapter = BusInfoAdapter(busList, object : RecyclerViewOnClick {
            override fun onClickItem(pos: Int) {
                if (processAuth.isLoggedIn(this@SelectTicketActivity)) {
                    val item = busList[pos]
                    var intent = Intent(this@SelectTicketActivity, ChooseSeatActivity::class.java)
                    intent.putExtra("id", item._id)
                    startActivity(intent)
                } else {
                    showLoginDialog()
                }
            }
        })
        binding.recyclerViewTripList.adapter = adapter


        binding.textViewTripNumber.setText("\t ${busList.size} chuyến")

        binding.textViewArrangge.setOnClickListener {
            showSortOptions()
        }

        binding.textViewFilter.setOnClickListener {
            val filterBottomSheet = FilterBottomSheet { selectedFilter ->
                applyFilter(selectedFilter)
            }
            filterBottomSheet.show(supportFragmentManager, filterBottomSheet.tag)
        }

        val pickUpPoint = intent.getStringExtra("pickUpPoint")
        val dropOffPoint = intent.getStringExtra("dropOffPoint")
        binding.textViewPickUpPoint.setText(pickUpPoint)
        binding.textViewDropOffPoint.setText(dropOffPoint)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dateFormat = LocalDate.parse(dateString, formatter)
        val dayOfWeekMap = mapOf(
            DayOfWeek.MONDAY to "T2",
            DayOfWeek.TUESDAY to "T3",
            DayOfWeek.WEDNESDAY to "T4",
            DayOfWeek.THURSDAY to "T5",
            DayOfWeek.FRIDAY to "T6",
            DayOfWeek.SATURDAY to "T7",
            DayOfWeek.SUNDAY to "CN"
        )
        val dayOfWeek = dayOfWeekMap[dateFormat.dayOfWeek]

        binding.textViewSelectedDate.setText("$dayOfWeek, $dateString")
        binding.imgBack.setOnClickListener {
            finish()
        }

    }

    private fun showSortOptions() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_sort, null)
        bottomSheetDialog.setContentView(view)

        val textViewSortByPrice = view.findViewById<TextView>(R.id.textViewSortByPrice)
        val textViewSortByTime = view.findViewById<TextView>(R.id.textViewSortByTime)
        val imgback = view.findViewById<ImageView>(R.id.imgExit)

        textViewSortByPrice.setOnClickListener {
            sortByPrice()
            bottomSheetDialog.dismiss()
        }

        textViewSortByTime.setOnClickListener {
//            sortByTime()
            bottomSheetDialog.dismiss()
        }

        imgback.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun sortByPrice() {
        busList.sortBy {
            it.price
        }
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Đã sắp xếp theo giá", Toast.LENGTH_SHORT).show()
    }


//    private fun sortByTime() {
//        busList.sortBy {
//            val formatter = DateTimeFormatter.ofPattern("HH:mm")
//            java.time.LocalTime.parse(it.startTime, formatter)
//        }
//        adapter.notifyDataSetChanged()
//        Toast.makeText(this, "Đã sắp xếp theo giờ xuất phát", Toast.LENGTH_SHORT).show()
//    }

    private fun applyFilter(selectedFilter: String) {
        val filters = selectedFilter.split("|")
        var timeFilter = filters.getOrNull(0)
        var typeFilter = filters.getOrNull(1)
        if (typeFilter == "null") {
            typeFilter = ""
        }
        if (timeFilter == "null") {
            timeFilter = ""
        }
        // Lấy danh sách gốc để bắt đầu lọc
        filterBusList = busList.toMutableList()

        // Áp dụng lọc theo thời gian nếu có
//        if (!timeFilter.isNullOrEmpty()) {
//            filterBusList = filterBusList.filter { bus ->
//                getTimeRange(formatToHourMinute(bus.departureTime)) == timeFilter
//            }.toMutableList()
//        }

//        // Áp dụng lọc theo loại xe nếu có
//        if (!typeFilter.isNullOrEmpty()) {
//            filterBusList = filterBusList.filter { bus ->
//                bus.type == typeFilter
//            }.toMutableList()
//        }

        if (filterBusList.size == 0) {
            binding.textViewNoTrip.visibility = View.VISIBLE
        } else {
            binding.textViewNoTrip.visibility = View.GONE
        }

        adapter.updateList(filterBusList)
        binding.textViewTripNumber.text = "\t ${filterBusList.size} chuyến"

    }

    // Hàm hỗ trợ xác định khoảng thời gian từ giờ khởi hành
    private fun getTimeRange(time: String): String {
        val hour = time.split(":")[0].toInt()
        return when (hour) {
            in 0..11 -> "0h - 12h"
            in 12..17 -> "12h - 18h"
            in 18..23 -> "18h - 24h"
            else -> ""
        }
    }

    private fun showLoginDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Bạn chưa đăng nhập")
        builder.setMessage("Hãy đăng nhập để tiến hành mua vé")

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setPositiveButton("Đăng nhập") { dialog, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        builder.show()
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