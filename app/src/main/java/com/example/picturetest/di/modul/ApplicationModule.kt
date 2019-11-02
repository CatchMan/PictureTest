package com.example.picturetest.di.modul

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.picturetest.util.dataLogger
import com.example.picturetest.util.domainLogger
import com.example.picturetest.util.netLogger
import com.catchman.data.interceptor.LoggingInterceptor
import com.example.picturetest.PicturesApp
import com.example.data.JobExecutor
import com.example.picturetest.UiThread
import com.example.data.AppDatabase
import com.example.picturetest.di.ApplicationContext
import com.example.data.ApiConst
import com.example.data.DataLogger
import com.example.data.pictures.PicturesGatewayImpl
import com.example.data.pictures.service.PicturesApi
import com.example.data.pictures.service.PicturesService
import com.example.data.pictures.service.PicturesServiceImpl
import com.example.data.pictures.storage.PicturesStorage
import com.example.data.pictures.storage.PicturesStorageImpl
import com.example.domain.DomainLogger
import com.example.domain.executor.PostExecutionThread
import com.example.domain.executor.ThreadExecutor
import com.example.domain.gateway.PictureGateway
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class ApplicationModule(private val app: Application) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context = app

    @Provides
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides
    @Singleton
    fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread = uiThread

    @Provides
    fun provideDataLogger(): DataLogger = dataLogger()

    @Provides
    fun provideDomainLogger(): DomainLogger = domainLogger()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        val logging = LoggingInterceptor(object : LoggingInterceptor.Logger {
            override fun log(message: String) = netLogger(message)
        }, app).setLevel(LoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
                .addInterceptor(logging)
            .addInterceptor(ChuckInterceptor(PicturesApp.context))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideCountryApi(client: OkHttpClient): PicturesApi {
        return Retrofit.Builder()
                .baseUrl(ApiConst.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(PicturesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSferaService(countryService: PicturesServiceImpl): PicturesService = countryService

    @Provides
    @Singleton
    fun provideSferaStorage(gitHubStorage: PicturesStorageImpl): PicturesStorage = gitHubStorage

    @Provides
    @Singleton
    fun provideSferaGateway(countryGateway: PicturesGatewayImpl): PictureGateway = countryGateway

    @Provides
    @Singleton
    fun provideRoomDatabase() =
            Room.databaseBuilder(app.applicationContext, AppDatabase::class.java, "country-database")
                    .fallbackToDestructiveMigration()
                    .build()

}