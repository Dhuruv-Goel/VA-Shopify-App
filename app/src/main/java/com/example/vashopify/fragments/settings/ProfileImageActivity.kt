package com.example.vashopify.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.vashopify.R
import com.example.vashopify.databinding.ActivityProfileImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileImageActivity : AppCompatActivity(){
    private val binding by lazy {
        ActivityProfileImageBinding.inflate(layoutInflater)
    }
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    //   private val imageArgs by navArgs<ProfileImageActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val startEditActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
//                val data = result.data
            }
        }
        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                if(result.resultCode == RESULT_OK){
                    result?.data?.data?.let{
                        imageUri = it
                        val intent1 = Intent(this@ProfileImageActivity, EditImageActivity::class.java).apply {
                            putExtra("editImageUri", it.toString())
                        }
                        startEditActivityForResult.launch(intent1)
                    }
                }

            }
        this.onBackPressedDispatcher.addCallback(this@ProfileImageActivity) {
            onBackButtonPressed()
        }

        val imageUrl = intent.getStringExtra("imageUrl")!!
        showProfileImage(imageUrl)

        binding.imageCloseProfileImage.setOnClickListener {
            onBackButtonPressed()
        }
    binding.btnimageEdit.setOnClickListener {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imageActivityResultLauncher.launch(intent)

    }

}


    private fun onBackButtonPressed() {
        val resultIntent = Intent()
//        resultIntent.putExtra("result_key", "result_value")
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