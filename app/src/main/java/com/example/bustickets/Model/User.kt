package com.example.bustickets.Model

data class User(
    val _id: String,
    val name: String,
    val password: String?,
    val email: String,
    val phone: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
