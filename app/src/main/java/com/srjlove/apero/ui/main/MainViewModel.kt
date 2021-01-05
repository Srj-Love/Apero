package com.srjlove.apero.ui.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srjlove.apero.data.model.Playing
import com.srjlove.apero.data.model.Upcomming
import com.srjlove.apero.data.repository.MainRepository
import com.srjlove.apero.utils.NetworkHelper
import com.srjlove.apero.utils.Resource
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
class MainViewModel @ViewModelInject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: MainRepository
) : ViewModel() {

    //init

    // upcoming
    private var _upcomings = MutableLiveData<Resource<Upcomming>>()
    val upcoming: LiveData<Resource<Upcomming>>
        get() = _upcomings

    // upcoming
    private var _playing = MutableLiveData<Resource<Playing>>()
    val playing: LiveData<Resource<Playing>>
        get() = _playing
    private var _pageNo = 1

    init {
        fetchUpcomingMovies()
        fetchPlayingMovies()
    }

    private fun fetchUpcomingMovies() {
        viewModelScope.launch {
            _upcomings.postValue(Resource.loading(null))
            if (networkHelper.isConnected()) {
                repository.getUpcomingMovies().let {
                    if (it.isSuccessful) {
                        _upcomings.postValue(Resource.success(it.body()))
                    } else _upcomings.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _upcomings.postValue(Resource.error("No internet connection", null))
        }
    }

    private fun fetchPlayingMovies() {
        viewModelScope.launch {
            _playing.postValue(Resource.loading(null))
            if (networkHelper.isConnected()) {
                repository.getPlayingMovies(_pageNo).let {
                    if (it.isSuccessful) {
                        _playing.postValue(Resource.success(it.body()))
                    } else _playing.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _playing.postValue(Resource.error("No internet connection", null))
        }
    }

    fun loadMore(currentOffset: Int) {
        _pageNo = currentOffset
        Log.i(TAG, "loadMore: $_pageNo")
        fetchPlayingMovies()
    }




}