package com.example.workshop4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NumberAdapter(
    private var numbers: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

    class NumberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.numberText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_number, parent, false)
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val number = numbers[position]
        holder.textView.text = number.toString()
        holder.itemView.setOnClickListener { onItemClick(number) }
    }

    override fun getItemCount() = numbers.size

    fun updateData(newNumbers: List<Int>) {
        numbers = newNumbers
        notifyDataSetChanged()
    }
}