package com.example.bustickets.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.bustickets.Activity.ChooseSeatActivity
import com.example.bustickets.Activity.EditAccountActivity
import com.example.bustickets.Activity.LoginActivity
import com.example.bustickets.Activity.VnpayActivity
import com.example.bustickets.R
import com.example.bustickets.Room.ProcessAuth
import com.example.bustickets.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    val processAuth = ProcessAuth()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        if (processAuth.isLoggedIn(requireContext())){
            binding.selectLogout.visibility = View.VISIBLE
            binding.imageView4.visibility = View.VISIBLE
            binding.textView8.visibility = View.VISIBLE
            binding.selectAccount.visibility = View.VISIBLE
            binding.imageViewAccount.visibility = View.VISIBLE
            binding.textViewDescriptionAccount.visibility = View.VISIBLE
            binding.textViewAccount.visibility = View.VISIBLE
            val sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("NAME", "default_value")
            binding.textViewName.setText(username)

        }else {
            binding.selectLogout.visibility = View.GONE
            binding.imageView4.visibility = View.GONE
            binding.textView8.visibility = View.GONE
            binding.selectAccount.visibility = View.GONE
            binding.imageViewAccount.visibility = View.GONE
            binding.textViewDescriptionAccount.visibility = View.GONE
            binding.textViewAccount.visibility = View.GONE
        }

        binding.textViewName.setOnClickListener {
            if (!processAuth.isLoggedIn(requireContext())) {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

        binding.selectAccount.setOnClickListener {
            val intent = Intent(requireActivity(), EditAccountActivity::class.java)
            startActivity(intent)
        }

        binding.selectLogout.setOnClickListener {
            showLogoutDialog()
        }

        return binding.root
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Xác nhận đăng xuất")
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?")

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setPositiveButton("Đăng xuất") { dialog, _ ->
            processAuth.logoutUser(requireContext())
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingFragment())
                .commit()
            dialog.dismiss()
        }

        builder.show()
    }
}