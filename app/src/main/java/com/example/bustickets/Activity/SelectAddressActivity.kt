package com.example.bustickets.Activity

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustickets.Fragment.HomeFragment
import com.example.bustickets.Model.ResponseStation
import com.example.bustickets.Model.Station
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.databinding.ActivitySelectAddressBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectAddressBinding
    private lateinit var stationList: List<Station> // Lưu danh sách các Station

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gọi API để lấy danh sách trạm
        RetrofitInstance.apiService.getStation()
            .enqueue(object : Callback<ResponseStation> {
                override fun onResponse(call: Call<ResponseStation>, response: Response<ResponseStation>) {
                    if (response.isSuccessful) {
                        stationList = response.body()!!.data.stations

                        // Lấy type từ intent và gán vào dataArray
                        val type = intent.getStringExtra("type")
                        val dataArray = when (type) {
                            "1" -> stationList.map { "${it.name}\n${it.address}" }.toTypedArray()
                            "2" -> stationList.map { "${it.name}\n${it.address}" }.toTypedArray()
                            else -> arrayOf("Default Item")
                        }

                        // Khởi tạo Adapter sau khi dữ liệu đã được tải về
                        val adapter = ArrayAdapter(
                            this@SelectAddressActivity,
                            R.layout.simple_list_item_1,
                            dataArray
                        )
                        binding.listViewSelectAddress.adapter = adapter
                    } else {
                        Toast.makeText(this@SelectAddressActivity, "Lỗi: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                        Log.d("aaa", "Lỗi: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseStation>, t: Throwable) {
                    Toast.makeText(this@SelectAddressActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        // Xử lý sự kiện khi click vào một item
        binding.listViewSelectAddress.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                // Lấy đối tượng Station tương ứng với item được chọn
                val selectedStation = stationList[position]

                // Gửi dữ liệu về Intent
                val intent = Intent(this,HomeFragment::class.java)
                intent.putExtra("station_code", selectedStation.code) // Trả về code
                intent.putExtra("station_name", selectedStation.name)
                setResult(RESULT_OK, intent)

                finish() // Kết thúc Activity
            }

        // Xử lý nút thoát
        binding.imgExit.setOnClickListener {
            finish()
        }
    }
}
