package com.example.picturetest.ui.mainList

import com.example.data.pictures.storage.StoragePictureInfo
import com.example.domain.model.Picture
import com.example.picturetest.ui.base.IPresenter
import com.example.picturetest.ui.base.IView

class MainContract {

    interface View : IView {

        fun getPicturesSuccess(pictureInfo: List<Picture>)

        fun getPicturesFail()


    }

    interface Presenter<V : View> : IPresenter<V> {

        fun loadPictures(page: Int)

        fun retrievePictures()

    }
}