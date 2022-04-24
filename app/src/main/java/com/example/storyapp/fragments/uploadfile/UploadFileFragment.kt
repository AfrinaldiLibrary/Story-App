package com.example.storyapp.fragments.uploadfile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.storyapp.R
import com.example.storyapp.data.PrefManager
import com.example.storyapp.databinding.FragmentUploadFileBinding
import com.example.storyapp.helper.reduceFileImage
import com.example.storyapp.helper.uriToFile
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

class UploadFileFragment : Fragment() {
    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UploadViewModel>()
    private lateinit var prefManager: PrefManager

    private var getFile: File? = null
    private var camera: Bitmap? = null
    private lateinit var safeContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Snackbar.make(binding.root, R.string.not_grant, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        prefManager = PrefManager(requireContext())

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        val token = prefManager.getToken().toString()
        if (getFile != null){
            val file = reduceFileImage(getFile as File)
            val imageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val description = binding.etDesc.text.toString()
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                imageFile
            )

            viewModel.upload(token, description, imageMultipart)
            viewModel.isSuccess.observe(viewLifecycleOwner){
                if(it){
                    val toStoriesFragment = UploadFileFragmentDirections.actionUploadFileFragmentToStoriesFragment()

                    view?.findNavController()?.navigate(toStoriesFragment)
                }
            }

            viewModel.isLoading.observe(viewLifecycleOwner){
                showProgressBar(it)
            }
        } else {
            Snackbar.make(binding.root, R.string.no_image, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih photo")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImg)
        }
    }

    private fun startCameraX() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_uploadFileFragment_to_cameraFragment)
        getResultCamera()
    }

    private fun getResultCamera(){
        setFragmentResultListener(
            ADD_RESULT
        ) { _, result ->
            val myFile = result.getSerializable("picture") as File

            getFile = myFile
            camera = BitmapFactory.decodeFile(getFile?.path)
            binding.ivPreview.setImageBitmap(camera)
        }
    }

    companion object {
        const val ADD_RESULT = "add_result"

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}