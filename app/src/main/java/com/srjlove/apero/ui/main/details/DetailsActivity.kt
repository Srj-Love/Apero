package com.srjlove.apero.ui.main.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.srjlove.apero.R
import com.srjlove.apero.data.model.Playing
import com.srjlove.apero.databinding.ActivityDetailsBinding
import com.srjlove.apero.ui.main.MainActivity

private const val TAG = "DetailsActivity"

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {


        val result = intent.getStringExtra("data")
        val position = intent.getIntExtra("position",1)



        if (result != null) {
            val dataModel = Gson().fromJson(result, Playing::class.java)
            dataModel.results.forEach { Log.i(TAG, "Position:- $position") }

            setupCarousel(dataModel,position)

        } else {
            Log.i(TAG, "PersonList in null");
        }



    }

    @SuppressLint("SetTextI18n")
    private fun setupCarousel(dataModel: Playing?, position: Int) {

        val list = dataModel!!.results

        binding.cDetails.apply {
            size = list.size
            indicatorAnimationType = IndicatorAnimationType.COLOR
            carouselOffset = OffsetType.CENTER
            enableSnapping(true)
            setCarouselViewListener { view, position ->


                val tvDetTitle = view.findViewById<TextView>(R.id.tvDetTitle)
                val tvDetRating = view.findViewById<TextView>(R.id.tvDetRating)
                val tvDetRelease = view.findViewById<TextView>(R.id.tvDetRelease)
                val tvDetDesc = view.findViewById<TextView>(R.id.tvDetDesc)
                val ivDetPoster = view.findViewById<ImageView>(R.id.ivDetPoster)


                val model = list[position]

                val color = arrayListOf(
                    R.color.place_1,
                    R.color.place_2,
                    R.color.place_3,
                    R.color.place_4
                ).random()

                tvDetRating.text = "Rating: ${model.voteAverage.toString()}"
                tvDetRelease.text = "Released On: ${model.releaseDate}"
                tvDetTitle.text = "Name: ${model.title}"
                tvDetDesc.text = "Description: \n ${model.overview}"

                Glide.with(this)
                    .load("${MainActivity.IMAGE_BASE_URL}${model.posterPath}")
                    .placeholder(color)
                    .error(color)
                    .into(ivDetPoster)


            }
            show()
        }

        binding.cDetails.currentItem = position



    }
}