package com.example.picturetest.ui.base

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.picturetest.PicturesApp
import com.example.picturetest.di.component.DaggerFragmentComponent
import com.example.picturetest.di.component.FragmentComponent
import com.example.picturetest.di.modul.ActivityModule
import com.example.picturetest.di.modul.FragmentModule
import com.example.picturetest.util.logi


abstract class BaseFragment : Fragment(), IView {

    protected lateinit var fragmentComponent: FragmentComponent

    var activity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity){
            this.activity = context
            fragmentComponent = DaggerFragmentComponent.builder()
                    .fragmentModule(FragmentModule())
                    .activityModule(ActivityModule(context))
                    .applicationComponent((context.application as PicturesApp).appComponent)
                    .build()
            context.onFragmentAttached()
        }
    }

    override fun onDetach() {
        activity = null
        super.onDetach()
    }

    override fun showProgress(show: Boolean) {
        if (isAdded) activity?.showProgress(show)
    }

    override fun isNetworkConnected(): Boolean = activity?.isNetworkConnected() ?: false

    override fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    protected fun showDialog(title: String, message: String){
        AlertDialog.Builder(context!!)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, which ->  message.logi()}
                .show()
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }
}