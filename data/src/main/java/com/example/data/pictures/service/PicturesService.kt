package com.example.data.pictures.service

import io.reactivex.Single


/**
 * Created by ujujzk on 21.06.2018
 * Softensy Digital Studio
 * softensiteam@gmail.com
 */

interface PicturesService {

    fun gePicturesInfo(page: Int): Single<List<PictureInfo>>

}