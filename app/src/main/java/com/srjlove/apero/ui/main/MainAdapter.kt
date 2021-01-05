package com.srjlove.apero.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srjlove.apero.R
import com.srjlove.apero.data.model.Playing
import com.srjlove.apero.databinding.ItemLayoutBinding
import com.srjlove.apero.ui.main.MainActivity.Companion.IMAGE_BASE_URL
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter(
    private val playingList: ArrayList<Playing.Result>,
    private val selectedCallback: SelectedCallback
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>(),Filterable {

    private var mListFilterred: ArrayList<Playing.Result>

    interface SelectedCallback {
        fun onSelection(list: List<Playing.Result>,position: Int)
    }

    inner class DataViewHolder(private var vb: ItemLayoutBinding) :
        RecyclerView.ViewHolder(vb.root) {

        @SuppressLint("SetTextI18n")
        fun bind(model: Playing.Result) {
            val color = arrayListOf(
                R.color.place_1,
                R.color.place_2,
                R.color.place_3,
                R.color.place_4
            ).random()

            with(vb) {
                textViewUserName.text = model.title
                tvPlayingRating.text = "Rating: ${model.voteAverage}"
                tvPlayingReleaseDt.text = "Release Date: ${model.releaseDate}"
                Glide.with(imageViewAvatar.context)
                    .load("${IMAGE_BASE_URL}${model.posterPath}")
                    .placeholder(color)
                    .error(color)
                    .into(imageViewAvatar)

                container.setOnClickListener {
                    selectedCallback.onSelection(playingList,bindingAdapterPosition)
                }
            }

        }
    }

    init {
        mListFilterred = playingList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val item = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(item)
    }

    override fun getItemCount(): Int = mListFilterred.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(mListFilterred[position])

    fun addData(list: List<Playing.Result>) {
        if (list.isNullOrEmpty().not()) {
            playingList.addAll(list)
        }
    }

    @SuppressLint("CheckResult", "Unchecked")
    private val mFilterable: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val charSearch = constraint.toString()
            mListFilterred = if (charSearch.isEmpty()) {
                playingList
            } else {
                val resultList = ArrayList<Playing.Result>()
                for (row in playingList) {
                    if (row.title!!.toLowerCase(Locale.ROOT)
                            .contains(charSearch.toLowerCase(Locale.ROOT))
                    ) {
                        resultList.add(row)
                    }
                }
                resultList
            }
            val filterResults = FilterResults()
            filterResults.values = mListFilterred
            filterResults.count = mListFilterred.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            if (mListFilterred.size > 0) {
                mListFilterred = results.values as ArrayList<Playing.Result>
                notifyDataSetChanged()
            }
        }
    }

    override fun getFilter(): Filter {
        return mFilterable
    }

    fun getData() = playingList

}