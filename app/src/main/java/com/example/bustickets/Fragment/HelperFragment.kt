package com.example.bustickets.Fragment

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bustickets.databinding.FragmentHelperBinding

class HelperFragment : Fragment() {
    private var _binding: FragmentHelperBinding? = null

    private val binding get() = _binding!! // An toàn để sử dụng sau khi khởi tạo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentHelperBinding.inflate(inflater, container, false)
        binding.textViewBooking.paintFlags = binding.textViewBooking.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        return binding.root
    }
}