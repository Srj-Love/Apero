package com.srjlove.apero.data.repository

import com.srjlove.apero.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper){
    suspend fun getUser() = apiHelper.getUsers()
    suspend fun getUpcomingMovies() =  apiHelper.getUpcomingMovies()
    suspend fun getPlayingMovies(pageNo: Int) =  apiHelper.getPlayingMovies(pageNo)
}

