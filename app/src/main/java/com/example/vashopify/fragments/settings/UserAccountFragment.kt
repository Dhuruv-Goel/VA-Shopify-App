package com.example.vashopify.fragments.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vashopify.R
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.UserResponse
import com.example.vashopify.databinding.FragmentUserAccountBinding
import com.example.vashopify.fragments.settings.data.ui.AccountUiEvent
import com.example.vashopify.fragments.settings.viewmodel.UserAccountViewModel
import com.example.vashopify.shop.ProductEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class UserAccountFragment : Fragment(R.layout.fragment_user_account) {
    private lateinit var binding: FragmentUserAccountBinding
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private lateinit var selectedimageUrl: String
    private lateinit var prefs: SharedPreferences
    private val viewmodel: UserAccountViewModel by viewModels()
//    private var isMediaManagerInitialized = false
//    private val READ_MEDIA_IMAGES_REQUEST_CODE = 101


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserAccountBinding.inflate(inflater)
        val startEditActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the result here
                val data = result.data
                // Perform necessary actions, e.g., update the profile image
            }
        }
        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
                 if(result.resultCode == RESULT_OK){
                     result?.data?.data?.let{
                         imageUri = it
                         val intent1 = Intent(requireContext(), EditImageActivity::class.java).apply {
                             putExtra("editImageUri", it.toString())
                         }
                         startEditActivityForResult.launch(intent1)
                     }
                 }

            }
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedimageUrl = ""
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPressed()
        }
        prefs = requireActivity().getSharedPreferences("prefs",MODE_PRIVATE)
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the result here
                val data = result.data
                // Perform necessary actions, e.g., update the profile image
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.accountUiState.collect {state->
                    if (state.isLoading) {
                        binding.buttonSave.startAnimation()
                    }else {
                        binding.buttonSave.revertAnimation()
                    }

                    if(state.isError) {
                        showToast("Enter Valid Email")
                    }
                }
            }

        }
        binding.apply {
            edFirstName.addTextChangedListener {  text ->
                viewmodel.onEvent( AccountUiEvent.FirstNameChanged(text.toString()))
            }
            edLastName.addTextChangedListener {  text ->
                viewmodel.onEvent( AccountUiEvent.LastNameChanged(text.toString()))
            }
            edEmail.addTextChangedListener {  text ->
                viewmodel.onEvent( AccountUiEvent.EmailChanged(text.toString()))
            }
            buttonSave.setOnClickListener {
                viewmodel.onEvent(AccountUiEvent.Update)
//                viewmodel.getAccount()
            }
        }
        observeUser()
        observeUpdateUser()
        binding.imageCloseUserAccount.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_profileFragment2)
        }
        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)

        }


        binding.imageUser.setOnClickListener {
            val profileImage = prefs.getString("profileImageUrl","")
            val intent = Intent(requireContext(), ProfileImageActivity::class.java).apply {
                putExtra("imageUrl", profileImage)
            }
            startForResult.launch(intent)
        }

//        binding.buttonSave.setOnClickListener {
//
//            binding.apply {
//                val firstName = edFirstName.text.toString().trim()
//                val lastName = edLastName.text.toString().trim()
//                val email = edEmail.text.toString().trim()
//
//                    val user = User(firstName, lastName, email, selectedimageUrl)
//                    viewmodel.updateUser(user)
//                    viewmodel.getAccount()
////                binding.buttonSave.startAnimation()
//
//            }
//        }
        viewmodel.getAccount()
//        viewmodel.getAccount()
    }
    private fun onBackButtonPressed() {
        findNavController().navigate(R.id.action_userAccountFragment_to_profileFragment2)

    }
//
//    private fun upload(){
//        val filesDir = requireContext().filesDir
//        val file = File(filesDir,"image.png")
//        val inputStream = imageUri?.let { requireContext().contentResolver.openInputStream(it) }
//        val outputStream = FileOutputStream(file)
//        inputStream!!.copyTo(outputStream)
//
//
//        val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
//        val part = MultipartBody.Part.createFormData("image",file.name,requestBody)
//
//        viewmodel.uploadImg(part)
//    }


    //    private fun uploadCallback(
//    ): UploadCallback = object : UploadCallback {
//        override fun onStart(requestId: String?) {
//        }
//
//        override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
////            binding.buttonSave.startAnimation()
////            showToast("Loading..")
//        }
//
//        override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
//            val imageUrl = resultData?.get("url")?.toString()
//            selectedimageUrl = imageUrl ?: ""
//
//            Log.d("image", selectedimageUrl)
//        }
//
//        override fun onError(requestId: String?, error: ErrorInfo?) {
////            binding.buttonSave.revertAnimation()
////            showToast("Error")
//        }
//
//        override fun onReschedule(requestId: String?, error: ErrorInfo?) {
//            // Handle reschedule if needed
//        }
//    }
    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.user.collect { response ->
                    when (response) {
                        is ProductEvent.Loading -> {
                            showUserLoading()
                        }

                        is ProductEvent.AuthSuccess -> {
                            hideUserLoading()
                            Log.d("User", response.user.toString())
                            showUserInformation(response.user)
                            prefs.edit()
                                .putString("userName", "${response.user.firstName} ${response.user.lastName}")
                                .putString("profileImageUrl", response.user.profileImageUrl)
                                .apply()
                        }

                        is ProductEvent.Failure -> {
                            hideUserLoading()
                        }

                        is ProductEvent.Unauthorised -> {
                            hideUserLoading()
                            showToast("Login Again")

                        }

                        else -> Unit
                    }


                }
            }
        }
    }


    private fun observeUpdateUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.accountResult.collect { result ->
                when (result) {

                    is AuthResult.Authorized -> {
                        binding.buttonSave.revertAnimation()
                        viewmodel.getAccount()
                    }

                    is AuthResult.Unauthorized -> {
                        binding.buttonSave.revertAnimation()
                    }

                    is AuthResult.UnknownError -> {
                        binding.buttonSave.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

    }

    private fun showUserInformation(data: UserResponse) {
        binding.apply {
            Glide.with(this@UserAccountFragment)
                .load(data.profileImageUrl).error(R.drawable.ic_profile).into(imageUser)
            edFirstName.setText(data.firstName)
            edLastName.setText(data.lastName)
            edEmail.setText(data.email)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.GONE
            imageUser.visibility = View.VISIBLE
            imageEdit.visibility = View.VISIBLE
            edFirstName.visibility = View.VISIBLE
            edLastName.visibility = View.VISIBLE
            edEmail.visibility = View.VISIBLE
            tvUpdatePassword.visibility = View.VISIBLE
            buttonSave.visibility = View.VISIBLE
        }
    }

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            imageUser.visibility = View.INVISIBLE
            imageEdit.visibility = View.INVISIBLE
            edFirstName.visibility = View.INVISIBLE
            edLastName.visibility = View.INVISIBLE
            edEmail.visibility = View.INVISIBLE
            tvUpdatePassword.visibility = View.INVISIBLE
            buttonSave.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.getAccount()
    }
}
