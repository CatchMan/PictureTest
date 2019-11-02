package com.example.picturetest.ui.base

import io.reactivex.disposables.CompositeDisposable


/**
 * Created by ujujzk on 17.07.2018
 * Softensy Digital Studio
 * softensiteam@gmail.com
 */

abstract class BasePresenter<V : IView> constructor(protected val compositeDisposable: CompositeDisposable) :
    IPresenter<V> {

    @Volatile
    var view: V? = null

    override fun bindView(view: V) {
        this.view = view
    }

    override fun unbindView() {
        this.view = null
    }

    override fun dispose() = compositeDisposable.dispose()

    fun isViewAttached() = this.view != null

}