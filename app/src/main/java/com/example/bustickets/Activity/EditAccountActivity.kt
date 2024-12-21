package com.example.bustickets.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bustickets.Model.ResponseProfile
import com.example.bustickets.Model.User
import com.example.bustickets.Model.ticketResponse
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.R
import com.example.bustickets.Room.ProcessAuth
import com.example.bustickets.databinding.ActivityEditAccountBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditAccountBinding
    private var profile: User? = null
    private var originalProfile: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val processAuth = ProcessAuth()
        CoroutineScope(Dispatchers.Main).launch {
            val token = processAuth.getUserToken(this@EditAccountActivity)
            if (token != null) {
                RetrofitInstance.apiService.getProfile("Bearer ${token}")
                    .enqueue(object : Callback<ResponseProfile> {
                        override fun onResponse(call: Call<ResponseProfile>, response: Response<ResponseProfile>) {
                            if (response.isSuccessful) {
                                profile = response.body()!!.data.user
                                originalProfile = profile?.copy()
                                updateUI()
                            } else {
                                Toast.makeText(this@EditAccountActivity, "Lỗi: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                                Log.d("aaa","Lỗi: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseProfile>, t: Throwable) {
                            Toast.makeText(this@EditAccountActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                println("Không tìm thấy token!")
            }
        }

        binding.editName.setOnClickListener {
            showInputDialog("Thay đổi tên","Nhập tên muốn thay đổi", binding.textViewName)
        }

        binding.editEmail.setOnClickListener {
            showInputDialog("Thay đổi email","Nhập email mới", binding.textViewEmail)
        }

        binding.editPhone.setOnClickListener {
            showInputDialog("Thay đổi số điện thoại","Nhập số điện thoại mới", binding.textViewPhone)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.buttonChange.setOnClickListener {

        }
    }

    private fun showInputDialog(title: String, hint: String, textView: TextView) {
        // Tạo EditText để nhập thông tin
        val inputEditText = EditText(this)
        inputEditText.hint = hint

        // Tạo dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(inputEditText) // Thêm EditText vào dialog
            .setPositiveButton("Xác Nhận") { _, _ ->
                textView.text  = inputEditText.text.toString()
                checkForChanges()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun updateUI() {
        binding.textViewName.text = profile?.name
        binding.textViewEmail.text = profile?.email
        binding.textViewPhone.text = profile?.phone
    }

    private fun checkForChanges() {
        // So sánh dữ liệu hiện tại với dữ liệu gốc
        val isChanged = profile?.let {
            binding.textViewName.text.toString() != it.name ||
                    binding.textViewEmail.text.toString() != it.email ||
                    binding.textViewPhone.text.toString() != it.phone
        } ?: false

        // Hiển thị hoặc ẩn buttonChange
        binding.buttonChange.visibility = if (isChanged) View.VISIBLE else View.GONE
    }
}