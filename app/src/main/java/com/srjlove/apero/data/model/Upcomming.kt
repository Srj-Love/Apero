package com.srjlove.apero.data.model


import com.google.gson.annotations.SerializedName

data class Upcomming(
    @SerializedName("dates")
    var dates: Dates? = Dates(),
    @SerializedName("page")
    var page: Int? = 0, // 1
    @SerializedName("results")
    var results: List<Result> = listOf(),
    @SerializedName("total_pages")
    var totalPages: Int? = 0, // 14
    @SerializedName("total_results")
    var totalResults: Int? = 0 // 263
) {
    data class Dates(
        @SerializedName("maximum")
        var maximum: String? = "", // 2021-01-29
        @SerializedName("minimum")
        var minimum: String? = "" // 2021-01-04
    )

    data class Result(
        @SerializedName("adult")
        var adult: Boolean? = false, // false
        @SerializedName("backdrop_path")
        var backdropPath: String? = "", // /srYya1ZlI97Au4jUYAktDe3avyA.jpg
        @SerializedName("genre_ids")
        var genreIds: List<Int?>? = listOf(),
        @SerializedName("id")
        var id: Int? = 0, // 464052
        @SerializedName("original_language")
        var originalLanguage: String? = "", // en
        @SerializedName("original_title")
        var originalTitle: String? = "", // Wonder Woman 1984
        @SerializedName("overview")
        var overview: String? = "", // Wonder Woman comes into conflict with the Soviet Union during the Cold War in the 1980s and finds a formidable foe by the name of the Cheetah.
        @SerializedName("popularity")
        var popularity: Double? = 0.0, // 7713.476
        @SerializedName("poster_path")
        var posterPath: String? = "", // /8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg
        @SerializedName("release_date")
        var releaseDate: String? = "", // 2020-12-16
        @SerializedName("title")
        var title: String? = "", // Wonder Woman 1984
        @SerializedName("video")
        var video: Boolean? = false, // false
        @SerializedName("vote_average")
        var voteAverage: Double? = 0.0, // 7.3
        @SerializedName("vote_count")
        var voteCount: Int? = 0 // 2077
    )
}