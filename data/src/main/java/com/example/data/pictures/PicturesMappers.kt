package com.example.data.pictures

import com.example.data.pictures.service.PictureInfo
import com.example.data.pictures.storage.StoragePictureInfo
import com.example.domain.model.Picture

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function








class PicturesFromServiceForStorage : Function<List<PictureInfo>, Single<List<StoragePictureInfo>>> {
    override fun apply(t: List<PictureInfo>): Single<List<StoragePictureInfo>> =
        Observable.fromIterable(t)
            .map { StoragePictureInfo(it.id, it.author, it.width, it.height, it.url, it.downloadUrl) }
            .toList()
}

class PictureFromStorageToPresentation : Function<StoragePictureInfo, Picture> {
    override fun apply(t: StoragePictureInfo): Picture =
        Picture(t.id, t.author, t.width, t.height, t.url, t.downloadUrl)

}
