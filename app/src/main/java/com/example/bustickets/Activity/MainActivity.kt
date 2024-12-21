package com.example.bustickets.Activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bustickets.Authentication.BiometricPromptManager
import com.example.bustickets.R
import com.example.bustickets.databinding.ActivityMainBinding
import com.example.bustickets.Fragment.HomeFragment
import com.example.bustickets.Fragment.NotificationFragment
import com.example.bustickets.Fragment.HelperFragment
import com.example.bustickets.Fragment.SettingFragment
import com.example.bustickets.Fragment.TicketsFragment
import com.example.bustickets.Room.ProcessAuth
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val promptManager by lazy {
        BiometricPromptManager(this)
    }
    companion object {
        var isLoggedIn: Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val processAuth = ProcessAuth()
        isLoggedIn = processAuth.isLoggedIn(this@MainActivity)

        binding.bottomNavigation.add(
            CurvedBottomNavigation.Model(1,"Hỗ trợ", R.drawable.ic_helpdesk)
        )
        binding.bottomNavigation.add(
            CurvedBottomNavigation.Model(2,"Vé của tôi", R.drawable.tickets)
        )
        binding.bottomNavigation.add(
            CurvedBottomNavigation.Model(3,"Trang chủ", R.drawable.ic_ticket)
        )
        binding.bottomNavigation.add(
            CurvedBottomNavigation.Model(4,"Thông báo", R.drawable.ic_notification)
        )
        binding.bottomNavigation.add(
            CurvedBottomNavigation.Model(5,"Cài đặt", R.drawable.ic_settings)
        )
        binding.bottomNavigation.setOnClickMenuListener {
            when (it.id){
                1 -> {
                    replaceFragment(HelperFragment())
                }
                2 -> {
                    replaceFragment(TicketsFragment())
                }
                3 -> {
                    replaceFragment(HomeFragment())
                }
                4 -> {
                    replaceFragment(NotificationFragment())
                }
                5 -> {
                    replaceFragment(SettingFragment())
                }
            }

        }
        replaceFragment(HomeFragment())
        binding.bottomNavigation.show(3)
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val density = displayMetrics.density  // Mật độ pixel (dp)
        val curveRadius = when {
            screenWidth == 1080 -> 20* density  // Tỷ lệ dành cho 1080x1920
            screenWidth == 540 -> 150 * density   // Tỷ lệ dành cho 540x960
            else -> 70 * density                 // Tỷ lệ mặc định
        }

        binding.bottomNavigation.curveRadius = curveRadius
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }
}