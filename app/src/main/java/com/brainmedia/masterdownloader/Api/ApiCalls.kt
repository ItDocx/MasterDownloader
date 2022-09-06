package com.brainmedia.masterdownloader.Api

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ApiCalls {


    fun DownloadTikTokVideos(context: Context, tikUrl: String){

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com/vid/index?url=$tikUrl")
            .get()
            .addHeader(
                "X-RapidAPI-Key",
                "6006d97495msh704b5f2cab5cd7ep12eb79jsnabb6766fe157"
            )
            .addHeader(
                "X-RapidAPI-Host",
                "tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com"
            )
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context.applicationContext,"Error While Parsing Url", Toast.LENGTH_LONG)
                    .show()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var myresponse = response.body!!.string()

                    val jObj = JSONObject(myresponse)
                    val jArray = jObj.getJSONArray("video") as JSONArray

                    for ( i in 0..0 ){

                        val jArray2 = jArray.getString(i)
                        val dlRequest = DownloadManager.Request(Uri.parse(jArray2))
                        dlRequest.setAllowedOverRoaming(true)
                            .setTitle("TikTok00"+System.currentTimeMillis())
                            .setDescription("Brains Media").setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .allowScanningByMediaScanner()
                        dlRequest.setAllowedOverMetered(true)
                            .setDestinationInExternalFilesDir(context, Environment.
                            DIRECTORY_DOWNLOADS,"TikTok"+System.currentTimeMillis()+".mp4")

                        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                        downloadManager.enqueue(dlRequest)



                    }






                }
            }
        })





    }

    fun DownloadInstaVideos(context: Context,instaUrl:String){

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://instagram-downloader-download-instagram-videos-stories.p.rapidapi.com/index?url=$instaUrl")
            .get()
            .addHeader(
                "X-RapidAPI-Key",
                "4130ce89c1mshd39543e2a88d47dp1877a7jsn0ed7e153cb4c"
            )
            .addHeader(
                "X-RapidAPI-Host",
                "instagram-downloader-download-instagram-videos-stories.p.rapidapi.com"
            )
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context.applicationContext, "Error While Parsing Url", Toast.LENGTH_LONG)
                    .show()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var myresponse = response.body!!.string()

                    val jObj = JSONObject(myresponse)
                    val video = jObj.getString("media")

                    println(video.toString())

                    val dlRequest = DownloadManager.Request(Uri.parse(video.toString()))
                    dlRequest.setAllowedOverRoaming(true)
                        .setTitle("instagram00" + System.currentTimeMillis())
                        .setDescription("Brains Media").setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                        )
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        .allowScanningByMediaScanner()
                    dlRequest.setAllowedOverMetered(true)
                        .setDestinationInExternalFilesDir(
                            context,
                            Environment.DIRECTORY_DOWNLOADS,
                            "Instagram" + System.currentTimeMillis() + ".mp4"
                        )

                    val downloadManagerInsta = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManagerInsta.enqueue(dlRequest)


                }

            }
        })


    }

    fun DownloadFbVideos(context: Context,Fb_Url:String){

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://facebook-reel-and-video-downloader.p.rapidapi.com/app/main.php?url=$Fb_Url")
            .get()
            .addHeader(
                "X-RapidAPI-Key",
                "6006d97495msh704b5f2cab5cd7ep12eb79jsnabb6766fe157"
            )
            .addHeader(
                "X-RapidAPI-Host",
                "facebook-reel-and-video-downloader.p.rapidapi.com"
            )
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context.applicationContext, "Error While Parsing Url", Toast.LENGTH_LONG)
                    .show()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val myresponse = response.body!!.string()


                    val jObj = JSONObject(myresponse)
                    if (myresponse.contains("Download High Quality") && myresponse.contains("Download Low Quality")) {


                        val video = jObj.getJSONObject("links")

                        val hdVideo = video.getString("Download High Quality")

                        println(hdVideo.toString())

                        val dlRequest = DownloadManager.Request(Uri.parse(hdVideo.toString()))
                        dlRequest.setAllowedOverRoaming(true)
                            .setTitle("FB00" + System.currentTimeMillis())
                            .setDescription("Brains Media").setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                            )
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .allowScanningByMediaScanner()
                        dlRequest.setAllowedOverMetered(true)
                            .setDestinationInExternalFilesDir(
                                context,
                                Environment.DIRECTORY_DOWNLOADS,
                                "Facebook" + System.currentTimeMillis() + ".mp4"
                            )

                        val downloadManagerFbHD = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
                        downloadManagerFbHD.enqueue(dlRequest)

                    }
                     else if(myresponse.contains("Download Low Quality") && !myresponse.contains("Download High Quality")) {

                        val video = jObj.getJSONObject("links")

                        val sdVideo = video.getString("Download High Quality")

                        println(sdVideo.toString())

                        val dlRequest = DownloadManager.Request(Uri.parse(sdVideo.toString()))
                        dlRequest.setAllowedOverRoaming(true)
                            .setTitle("FB00" + System.currentTimeMillis())
                            .setDescription("Brains Media").setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                            )
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .allowScanningByMediaScanner()
                        dlRequest.setAllowedOverMetered(true)
                            .setDestinationInExternalFilesDir(
                                context,
                                Environment.DIRECTORY_DOWNLOADS,
                                "Facebook" + System.currentTimeMillis() + ".mp4"
                            )

                        val downloadManagerFbSD =
                            context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
                        downloadManagerFbSD.enqueue(dlRequest)


                    } else {
                        Toast.makeText(
                            context.applicationContext,
                            "Video Not Found",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }

        })


    }







}