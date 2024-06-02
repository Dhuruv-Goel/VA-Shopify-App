package com.example.vashopify

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class VAShopifyApplication:Application() {

//    override fun onCreate() {
//        super.onCreate()
//        val cloudinary = Cloudinary(CloudinaryConfig().apply {
//            cloudName = CloudinaryConfig.CloudinaryConfig.cloudName
//            apiKey = CloudinaryConfig.apiKey
//            apiSecret = CloudinaryConfig.apiSecret
//        })

//        CloudinaryConfig.initialize(context = this)

//        val config: MutableMap<String, String> = HashMap<String, String>()
//        config["cloud_name"] = CloudinaryConfig.cloudName
//        config["api_key"] = CloudinaryConfig.apiKey
//        config["api_secret"] = CloudinaryConfig.apiSecret
//        config["cloudinary_url"] = CloudinaryConfig.Cloudinary_Url

//        MediaManager.init(applicationContext, config)

//    }

}