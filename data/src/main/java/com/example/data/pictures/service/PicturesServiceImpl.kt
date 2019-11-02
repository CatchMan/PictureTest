package com.example.data.pictures.service

import com.example.data.ApiConst
import io.reactivex.Single
import javax.inject.Inject



class PicturesServiceImpl @Inject constructor(val picturesApi: PicturesApi): PicturesService {

    override fun gePicturesInfo(page: Int): Single<List<PictureInfo>> = this.picturesApi.getPictures(page, ApiConst.LIMIT)
}