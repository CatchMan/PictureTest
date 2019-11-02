package com.example.domain.gateway

import com.example.domain.model.Picture
import io.reactivex.Single



interface PictureGateway {

    fun loadPictures(page: Int): Single<String>

    fun retrievePictures(): Single<List<Picture>>

}