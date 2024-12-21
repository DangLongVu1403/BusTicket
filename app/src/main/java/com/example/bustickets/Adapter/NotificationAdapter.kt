package com.example.bustickets.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bustickets.Model.Notification
import com.example.bustickets.R

class NotificationAdapter(private val notifications: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    // ViewHolder class
    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val idTicketTextView: TextView = itemView.findViewById(R.id.textViewIdTicket)
        val informationTextView: TextView = itemView.findViewById(R.id.textViewInformation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notifications = notifications[position]
        holder.titleTextView.text = notifications.title
        holder.idTicketTextView.text = notifications.id
        holder.informationTextView.text = notifications.information
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}