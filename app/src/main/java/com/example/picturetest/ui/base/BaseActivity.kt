package com.example.picturetest.ui.base

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.picturetest.PicturesApp
import com.example.picturetest.R
import com.example.picturetest.di.component.ActivityComponent
import com.example.picturetest.di.component.DaggerActivityComponent
import com.example.picturetest.di.modul.ActivityModule
import com.example.picturetest.ui.LoadingDialog
import com.example.picturetest.ui.LoadingView
import com.example.picturetest.util.loge
import com.example.picturetest.util.logi
import java.io.BufferedInputStream
import java.io.File
import java.io.FileNotFoundException



abstract class BaseActivity : AppCompatActivity(), IView, BaseFragment.Callback {

    lateinit var activityComponent: ActivityComponent

    private var mLoadingView: LoadingView? = null

    protected val fm = supportFragmentManager

    private var background: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as PicturesApp).appComponent)
                .build()
        mLoadingView = LoadingDialog.view(supportFragmentManager)
    }


    override fun onDestroy() {
        super.onDestroy()
        releaseBackground()
    }


    override fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        } else false
    }

    override fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    protected fun drawBackground(background: ImageView, fileName: String) {
        this.background = background
        val dir = "drawable"
        val assets = assets
        try {
            BufferedInputStream(assets.open(dir + File.separator + fileName)).use {
                background.setImageDrawable(BitmapDrawable(resources, BitmapFactory.decodeStream(it)))
            }
        } catch (e: FileNotFoundException) {
            "Can't get bitmap from assets: $dir/$fileName".loge(e)
        }
    }

    private fun releaseBackground() {
        if (this.background?.drawable is BitmapDrawable) {
            (this.background?.drawable as BitmapDrawable).bitmap?.recycle()
        }
    }

    fun replaceFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean, animate: Boolean) {
        val fragmentName = fragment.javaClass.simpleName
        replaceFragment(fragment, fragmentName, containerId, addToBackStack, animate)
    }

    fun replaceFragment(fragment: Fragment, fragmentName: String, containerId: Int, addToBackStack: Boolean, animate: Boolean) {
        if (!isFinishing) {
            if (fm.findFragmentByTag(fragmentName) == null) {
                fm.beginTransaction().apply {
                   // if (animate) setCustomAnimations(R.anim.fade_in, 0)
                    replace(containerId, fragment, fragmentName)
                    if (addToBackStack) addToBackStack(fragmentName)
                }.commitAllowingStateLoss()
            }
        }
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            mLoadingView?.showLoadingIndicator(getString(R.string.please_wait))
        } else {
            mLoadingView?.hideLoadingIndicator()
        }
    }

    protected fun showDialog(title: String, message: String){
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, which ->  message.logi()}
                .show()
    }
}