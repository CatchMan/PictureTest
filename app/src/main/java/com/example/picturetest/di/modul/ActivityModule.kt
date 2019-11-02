package com.example.picturetest.di.modul

import androidx.appcompat.app.AppCompatActivity
import com.example.picturetest.di.ActivityContext
import com.example.picturetest.di.PerActivity
import com.example.picturetest.ui.mainList.MainContract
import com.example.picturetest.ui.mainList.MainPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @ActivityContext
    fun provideContext() = activity

    @Provides
    fun provideActivity() = activity

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @PerActivity
    fun provideMainPresenter (mainPresenter: MainPresenter<MainContract.View>) :
            MainContract.Presenter<MainContract.View> = mainPresenter
//
//    @Provides
//    @PerActivity
//    fun provideUserListPresenter (userListPresenter: UserListPresenter<UserListContract.View>) :
//            UserListContract.Presenter<UserListContract.View> = userListPresenter

}