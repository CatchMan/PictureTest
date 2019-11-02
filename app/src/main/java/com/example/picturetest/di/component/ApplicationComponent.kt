package com.example.picturetest.di.component

import android.app.Application
import android.content.Context
import com.example.picturetest.PicturesApp
import com.example.picturetest.di.ApplicationContext
import com.example.picturetest.di.modul.ApplicationModule
import com.example.domain.executor.PostExecutionThread
import com.example.domain.executor.ThreadExecutor
import com.example.domain.gateway.PictureGateway
import dagger.Component
import javax.inject.Singleton



@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun inject(app: PicturesApp)

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun threadExecutor(): ThreadExecutor
    fun postExecutionThread(): PostExecutionThread

    fun countryGateway(): PictureGateway

}