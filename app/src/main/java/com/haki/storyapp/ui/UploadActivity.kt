package com.haki.storyapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.haki.storyapp.R
import com.haki.storyapp.customView.MyButton
import com.haki.storyapp.customView.MyDeskEditText
import com.haki.storyapp.databinding.ActivityUploadBinding
import com.haki.storyapp.di.getImageUri
import com.haki.storyapp.di.reduceFileImage
import com.haki.storyapp.di.uriToFile
import com.haki.storyapp.repo.ResultState
import com.haki.storyapp.ui.viewModel.UploadViewModel
import com.haki.storyapp.ui.viewModel.ViewModelFactory

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private var currentImageUri: Uri? = null

    private lateinit var myButton: MyButton
    private lateinit var myDeskEditText: MyDeskEditText

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, getString(R.string.permis_granted), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.permis_denied), Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        myButton = binding.btnUpload
        myDeskEditText = binding.etDesk

        myDeskEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButton(true)
            }

            override fun afterTextChanged(s: Editable?) {
                //Do Nothing
            }

        })

        binding.btnCam.setOnClickListener { startCamera() }
        binding.btnGal.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImg() }

    }

    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImg()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private fun showImg() {
        currentImageUri?.let {
            Log.d("the URI", "showImage: $it")
            binding.ivFoto.setImageURI(it)
            binding.ivFoto.setPadding(0, 0, 0, 0)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImg()
        }
    }

    private fun uploadImg() {
        currentImageUri?.let { uri ->
            val imgFile = uriToFile(uri, this).reduceFileImage()
            Log.d("the File", "imagepath: ${imgFile.path}")
            val description = binding.etDesk.text.toString()

            viewModel.upload(imgFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                            setMyButton(false)
                        }

                        is ResultState.Success -> {
                            showToast(result.data.message)
                            showLoading(false)
                            setMyButton(true)

                            val intent = Intent(this@UploadActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                            setMyButton(true)
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showSnackBar(getString(R.string.img_notfound))

    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.close)) { }
            .setActionTextColor(getColor(R.color.secCol))
            .show()
    }

    private fun setMyButton(isEnable: Boolean) {
        myButton.isEnabled = isEnable && (binding.etDesk.text?.trim().toString() != "")

    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnUpload.text = if (isLoading) "" else getString(R.string.upload)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}