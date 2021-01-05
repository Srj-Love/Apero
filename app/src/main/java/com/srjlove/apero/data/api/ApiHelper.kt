package com.srjlove.apero.data.api

import com.srjlove.apero.data.model.Playing
import com.srjlove.apero.data.model.Upcomming
import com.srjlove.apero.data.model.User
import retrofit2.Response


interface ApiHelper {

    suspend fun getUsers(): Response<List<User>>

    suspend fun getUpcomingMovies(): Response<Upcomming>
    suspend fun getPlayingMovies(pageNo: Int): Response<Playing>

}