package com.example.bustickets.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bustickets.Authentication.Validator
import com.example.bustickets.Model.LoginRequest
import com.example.bustickets.Model.LoginResponse
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.R
import com.example.bustickets.Room.ProcessAuth
import com.example.bustickets.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple)

        binding.errorTextPhoneNumber.visibility = View.GONE
        binding.errorTextPassword.visibility = View.GONE

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.textViewForgotPassword.setOnClickListener {

        }

        binding.materialButtonLogin.setOnClickListener {
            val email = binding.textInputEditTextEmail.text.toString().trim()
            val password = binding.textInputEditTextPassword.text.toString().trim()
            binding.errorTextPhoneNumber.visibility = View.GONE
            binding.errorTextPassword.visibility = View.GONE

            if (email.isNullOrEmpty()) {
                binding.errorTextPhoneNumber.setText("Không được để trống!")
                binding.errorTextPhoneNumber.visibility = View.VISIBLE
            }else if (password.isNullOrEmpty()) {
                binding.errorTextPassword.setText("Không được để trống!")
                binding.errorTextPassword.visibility = View.VISIBLE
            }else if (!Validator.validateEmail(email)){
                binding.errorTextPhoneNumber.setText("Không đúng định dạng!")
                binding.errorTextPhoneNumber.visibility = View.VISIBLE
            }else if (!Validator.validatePassword(password)){
                binding.errorTextPassword.setText("Mật khẩu gồm ký tự in hoa, ký tự in thường, chữ số, ký tự đặc biệt!")
                binding.errorTextPassword.visibility = View.VISIBLE
            }else{
                val apiService = RetrofitInstance.apiService
                val loginRequest = LoginRequest(email, password)
                val call = apiService.loginUser(loginRequest)
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val accessToken = response.body()?.data ?.token
                            val user = response.body()?.data?.user
                            val sharedPreferences = this@LoginActivity.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("NAME", user!!.name)
                            editor.putString("ID", user!!._id)
                            editor.apply()
                            if (accessToken != null) {
                                val processAuth = ProcessAuth()
                                processAuth.saveUserToken(this@LoginActivity, accessToken)
                            } else {
                                println("Access token bị null, không thể lưu.")
                            }

                            // TODO: Xử lý message
                            val intent = Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                            startActivity(intent);
                            finish()
                        } else {
                            // TODO: Xử lý phản hồi không thành công
                            Toast.makeText(
                                this@LoginActivity,
                                "Email hoặc mật khẩu không đúng!",
                                Toast.LENGTH_LONG
                            ).show();
                            binding.textInputEditTextPassword.setText("");
                            binding.textInputEditTextEmail.setText("");
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // In lỗi chi tiết để kiểm tra
                        Log.e("LoginError", "Request failed: ${t.message}", t)
                        Toast.makeText(
                            this@LoginActivity,
                            "Đã xảy ra lỗi: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }
}