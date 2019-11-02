package com.example.data.pictures

import com.example.data.DataLogger
import com.example.data.pictures.service.PicturesService
import com.example.data.pictures.storage.PicturesStorage
import com.example.domain.gateway.PictureGateway
import com.example.domain.model.Picture
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import javax.inject.Inject



class PicturesGatewayImpl @Inject constructor(val service: PicturesService, val storage: PicturesStorage, val logger: DataLogger) :
    PictureGateway {



    override fun loadPictures(page: Int): Single<String> =
        service.gePicturesInfo(page)
            .flatMap {
                    picture ->
                Single.just(picture)
                    .flatMap(PicturesFromServiceForStorage())
                    .flatMap { storage.putPictures(it) }
                    .subscribeOn(Schedulers.newThread())
    }

    override fun retrievePictures(): Single<List<Picture>> =
        storage.getAllPictures()
            .flatMapObservable { f -> Observable.fromIterable(f) }
            .map(PictureFromStorageToPresentation())
            .toList()

}