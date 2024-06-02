package com.example.vashopify.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.vashopify.R
import com.example.vashopify.databinding.ActivityAddressBinding
import com.example.vashopify.databinding.ActivityProfileImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileImageActivity : AppCompatActivity(){
    private val binding by lazy {
        ActivityProfileImageBinding.inflate(layoutInflater)
    }
//   private val imageArgs by navArgs<ProfileImageActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        this.onBackPressedDispatcher.addCallback(this@ProfileImageActivity) {
            onBackButtonPressed()
        }

        val imageUrl = intent.getStringExtra("imageUrl")!!
        showProfileImage(imageUrl)

        binding.imageCloseProfileImage.setOnClickListener {
            onBackButtonPressed()
        }
    }


    private fun onBackButtonPressed() {
        val resultIntent = Intent()
//        resultIntent.putExtra("result_key", "result_value") // Add any result data you need
        setResult(RESULT_OK, resultIntent)
        finish()

    }
    private fun showProfileImage(image: String){
       binding.apply {
           Glide.with(this@ProfileImageActivity)
               .load(image)
               .error(R.drawable.ic_profile)
               .into(profileImage)
       }
    }
}