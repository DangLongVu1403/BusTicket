package com.example.bustickets.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustickets.R
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date

class VnpayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vnpay)

        val etAmount: EditText = findViewById(R.id.etAmount)
        val btnPay: Button = findViewById(R.id.btnPay)

        btnPay.setOnClickListener {
            val amount = etAmount.text.toString()
            if (amount.isNotEmpty()) {
                // Thực hiện logic thanh toán
                payWithVNPAY(amount.toInt())
            } else {
                Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun payWithVNPAY(amount: Int) {
        val vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"
        val vnp_TmnCode = "OWNAQJ31"
        val vnp_HashSecret = "IIGOYM67KHL6UN5ULXLWSD43LIG4THRM"
        val vnp_ReturnUrl = "https://yourwebsite.com/return_url"

        val vnp_Params = mutableMapOf<String, String>()
        vnp_Params["vnp_Version"] = "2.1.0"
        vnp_Params["vnp_Command"] = "pay"
        vnp_Params["vnp_TmnCode"] = vnp_TmnCode
        vnp_Params["vnp_Amount"] = (amount * 100).toString()
        vnp_Params["vnp_CurrCode"] = "VND"
        vnp_Params["vnp_TxnRef"] = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        vnp_Params["vnp_OrderInfo"] = "Thanh toán đơn hàng test"
        vnp_Params["vnp_OrderType"] = "other"
        vnp_Params["vnp_Locale"] = "vn"
        vnp_Params["vnp_ReturnUrl"] = vnp_ReturnUrl
        vnp_Params["vnp_IpAddr"] = "127.0.0.1"
        vnp_Params["vnp_CreateDate"] = SimpleDateFormat("yyyyMMddHHmmss").format(Date())

        // Tạo URL thanh toán
        val query = vnp_Params.entries.joinToString("&") { (key, value) ->
            "$key=${URLEncoder.encode(value, "UTF-8")}"
        }
        val vnp_SecureHash = hmacSHA512(vnp_HashSecret, query)
        val paymentUrl = "$vnp_Url?$query&vnp_SecureHash=$vnp_SecureHash"

        // Mở trình duyệt để thực hiện thanh toán
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
        startActivity(browserIntent)
    }

    private fun hmacSHA512(key: String, data: String): String {
        val hmacSHA512 = MessageDigest.getInstance("SHA-512")
        val hmacKey = key.toByteArray()
        hmacSHA512.update(hmacKey)
        return hmacSHA512.digest(data.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }
}

