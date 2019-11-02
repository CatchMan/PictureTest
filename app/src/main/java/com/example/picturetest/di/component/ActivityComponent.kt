package com.example.picturetest.di.component

import com.example.picturetest.di.PerActivity
import com.example.picturetest.di.modul.ActivityModule
import com.example.picturetest.ui.mainList.MainActivity
import dagger.Component




@PerActivity
@Component(dependencies = [(ApplicationComponent::class)], modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)
//
//    fun inject(profileActivity: ProfileActivity)


}