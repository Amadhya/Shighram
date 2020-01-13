package com.example.parksmart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PaymentHistoryAdapter: RecyclerView.Adapter<ViewHolder>() {

    var data =  listOf<Map<String, String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater
            .inflate(R.layout.payment_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.location.text = item["location"]
        holder.duration.text = item["duration"]
        holder.date.text = item["date"]
        holder.amount.text = item["amount"]
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val location: TextView = itemView.findViewById(R.id.location)
    val duration: TextView = itemView.findViewById(R.id.duration)
    val date: TextView = itemView.findViewById(R.id.date)
    val amount: TextView = itemView.findViewById(R.id.amount)
}