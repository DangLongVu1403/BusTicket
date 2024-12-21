package com.example.bustickets.Room

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProcessAuth {
    fun saveUserToken(context: Context, token: String) {
        val tokenDao = ApplicationDatabase.getDatabase(context).tokenDao()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                tokenDao.insertToken(TokenEntity(accessToken = token))
                val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("IS_LOGGED_IN", true)
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getUserToken(context: Context): String? {
        val tokenDao = ApplicationDatabase.getDatabase(context).tokenDao()
        return try {
            val tokenEntity = tokenDao.getToken()
            tokenEntity?.accessToken
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    fun logoutUser(context: Context) {
        val tokenDao = ApplicationDatabase.getDatabase(context).tokenDao()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                tokenDao.deleteToken()
                val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("IS_LOGGED_IN", false)
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Lỗi khi đăng xuất: ${e.message}")
            }
        }
    }
}