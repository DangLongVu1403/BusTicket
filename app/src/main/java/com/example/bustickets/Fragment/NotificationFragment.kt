package com.example.bustickets.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustickets.Activity.LoginActivity
import com.example.bustickets.Adapter.NotificationAdapter
import com.example.bustickets.Model.Notification
import com.example.bustickets.Room.ProcessAuth
import com.example.bustickets.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        val notifications = listOf(
            Notification("Vé của bạn đã được đặt", "ajxsI", " được đặt thành công. Cảm ơn bạn đã tin tưởng"),
        )

        val adapter = NotificationAdapter(notifications)
        binding.recyclerViewNotificationList.adapter = adapter
        binding.recyclerViewNotificationList.layoutManager = LinearLayoutManager(requireContext())


        val processAuth = ProcessAuth()
        if (processAuth.isLoggedIn(requireContext())){
            binding.layoutLogout.visibility = View.GONE
            binding.layoutNotificationTrip.visibility = View.VISIBLE
        }else {
            binding.layoutLogout.visibility = View.VISIBLE
            binding.layoutNotificationTrip.visibility = View.GONE
        }

        binding.textViewLogin.setOnClickListener {
            val inent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(inent)
        }
        return binding.root
    }
}