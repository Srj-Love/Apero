package com.srjlove.apero.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.srjlove.apero.R
import com.srjlove.apero.data.model.Playing
import com.srjlove.apero.data.model.Upcomming
import com.srjlove.apero.databinding.ActivityMainBinding
import com.srjlove.apero.ui.main.details.DetailsActivity
import com.srjlove.apero.ui.main.upcoming.UpcomingAdapter
import com.srjlove.apero.utils.Status
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainAdapter.SelectedCallback {

    companion object {
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w200"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: MainAdapter
    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var binding: ActivityMainBinding

    // pagination
    private var startOffset: Int = 1
    private var currentOffset: Int = startOffset
    private var isLoading = false
    private var isLastPage = false

    private var isUpcomingShowing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)



        return super.onCreateOptionsMenu(menu)
    }

    private fun handleSearchView(
    ) {

        binding.svMovies.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i(TAG, "onQueryTextSubmit: $query")
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i(TAG, "onQueryTextChange: $newText")
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_open -> {
                handleUpcomingVisibility(item)
                true
            }

            else -> false
        }
    }


    private fun handleUpcomingVisibility(menu: MenuItem?) {
        menu?.let {
            if (isUpcomingShowing) {

                val oldParams = binding.arcShape.layoutParams
                oldParams.height = 80


                menu.setIcon(android.R.drawable.arrow_down_float)
                isUpcomingShowing = false
                with(binding) {
                    arcShape.layoutParams = oldParams
                    rcvUpcomming.isVisible = isUpcomingShowing
                }

            } else {
                val oldParams = binding.arcShape.layoutParams
                oldParams.height = 600

                menu.setIcon(android.R.drawable.arrow_up_float)
                isUpcomingShowing = true
                with(binding) {
                    arcShape.layoutParams = oldParams
                    rcvUpcomming.isVisible = isUpcomingShowing

                }

            }
        }
    }

    private fun setupObserver() {

        mainViewModel.upcoming.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { upcoming -> renderUpcomingList(upcoming) }
                    binding.rcvUpcomming.visibility =
                        if (isUpcomingShowing) View.VISIBLE else View.GONE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rcvUpcomming.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        mainViewModel.playing.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (currentOffset > 1) binding.pbPlaying.visibility = View.GONE
                    else binding.progressBar.visibility = View.GONE
                    it.data?.let { playing -> renderPlayingList(playing) }
                    binding.rcvPlaying.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    if (currentOffset > 1)
                        binding.pbPlaying.visibility = View.VISIBLE
                    else {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rcvPlaying.visibility = View.GONE
                    }

                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderPlayingList(playing: Playing) {
        adapter.addData(playing.results)
        adapter.notifyDataSetChanged()
    }

    // init RCV
    private fun setupUI() {
        with(binding) {
            //region Playing
            rcvPlaying.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MainAdapter(arrayListOf(), this@MainActivity)
            rcvPlaying.addItemDecoration(
                DividerItemDecoration(
                    rcvPlaying.context,
                    (rcvPlaying.layoutManager as LinearLayoutManager).orientation
                )
            )
            rcvPlaying.adapter = adapter

            nsvPlaying.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->

                if (v.getChildAt(v.childCount - 1) != null) {


                    if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                        if (isLastPage.not() && isLoading.not()) {
                            currentOffset++
                            pbPlaying.isVisible = true
                            mainViewModel.loadMore(currentOffset)
                        }
                    }
                }
            }

            //endregion

            //region upcoming
            rcvUpcomming.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            upcomingAdapter = UpcomingAdapter(arrayListOf())
            rcvUpcomming.addItemDecoration(
                DividerItemDecoration(
                    rcvUpcomming.context,
                    (rcvUpcomming.layoutManager as LinearLayoutManager).orientation
                )
            )
            rcvUpcomming.adapter = upcomingAdapter
            //endregion
        }
        handleSearchView()
    }

    private fun renderUpcomingList(upcoming: Upcomming) {
        upcomingAdapter.addData(upcoming.results)
        upcomingAdapter.notifyDataSetChanged()
    }

    // on cell selected
    override fun onSelection(list: List<Playing.Result>, position: Int) {

        val playing = Playing()
        playing.results = list

        val plString = Gson().toJson(playing)

        startActivity(
            Intent(
                this, DetailsActivity::class.java
            ).apply {
                putExtra("data", plString)
                putExtra("position", position)
            }
        )
    }

}