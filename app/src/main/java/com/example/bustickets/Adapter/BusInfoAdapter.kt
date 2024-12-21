package com.example.bustickets.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bustickets.Interface.RecyclerViewOnClick
import com.example.bustickets.Model.Trip
import com.example.bustickets.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class BusInfoAdapter(
    private var busList: List<Trip>, val ROC: RecyclerViewOnClick
) : RecyclerView.Adapter<BusInfoAdapter.BusInfoViewHolder>() {

    inner class BusInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewRoute: TextView = itemView.findViewById(R.id.textViewRoute)
        val textViewTypeBus: TextView = itemView.findViewById(R.id.textViewTypeBus)
        val textViewAvailableSeat: TextView = itemView.findViewById(R.id.textViewAvailableSeat)
        val textViewPriceTicket: TextView = itemView.findViewById(R.id.textViewPriceTicket)
        val textViewStartPoint: TextView = itemView.findViewById(R.id.textViewStartPoint)
        val textViewTimeStart: TextView = itemView.findViewById(R.id.textViewTimeStart)
        val textViewEndPoint: TextView = itemView.findViewById(R.id.textViewEndPoint)
        val textViewTimeEnd: TextView = itemView.findViewById(R.id.textViewTimeEnd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_select_ticket, parent, false)
        return BusInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusInfoViewHolder, position: Int) {
        val busInfo = busList[position]
        val maxLength = 20
        val originalText = busInfo.startLocation + "-" + busInfo.endLocation
        val truncatedText = if (originalText.length > maxLength) {
            originalText.substring(0, maxLength) + "..."
        } else {
            originalText
        }
        holder.textViewRoute.text = truncatedText
//        holder.textViewTypeBus.text = busInfo.type
        holder.textViewAvailableSeat.text = busInfo.availableSeats.toString()
        holder.textViewPriceTicket.text = "${busInfo.price} VND"
        holder.textViewStartPoint.text = busInfo.startLocation
        holder.textViewTimeStart.text = formatToHourMinute(busInfo.departureTime)
        holder.textViewEndPoint.text = busInfo.endLocation
        holder.textViewTimeEnd.text = formatToHourMinute(busInfo.arriveTime)
        holder.itemView.setOnClickListener {
            ROC.onClickItem(position)
        }

    }

    override fun getItemCount(): Int {
        return busList.size
    }

    fun updateList(newList: List<Trip>) {
        busList = newList
        notifyDataSetChanged()
    }

    fun formatToHourMinute(input: String): String {
        return try {
            // Định dạng chuỗi đầu vào
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

            // Chuyển chuỗi sang ZonedDateTime
            val dateTime = ZonedDateTime.parse(input, inputFormatter)

            // Định dạng giờ: phút
            val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

            // Trả về chuỗi giờ: phút
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            "Invalid input format" // Trả về thông báo nếu chuỗi không hợp lệ
        }
    }

}
