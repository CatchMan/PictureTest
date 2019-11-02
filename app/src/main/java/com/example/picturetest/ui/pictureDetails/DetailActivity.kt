package com.example.picturetest.ui.pictureDetails

import android.content.Context
import android.content.Intent
import com.example.domain.model.Picture
import com.example.picturetest.R
import com.example.picturetest.ui.base.BaseActivity
import android.animation.AnimatorListenerAdapter
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.PopupMenu
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.picturetest.util.loge
import kotlinx.android.synthetic.main.activity_detail.*
import com.androidnetworking.AndroidNetworking
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.*
import androidx.core.app.ActivityCompat
import com.example.picturetest.servicses.DownloadService


class DetailActivity : BaseActivity() {

    private var isViewVisible = true
    private lateinit var picture: Picture
    private val imageDomainLink = "https://picsum.photos/id/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_detail)
        AndroidNetworking.initialize(applicationContext)
        picture = intent.getSerializableExtra(EXTRA_PICTURE) as Picture
        tvImageSize.text = "${picture.width} x ${picture.height}"
        tvAuthor.text = picture.author
        ivBack.setOnClickListener { onBackPressed() }
        ivShare.setOnClickListener { initPopapMenu(false, ivShare) }
        btnDownload.setOnClickListener { initPopapMenu(true, btnDownload) }
        Glide
            .with(this)
            .load(picture.downloadUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivPicture)
        ivPicture.setOnClickListener {
            showHideToolbar()
        }
    }

    private fun showHideToolbar(){
        if(isViewVisible) {
            topToolbar.animate()
                .translationY(0f)
                .setDuration(700)
                .alpha(0.0f)
                .setListener(object : AnimatorListenerAdapter() {})

            bottonToolbar.animate()
                .translationY(0f)
                .setDuration(700)
                .alpha(0.0f)
                .setListener(object : AnimatorListenerAdapter() {})
        } else {
            topToolbar.animate()
                .translationY(0f)
                .setDuration(700)
                .alpha(1.0f)
                .setListener(object : AnimatorListenerAdapter(){})

            bottonToolbar.animate()
                .translationY(0f)
                .setDuration(700)
                .alpha(1.0f)
                .setListener(object : AnimatorListenerAdapter(){})
        }
        isViewVisible = !isViewVisible
    }

    private fun initPopapMenu(isDownLoad: Boolean, view: View) {
        val popupMenu = PopupMenu(this, view)
        // popupMenu.inflate(R.menu.popupmenu); // если добавлять к существующему меню
        popupMenu.menu.add(0, 0, 0, "Phone size ${getScreenResolution()}")
        popupMenu.menu.add(0, 1, 0, "Max size (${picture.width} x ${picture.width}) ")
        popupMenu.setOnMenuItemClickListener {
            val url = if(it.itemId == 0) {
                val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = wm.defaultDisplay
                val metrics = DisplayMetrics()
                display.getMetrics(metrics)
                imageDomainLink + picture.downloadUrl.split("/")
                    .mapNotNull { t -> t.toIntOrNull() }.first() + "/" + metrics.widthPixels + "/" +
                         metrics.heightPixels
            } else {
                picture.downloadUrl
            }
            url.loge()
            if(isDownLoad) {
                downloadImage(url)
            } else {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    shareIntent.putExtra(Intent.EXTRA_TEXT, url)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch (e: Exception) {
                    //e.toString();
                }

            }
            true
        }
        popupMenu.show()
    }

    private fun getScreenResolution(): String {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        return "($width x $height)"
    }

    private fun downloadImage(url: String) {

        val check = ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        if (check == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(this, DownloadService::class.java)
            intent.putExtra("url", url)
            startService(intent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE),1024)
            }
        }

    }



    companion object {

        const val EXTRA_PICTURE = "login"

        fun start(context: Context, picture: Picture) {
            val starter = Intent(context, DetailActivity::class.java)
            starter.putExtra(EXTRA_PICTURE, picture)
            context.startActivity(starter)
        }
    }

}
