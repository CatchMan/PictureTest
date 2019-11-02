package com.example.domain.interactor

import com.example.domain.executor.PostExecutionThread
import com.example.domain.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


abstract class UseCase<T, Params> (
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread,
    private val compositeDisposable: CompositeDisposable) : Disposable {

    abstract fun buildUseCaseObservable(params: Params) : Single<T>


    fun execute(observer: DisposableSingleObserver<T>, params: Params){
        val observable = this.buildUseCaseObservable(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer) as Disposable)
    }

    protected fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    override fun dispose(){
        if (!compositeDisposable.isDisposed){
            compositeDisposable.dispose()
        }
    }

    override fun isDisposed(): Boolean = compositeDisposable.isDisposed

    protected fun loading(boolean: Boolean){

    }
}