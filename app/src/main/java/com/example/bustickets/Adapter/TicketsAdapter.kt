package com.example.bustickets.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bustickets.Model.CancelTicketRequest
import com.example.bustickets.Model.informationTicket
import com.example.bustickets.Object.RetrofitInstance
import com.example.bustickets.R
import com.example.bustickets.Room.ProcessAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TicketsAdapter(private val ticketList: List<informationTicket>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_BOOKED = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_BOOKED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ticket_booked, parent, false)
                BookedViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_ticket_other, parent, false)
                OtherViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ticket = ticketList[position]
        when (holder) {
            is BookedViewHolder -> holder.bind(ticket)
            is OtherViewHolder -> holder.bind(ticket)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (ticketList[position].status) {
            "booked" -> 1
            "cancelled" -> 2
            "cancelled" -> 3
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    class BookedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ticket: informationTicket) {
            val idTicket = itemView.findViewById<TextView>(R.id.textViewIdTickket)
            val startPoint = itemView.findViewById<TextView>(R.id.textViewStartPoint)
            val endPoint = itemView.findViewById<TextView>(R.id.textViewEndPoint)
            val startTime = itemView.findViewById<TextView>(R.id.textViewTimeStart)
            val endTime = itemView.findViewById<TextView>(R.id.textViewTimeEnd)
            val date = itemView.findViewById<TextView>(R.id.textViewDate)
            val bookingDate = itemView.findViewById<TextView>(R.id.textViewBookingDate)

            val maxLength = 15
            val originalText = ticket._id
            val truncatedText = if (originalText.length > maxLength) {
                originalText.substring(0, maxLength) + "..."
            } else {
                originalText
            }
            idTicket.text = truncatedText
            bookingDate.text = "Ngày đặt: ${ticket.createdAt}"
            startPoint.text = ticket.trip.startLocation
            endPoint.text = ticket.trip.endLocation
            startTime.text = formatToHourMinute(ticket.trip.departureTime)
            endTime.text = formatToHourMinute(ticket.trip.arriveTime)
            date.text = formatToDateMonthYear(ticket.trip.updatedAt)
            bookingDate.text = formatToDateMonthYear(ticket.createdAt)

            // Thêm sự kiện click
            itemView.setOnClickListener {
                showCustomDialog(itemView, ticket)
            }
        }

        private fun showCustomDialog(view: View, ticket: informationTicket) {
            val context = view.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_ticket_options, null)

            val dialog = android.app.AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            val btnViewDetails = dialogView.findViewById<TextView>(R.id.btnViewDetails)
            val btnCancelTicket = dialogView.findViewById<TextView>(R.id.btnCancelTicket)

            btnViewDetails.setOnClickListener {
                dialog.dismiss()
                showTicketDetails(context, ticket)
            }

            btnCancelTicket.setOnClickListener {
                dialog.dismiss()
                showCancelConfirmation(context, ticket)
            }

            dialog.show()
        }

        private fun showTicketDetails(context: Context, ticket: informationTicket) {
            val detailView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_ticket_details, null)

            val dialog = android.app.AlertDialog.Builder(context)
                .setView(detailView)
                .create()

            detailView.findViewById<TextView>(R.id.textViewTicketId).text = "Mã vé: ${ticket._id}"
            detailView.findViewById<TextView>(R.id.textViewStartLocation).text =
                "Điểm đi: ${ticket.trip.startLocation}"
            detailView.findViewById<TextView>(R.id.textViewEndLocation).text =
                "Điểm đến: ${ticket.trip.endLocation}"
            detailView.findViewById<TextView>(R.id.textViewDepartureTime).text =
                "Thời gian xuất bến: ${formatToHourMinute(ticket.trip.departureTime)}"
//            detailView.findViewById<TextView>(R.id.textViewTypeBus).text =
//                "Loại xe: ${ticket.trip.type}"
            detailView.findViewById<TextView>(R.id.textViewNumberSeat).text =
                "Số ghế: ${ticket.seatNumber}"
            detailView.findViewById<TextView>(R.id.textViewBookingDate).text =
                "Ngày đặt: ${formatToDateMonthYear(ticket.createdAt)}"
            detailView.findViewById<TextView>(R.id.textViewPrice).text =
                "Tổng tiền: ${ticket.trip.price} VND"

            dialog.show()
        }

        private fun showCancelConfirmation(context: Context, ticket: informationTicket) {
            android.app.AlertDialog.Builder(context)
                .setTitle("Xác nhận hủy vé")
                .setMessage("Bạn có chắc chắn muốn hủy vé này?")
                .setPositiveButton("Đồng ý") { _, _ ->
                    cancelTicket(ticket,context)
                }
                .setNegativeButton("Không", null)
                .create()
                .show()
        }

        private fun cancelTicket(ticket: informationTicket,context: Context) {
            Toast.makeText(itemView.context, "Vé ${ticket._id} đã được hủy.", Toast.LENGTH_SHORT).show()
            val processAuth = ProcessAuth()
            CoroutineScope(Dispatchers.Main).launch {
                val token = processAuth.getUserToken(context)
                if (token != null) {
                    RetrofitInstance.apiService.cancelTicket("Bearer ${token}",ticket._id)
                        .enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context,"Hủy vé thành công", Toast.LENGTH_SHORT).show()

                                } else {
                                    Toast.makeText(context, "Lỗi: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                                    Log.d("aaa","Lỗi: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {
                    println("Không tìm thấy token!")
                }
            }
        }

        private fun formatToHourMinute(input: String): String {
            // Định dạng chuỗi đầu vào
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val dateTime = ZonedDateTime.parse(input, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")
            return dateTime.format(outputFormatter)
        }

        private fun formatToDateMonthYear(input: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val dateTime = ZonedDateTime.parse(input, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return dateTime.format(outputFormatter)
        }
    }

    class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ticket: informationTicket) {
            val idTicket = itemView.findViewById<TextView>(R.id.textViewIdTickket)
            val status = itemView.findViewById<TextView>(R.id.textViewStatus)
            val bookingDate = itemView.findViewById<TextView>(R.id.textViewBookingDate)

            idTicket.text = ticket._id
            status.text = when (ticket.status) {
                "COMPLETED" -> "Đã hoàn thành"
                "cancelled" -> "Đã hủy"
                else -> "Trạng thái khác"
            }
            bookingDate.text = "Ngày đặt: ${ticket.createdAt}"
        }
    }
}
