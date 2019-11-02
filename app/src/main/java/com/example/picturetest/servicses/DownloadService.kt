package com.example.picturetest.servicses

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Environment

import androidx.core.app.NotificationCompat

import com.example.picturetest.R

import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class DownloadService : IntentService("DownloadService") {

    internal lateinit var notificationManager: NotificationManager
    internal lateinit var myNotification: Notification
    internal var folder_main = "ImagesFolder"

    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onHandleIntent(intent: Intent?) {

        val urlToDownload = intent?.getStringExtra("url")

        try {
            val url = URL(urlToDownload)
            val connection = url.openConnection()
            connection.connect()

            val input = BufferedInputStream(connection.getInputStream())


            val outerFolder = File(Environment.getExternalStorageDirectory(), folder_main)
            val inerDire =
                File(outerFolder.absoluteFile, System.currentTimeMillis().toString() + ".jpg")

            if (!outerFolder.exists()) {
                outerFolder.mkdirs()
            }

            inerDire.createNewFile()

            val output = FileOutputStream(inerDire)

            val data = ByteArray(1024)
            var count: Int = input.read(data)
            while (count != -1) {
                output.write(data, 0, count)
                count = input.read(data)
            }

            output.flush()
            output.close()
            input.close()

        } catch (e: IOException) {
            e.printStackTrace()

        }

        sendNotification()

    }

    internal fun sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder: NotificationCompat.Builder
            val pendingIntent: PendingIntent
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel: NotificationChannel? = null
            mChannel = NotificationChannel("0", "myChanal", importance)
            mChannel.description = "Download finish"
            mChannel.enableVibration(true)
            notificationManager.createNotificationChannel(mChannel)
            builder = NotificationCompat.Builder(this, "0")

            builder.setContentTitle(getString(R.string.download_picture))
                .setSmallIcon(R.mipmap.ic_launcher_round) // required
                .setContentText(getString(R.string.picture_download_compleate))  // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            val notification = builder.build()
            notificationManager.notify(0, notification)
        } else {
            myNotification = NotificationCompat.Builder(applicationContext, "0")
                .setContentTitle(getString(R.string.download_picture))
                .setContentText(getString(R.string.picture_download_compleate))
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build()
            notificationManager.notify(MY_NOTIFICATION_ID, myNotification)
        }

    }

    companion object {

        private val MY_NOTIFICATION_ID = 1
    }

}
