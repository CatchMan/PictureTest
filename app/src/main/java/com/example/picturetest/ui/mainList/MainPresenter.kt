package com.example.picturetest.ui.mainList

import com.example.domain.interactor.picture.GetPicturesUseCase
import com.example.domain.interactor.picture.LoadPicturesUseCase
import com.example.domain.model.Picture
import com.example.picturetest.ui.base.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class MainPresenter <V : MainContract.View> @Inject constructor(
    val getPicturesUseCase: GetPicturesUseCase,
    val loadPicturesUseCase: LoadPicturesUseCase,
    compositeDisposable: CompositeDisposable
)
    : BasePresenter<V>(compositeDisposable), MainContract.Presenter<V> {

    override fun retrievePictures() {

        loadPicturesUseCase.execute(object : DisposableSingleObserver<List<Picture>>(){
            override fun onSuccess(t: List<Picture>) {
                view?.getPicturesSuccess(t)
            }

            override fun onError(e: Throwable) {
                view?.getPicturesFail()
            }
        }, LoadPicturesUseCase.Params())
    }

    override fun loadPictures(page: Int) {
        getPicturesUseCase.execute(object : DisposableSingleObserver<String>(){
            override fun onSuccess(t: String) {
                retrievePictures()
            }

            override fun onError(e: Throwable) {
                retrievePictures()
            }
        }, GetPicturesUseCase.Params(page))
    }
}