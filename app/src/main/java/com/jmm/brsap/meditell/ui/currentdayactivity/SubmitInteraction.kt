package com.jmm.brsap.meditell.ui.currentdayactivity

import android.Manifest
import android.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.jmm.brsap.meditell.databinding.FragmentSubmitInteractionBinding
import com.jmm.brsap.meditell.util.BaseFragment
import com.jmm.brsap.meditell.viewmodel.CurrentDayActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.provider.MediaStore

import androidx.core.content.FileProvider

import android.webkit.MimeTypeMap

import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import androidx.annotation.Nullable
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.jmm.brsap.meditell.ui.MainActivity

import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.InteractionModel
import com.jmm.brsap.meditell.util.Status


@AndroidEntryPoint
class SubmitInteraction : BaseFragment<FragmentSubmitInteractionBinding>(FragmentSubmitInteractionBinding::inflate) {

    val CAMERA_PERM_CODE = 101
    val CAMERA_REQUEST_CODE = 102
    val GALLERY_REQUEST_CODE = 105

    private val viewModel by activityViewModels<CurrentDayActivityViewModel>()
    var currentPhotoPath =""
    private val uploadOptions = arrayOf("Camera","Gallery")
    private lateinit var dialogBuilder : AlertDialog.Builder
    private lateinit var uploadOptionsDialog: AlertDialog

    private var userId = ""

    var storageReference: StorageReference? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDialog()
        populateTypeAdapter()
        storageReference = FirebaseStorage.getInstance().reference
        binding.btnUploadPicture.setOnClickListener {
            uploadOptionsDialog.show()
        }

        binding.btnSubmit.setOnClickListener {
//            showToast("clicked")
            viewModel.addNewInteraction(InteractionModel(
                interactedWith = viewModel.selectedInteractedId,
                interactionWasWith = viewModel.selectedInteractedWith,
                areaId = viewModel.selectedAreaId,
                type = viewModel.interactionType,
                interactedBy = userId,
                imageUrl = viewModel.selectedImageUrl,
                summary = binding.etSummary.text.toString().trim(),
            ))

            showToast("submitted successfully !!")
            findNavController().navigate(SubmitInteractionDirections.actionSubmitInteraction2ToActiveDay2())
        }

    }
    private fun initializeDialog(){
        dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Choose option")

        dialogBuilder.setItems(uploadOptions
        ) { dialog, which ->

            when (which) {
                0->{
                    showToast("camera")
                    askCameraPermissions()
                }
                1 -> {
                    showToast("gallery")
                    val gallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, GALLERY_REQUEST_CODE)
                }
            }
        }
        uploadOptionsDialog = dialogBuilder.create()

    }
    override fun subscribeObservers() {
        viewModel.userId.observe(viewLifecycleOwner,{
            userId = it
        })

        viewModel.isInteractionAdded.observe(viewLifecycleOwner, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it){
                            showToast("submitted successfully !!")
                            findNavController().navigate(SubmitInteractionDirections.actionSubmitInteraction2ToActiveDay2())
                        }
                    }
                    displayLoading(false)
                }
                Status.LOADING -> {
                    displayLoading(true)
                }
                Status.ERROR -> {
                    displayLoading(false)
                    _result.message?.let {
                        displayError(it)
                    }
                }
            }
        })
    }

    private fun populateTypeAdapter(){
        val types = listOf("call","visit")

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, types)
        binding.actvInteractionType.threshold = 1 //start searching for values after typing first character
        binding.actvInteractionType.setAdapter(arrayAdapter)

        binding.actvInteractionType.setOnItemClickListener { parent, view, position, id ->
            val area = parent.getItemAtPosition(position) as String
            viewModel.interactionType=area
        }
    }


    private fun askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERM_CODE
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera Permission is Required to Use camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val f = File(currentPhotoPath)
                binding.imgSelectedImage.isVisible = true
                binding.imgSelectedImage.setImageURI(Uri.fromFile(f))
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f))
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri: Uri = Uri.fromFile(f)
                mediaScanIntent.data = contentUri
                requireActivity().sendBroadcast(mediaScanIntent)
                uploadImageToFirebase(f.getName(), contentUri)
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val contentUri: Uri? = data!!.data
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri!!)
                Log.d("tag", "onActivityResult: Gallery Image Uri:  $imageFileName")
                binding.imgSelectedImage.isVisible = true
                binding.imgSelectedImage.setImageURI(contentUri)
                uploadImageToFirebase(imageFileName, contentUri)
            }
        }
    }

    private fun getFileExt(contentUri: Uri): String? {
        val c: ContentResolver = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c.getType(contentUri))
    }


    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        //        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }


    private fun uploadImageToFirebase(name: String, contentUri: Uri) {
        val image: StorageReference = storageReference!!.child("pictures/$name")
        image.putFile(contentUri).addOnSuccessListener {
            image.downloadUrl.addOnSuccessListener { uri ->
                viewModel.selectedImageUrl = uri.toString()
                Log.d(
                    "tag",
                    "onSuccess: Uploaded Image URl is $uri"
                )
            }
            Toast.makeText(requireContext(), "Image Is Uploaded.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(OnFailureListener {
            Toast.makeText(
                requireContext(),
                "Upload Failled.",
                Toast.LENGTH_SHORT
            ).show()
        })
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.jmm.brsap.meditell.android.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }


}