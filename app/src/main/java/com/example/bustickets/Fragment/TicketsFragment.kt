package com.example.bustickets.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustickets.Adapter.TicketsAdapter
import com.example.bustickets.Activity.LoginActivity
import com.example.bustickets.Model.TicketData
import com.example.bustickets.Model.Trip
import com.example.bustickets.Model.informationTicket
import com.example.bustickets.Model.ticketResponse
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.R
import com.example.bustickets.Room.ProcessAuth
import com.example.bustickets.databinding.FragmentTicketsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketsFragment : Fragment() {
    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding!!
    private var tickets: MutableList<informationTicket> = mutableListOf()
    private lateinit var adapter: TicketsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)

        val processAuth = ProcessAuth()
        CoroutineScope(Dispatchers.Main).launch {
            val token = processAuth.getUserToken(requireActivity())
            if (token != null) {
                RetrofitInstance.apiService.getUserTickets("Bearer ${token}")
                    .enqueue(object : Callback<ticketResponse> {
                        override fun onResponse(call: Call<ticketResponse>, response: Response<ticketResponse>) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    tickets = responseBody.data.toMutableList()
                                    filterTicketsByStatus("booked")
                                } else {
                                    Log.e("TicketsFragment", "API response body is null")
                                    Toast.makeText(requireContext(), "Không có dữ liệu trả về từ API.", Toast.LENGTH_SHORT).show()
                                }

                            } else {
                                Toast.makeText(requireContext(), "Lỗi: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                                Log.d("aaa","Lỗi: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<ticketResponse>, t: Throwable) {
                            Toast.makeText(requireContext(), "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                println("Không tìm thấy token!")
            }
        }

        if (processAuth.isLoggedIn(requireContext())){
            binding.layoutMyTicket.visibility = View.VISIBLE
            binding.layoutLogout.visibility = View.GONE
        } else {
            binding.layoutMyTicket.visibility = View.GONE
            binding.layoutLogout.visibility = View.VISIBLE
        }

        binding.textViewLogin.setOnClickListener {
            val inent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(inent)
        }

        adapter = TicketsAdapter(tickets)
        binding.recyclerViewTicketList.adapter = adapter
        binding.recyclerViewTicketList.layoutManager = LinearLayoutManager(requireActivity())
        filterTicketsByStatus("booked")
        binding.textViewBooked.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cyan))
        binding.textViewOther.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))

        // Xử lý sự kiện khi nhấn vào TextView
        binding.textViewBooked.setOnClickListener {
            filterTicketsByStatus("booked")
            binding.textViewBooked.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cyan))
            binding.textViewOther.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
        }

        binding.textViewOther.setOnClickListener {
            filterTicketsByStatus("cancelled", "cancelled")
            binding.textViewOther.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cyan))
            binding.textViewBooked.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
        }

        return binding.root
    }

    private fun filterTicketsByStatus(vararg statuses: String) {
        val filteredList = tickets.filter { it.status in statuses }
        adapter = TicketsAdapter(filteredList)
        binding.recyclerViewTicketList.adapter = adapter
    }
}