package com.example.vashopify

import android.content.Context
import android.util.Log
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Properties
class CloudinaryConfig {
    object CloudinaryConfig {
//        private var appContext: Context? = null
//        fun initialize(context: Context) {
//             appContext = context.applicationContext
//        }


        val cloudName: String by lazy {
            getLocalProperty("cloudinaryCloudName") ?: ""
        }

        //
//        val apiKey: String by lazy {
//            getLocalProperty("cloudinaryApiKey") ?: ""
//        }
//
//        val apiSecret: String by lazy {
//            getLocalProperty("cloudinaryApiSecret") ?: ""
//        }
        val Cloudinary_Url: String by lazy {
            getLocalProperty("CLOUDINARY_URL") ?: ""
        }

        private fun getLocalProperty(propertyName: String): String {
            return try {
                val inputStream =
                    CloudinaryConfig::class.java.classLoader?.getResourceAsStream("local.properties")
                        ?: throw FileNotFoundException("local.properties file not found")
             Log.d("media", "$inputStream   input")
                val properties = Properties()
                properties.load(inputStream)

                val value = properties.getProperty(propertyName)
                    ?: throw IllegalArgumentException("$propertyName not found in local.properties")

                value
            } catch (e: FileNotFoundException) {
                throw RuntimeException("Error loading properties: ${e.message}", e)
            } catch (e: IOException) {
                throw RuntimeException("Error reading properties file: ${e.message}", e)
            } catch (e: Exception) {
                throw RuntimeException("Unexpected error: ${e.message}", e)
            }
        }
//        private fun getLocalProperty(propertyName: String): String? {
//            return try {
//                val inputStream = CloudinaryConfig::class.java.classLoader?.getResourceAsStream("local.properties")
//                val properties = Properties()
//                properties.load(inputStream)
//                properties.getProperty(propertyName)
//            } catch (e: Exception) {
//                null
//            }
//        }
//    private fun getLocalProperty(propertyName: String): String {
//        return try {
//            val inputStream = CloudinaryConfig::class.java.classLoader?.getResourceAsStream("local.properties")
//            val properties = Properties()
//            properties.load(inputStream)
//            properties.getProperty(propertyName) ?: throw IllegalArgumentException("$propertyName not found in local.properties")
//        } catch (e: Exception) {
//            throw RuntimeException("Error loading properties: ${e.message}", e)
//        }
//    }

//   private fun getProperty(propertyName:String):String? {
//        val localPropertiesFile = rootProject.file("local.properties")
//        if (localPropertiesFile.exists()) {
//            val localProperties =  Properties()
//            localProperties.load( FileInputStream (localPropertiesFile))
//
//            // Set buildConfigField for RAZORPAY_KEY
////            buildConfigField("String", "RAZORPAY_KEY", "\"${localProperties.getProperty('RAZORPAY_KEY')}\"")
//
//            // Set buildConfigField for CLOUDINARY_URL
//        } else {
//            // Throw an exception if local.properties is not found
//            throw  GradleException ("local.properties file not found.")
//        }
//    }
    }
}

