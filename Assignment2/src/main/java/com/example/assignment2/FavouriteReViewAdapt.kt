package com.example.assignment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavouriteReViewAdapt(private val carList: MutableList<Car>)
    : RecyclerView.Adapter<FavouriteReViewAdapt.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_re_layout, parent, false)

        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        val car = carList[position]

        holder.carName.text = "${car.name} ${car.model}"
        holder.carImage.setImageResource(car.imageRes)

        holder.removeBtn.setOnClickListener {

            car.isFavourite = false

            carList.removeAt(position)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, carList.size)
        }
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val carImage: ImageView = itemView.findViewById(R.id.imgPro)
        val carName: TextView = itemView.findViewById(R.id.txtName)
        val removeBtn: Button = itemView.findViewById(R.id.button_remove)
    }
}