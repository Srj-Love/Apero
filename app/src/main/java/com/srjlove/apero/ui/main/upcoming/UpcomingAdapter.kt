package com.srjlove.apero.ui.main.upcoming

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srjlove.apero.R
import com.srjlove.apero.data.model.Upcomming
import com.srjlove.apero.databinding.ItemUpcomingRowBinding
import com.srjlove.apero.ui.main.MainActivity.Companion.IMAGE_BASE_URL

private const val TAG = "UpcomingAdapter"

class UpcomingAdapter(
    private val upcomingList: ArrayList<Upcomming.Result>
) : RecyclerView.Adapter<UpcomingAdapter.DataViewHolder>() {

    inner class DataViewHolder(private var vb: ItemUpcomingRowBinding) : RecyclerView.ViewHolder(vb.root) {

        fun bind(model: Upcomming.Result) {
            val color = arrayListOf(R.color.place_1, R.color.place_2, R.color.place_3, R.color.place_4).random()

            with(vb){
                tvUpcomingTitle.text = model.title
                Glide.with(ivUpcomingAvatar.context)
                    .load("$IMAGE_BASE_URL${model.posterPath}")
                    .placeholder(color)
                    .error(color)
                    .into(ivUpcomingAvatar)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val item = ItemUpcomingRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(item)
    }

    override fun getItemCount(): Int = upcomingList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(upcomingList[position])

    fun addData(list: List<Upcomming.Result>) {
        if (list.isNullOrEmpty().not()) {
            upcomingList.addAll(list)
        }
    }
}