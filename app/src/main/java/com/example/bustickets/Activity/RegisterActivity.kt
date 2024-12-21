package com.example.bustickets.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bustickets.Authentication.Validator
import com.example.bustickets.Model.RegisterRequest
import com.example.bustickets.Model.RegisterResponse
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.R
import com.example.bustickets.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.errorTextPhoneNumber.visibility = View.GONE
        binding.errorTextPassword.visibility = View.GONE
        binding.errorTextName.visibility = View.GONE
        binding.errorTextConfirmPassword.visibility = View.GONE

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple)

        binding.errorTextPhoneNumber.visibility = View.GONE
        binding.errorTextName.visibility = View.GONE
        binding.errorTextPassword.visibility = View.GONE
        binding.errorTextConfirmPassword.visibility = View.GONE

        binding.textViewLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.materialButtonRegister.setOnClickListener {
            val name = binding.textInputEditTextName.text.toString()
            val email = binding.textInputEditTextEmail.text.toString()
            val phoneNumber = binding.textInputEditTextPhoneNumber.text.toString()
            val password = binding.textInputEditTextPassword.text.toString()
            val confirmPassword = binding.textInputEditTextConfirmPassword.text.toString()
            binding.errorTextPhoneNumber.visibility = View.GONE
            binding.errorTextPassword.visibility = View.GONE
            binding.errorTextName.visibility = View.GONE
            binding.errorTextConfirmPassword.visibility = View.GONE

            if (name.isNullOrEmpty()) {
                binding.errorTextName.setText("Không được để trống!")
                binding.errorTextName.visibility = View.VISIBLE
            }else if (email.isNullOrEmpty()) {
                binding.errorTextEmail.setText("Không được để trống!")
                binding.errorTextEmail.visibility = View.VISIBLE
            } else if (phoneNumber.isNullOrEmpty()) {
                binding.errorTextPhoneNumber.setText("Không được để trống!")
                binding.errorTextPhoneNumber.visibility = View.VISIBLE
            }else if (password.isNullOrEmpty()) {
                binding.errorTextPassword.setText("Không được để trống!")
                binding.errorTextPassword.visibility = View.VISIBLE
            }else if (confirmPassword.isNullOrEmpty()) {
                binding.errorTextConfirmPassword.setText("Không được để trống!")
                binding.errorTextConfirmPassword.visibility = View.VISIBLE
            }else if (!Validator.validateEmail(email)){
                binding.errorTextEmail.setText("Không đúng định dạng!")
                binding.errorTextEmail.visibility = View.VISIBLE
            }else if (!Validator.validatePhoneNumber(phoneNumber)){
                binding.errorTextPhoneNumber.setText("Không đúng định dạng!")
                binding.errorTextPhoneNumber.visibility = View.VISIBLE
            }else if (!Validator.validatePassword(password)){
                binding.errorTextPassword.setText("Mật khẩu gồm ký tự in hoa, ký tự in thường, chữ số, ký tự đặc biệt!")
                binding.errorTextPassword.visibility = View.VISIBLE
            }else if (!confirmPassword.isNullOrEmpty()&& !password.isNullOrEmpty() && !confirmPassword.equals(password)) {
                binding.errorTextConfirmPassword.setText("Mật khẩu không trùng khớp!")
                binding.errorTextConfirmPassword.visibility = View.VISIBLE
            }else{
                val apiService = RetrofitInstance.apiService
                val registerRequest = RegisterRequest(name,email, password,phoneNumber,"user")
                val call = apiService.registerUser(registerRequest)
                call.enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            // TODO: Xử lý message
                            val intent = Intent(
                                this@RegisterActivity,
                                LoginActivity::class.java
                            )
                            startActivity(intent);
                            finish()
                        } else {
                            // TODO: Xử lý phản hồi không thành công
                            Toast.makeText(
                                this@RegisterActivity,
                                "Số điện thoại hoặc mật khẩu không đúng!",
                                Toast.LENGTH_LONG
                            ).show();
                            binding.textInputEditTextPassword.setText("")
                            binding.textInputEditTextPhoneNumber.setText("")
                            binding.textInputEditTextConfirmPassword.setText("")
                            binding.textInputEditTextName.setText("")
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        // In lỗi chi tiết để kiểm tra
                        Log.e("LoginError", "Request failed: ${t.message}", t)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Đã xảy ra lỗi: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }
}