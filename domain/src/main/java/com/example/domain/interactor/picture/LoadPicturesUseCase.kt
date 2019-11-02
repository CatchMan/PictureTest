package com.example.domain.interactor.picture

import com.example.domain.executor.PostExecutionThread
import com.example.domain.executor.ThreadExecutor
import com.example.domain.gateway.PictureGateway
import com.example.domain.interactor.UseCase
import com.example.domain.model.Picture
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class LoadPicturesUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    compositeDisposable: CompositeDisposable,
    private val sferaGateway: PictureGateway
) : UseCase<List<Picture>, LoadPicturesUseCase.Params>(threadExecutor, postExecutionThread, compositeDisposable) {

    override fun buildUseCaseObservable(params: Params): Single<List<Picture>> = sferaGateway.retrievePictures()

    class Params
}