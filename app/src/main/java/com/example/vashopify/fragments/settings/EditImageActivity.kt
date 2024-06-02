package com.example.vashopify.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.vashopify.R
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.databinding.ActivityEditImageBinding
import com.example.vashopify.fragments.settings.viewmodel.EditImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class EditImageActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditImageBinding.inflate(layoutInflater)
    }
    private val viewmodel: EditImageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeUpload()
        observeLoading()
        this.onBackPressedDispatcher.addCallback(this@EditImageActivity) {
            onBackButtonPressed()
        }

        val imageUriString = intent.getStringExtra("editImageUri")
        val imageUri = Uri.parse(imageUriString)
        if (imageUri != null) {
            showProfileImage(imageUri)
        }
        binding.apply {
            editButtonSave.setOnClickListener {
                uploadImage(imageUri)
            }
            imageCloseEditImage.setOnClickListener {
                onBackButtonPressed()
            }
            editButtonCancel.setOnClickListener {
                onBackButtonPressed()
            }
        }


    }

    private fun observeLoading() {
        lifecycleScope.launch {
            viewmodel.showLoadingEdit.collect { showLoading ->
                if (showLoading) {
                    showLoading()

                } else {
                    hideLoading()
                }
            }
        }
    }

    private fun observeUpload() {
        lifecycleScope.launch {
            viewmodel.editResult.collect { result ->
                when (result) {

                    is AuthResult.Authorized -> {
                        hideLoading()
                        showToast("Profile Picture Saved Successfully")
                        onBackButtonPressed()
                    }

                    is AuthResult.Unauthorized -> {
                        hideLoading()
                        showToast("Try Again!!")
                    }

                    is AuthResult.UnknownError -> {
                        hideLoading()
                        showToast("Check Internet Connection!!")
                    }

                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            lleditButton.visibility = View.GONE
            progressbarEditImg.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            lleditButton.visibility = View.VISIBLE
            progressbarEditImg.visibility = View.GONE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun uploadImage(uri: Uri) {
        val filePath = getRealPathFromURI(uri)
        val file = File(filePath)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        viewmodel.uploadImg(part)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        this.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            }
        }
        return path ?: throw IllegalArgumentException("Unable to retrieve path from URI")
    }

    private fun showProfileImage(imageUrl: Uri) {
        binding.apply {
            Glide.with(this@EditImageActivity)
                .load(imageUrl)
                .error(R.drawable.ic_profile)
                .into(editImage)
        }

    }

    private fun onBackButtonPressed() {
        val resultIntent = Intent()
//        resultIntent.putExtra("result_key", "result_value") // Add any result data you need
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}