package com.srjlove.apero.data.api

import com.srjlove.apero.data.model.Playing
import com.srjlove.apero.data.model.Upcomming
import com.srjlove.apero.data.model.User
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getUsers(): Response<List<User>> = apiService.getUsers()

    override suspend fun getUpcomingMovies(): Response<Upcomming> = apiService.getUpcomingMovies()
    override suspend fun getPlayingMovies(pageNo: Int): Response<Playing> = apiService.getPlayingMovies(pageNo)

}