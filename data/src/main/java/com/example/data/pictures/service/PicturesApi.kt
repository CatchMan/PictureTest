package com.example.data.pictures.service

import io.reactivex.Single
import retrofit2.http.*


interface PicturesApi {

    @GET("v2/list")
    fun getPictures(@Query("page") page: Int, @Query("limit") limit: Int): Single<List<PictureInfo>>


}