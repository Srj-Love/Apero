package com.srjlove.apero.data.api


import com.srjlove.apero.data.model.Playing
import com.srjlove.apero.data.model.Upcomming
import com.srjlove.apero.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService  {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("upcoming?api_key=8a4d9aeb54162b98e3c3b4ac335734df&language=en-US&page=1")
    suspend fun getUpcomingMovies(): Response<Upcomming>


    @GET("now_playing?api_key=8a4d9aeb54162b98e3c3b4ac335734df&language=en-US")
    suspend fun getPlayingMovies(@Query("page") pageNo: Int): Response<Playing>
}