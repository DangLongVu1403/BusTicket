package com.example.bustickets.Authentication

class Validator {
    companion object {

        fun validatePhoneNumber(phoneNumber: String): Boolean {
            // Regex kiểm tra số điện thoại hợp lệ
            val regex = Regex("^((\\+84)|0)[0-9]{9}$")
            return regex.matches(phoneNumber)
        }

        fun validateEmail(email: String): Boolean {
            val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
            return emailRegex.matches(email.trim())
        }


        fun validatePassword(password: String): Boolean {
            // Regex để kiểm tra mật khẩu
            val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
            return regex.matches(password)
        }
    }
}