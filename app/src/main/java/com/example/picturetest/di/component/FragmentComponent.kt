package com.example.picturetest.di.component

import com.example.picturetest.di.PerFragment
import com.example.picturetest.di.modul.ActivityModule
import com.example.picturetest.di.modul.FragmentModule
import dagger.Component




@PerFragment
@Component(dependencies = [(ApplicationComponent::class)], modules = [(FragmentModule::class),(ActivityModule::class)])
interface FragmentComponent {

}