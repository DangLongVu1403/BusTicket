package com.example.bustickets.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bustickets.R

enum class SeatState {
    UNSELECTABLE, UNSELECTED, SELECTED
}

class SeatAdapter(
    private val context: Context,
    private val positions: List<String>,
    private val availableSeats: List<Int>,
    private val onSeatSelected: (String) -> Unit
) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    private val seatStates = positions.map { position ->
        if (availableSeats.contains(position.toInt())) SeatState.UNSELECTED else SeatState.UNSELECTABLE
    }.toMutableList()

    public var selectedSeat: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seatPosition = positions[position]
        val state = seatStates[position]

        holder.seatTextView.text = seatPosition

        when (state) {
            SeatState.UNSELECTABLE -> holder.seatTextView.setBackgroundColor(
                ContextCompat.getColor(context, R.color.gray)
            )
            SeatState.UNSELECTED -> holder.seatTextView.setBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
            )
            SeatState.SELECTED -> holder.seatTextView.setBackgroundColor(
                ContextCompat.getColor(context, R.color.teal)
            )
        }

        holder.itemView.setOnClickListener {
            if (state == SeatState.UNSELECTABLE) return@setOnClickListener

            // Đổi trạng thái ghế đã chọn
            selectedSeat?.let { prevSeat ->
                val prevIndex = positions.indexOf(prevSeat)
                if (prevIndex != -1) {
                    seatStates[prevIndex] = SeatState.UNSELECTED
                    notifyItemChanged(prevIndex)
                }
            }

            seatStates[position] = SeatState.SELECTED
            selectedSeat = seatPosition
            notifyItemChanged(position)

            // Trả về ghế được chọn
            onSeatSelected(seatPosition)
        }
    }

    override fun getItemCount(): Int = positions.size

    fun clearSelection() {
        selectedSeat?.let { prevSeat ->
            val prevIndex = positions.indexOf(prevSeat)
            if (prevIndex != -1) {
                seatStates[prevIndex] = SeatState.UNSELECTED
                notifyItemChanged(prevIndex)
            }
        }
    }

    fun containsSeat(seat: String): Boolean {
        return positions.contains(seat)
    }

    class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seatTextView: TextView = itemView.findViewById(R.id.textViewSeat)
    }
}

