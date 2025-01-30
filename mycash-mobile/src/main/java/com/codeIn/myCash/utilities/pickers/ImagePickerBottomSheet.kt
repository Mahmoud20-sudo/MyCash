package com.codeIn.myCash.utilities.pickers

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.codeIn.common.util.Compressor
import com.codeIn.common.util.gone
import com.codeIn.common.util.invisible
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.BottomSheetImagePickerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ImagePickerBottomSheet(
    private val fromCamera: Boolean = false,
    private val fromGallery: Boolean = false,
    private val attachment: Uri? = null,
    private val communicator: Communicator
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetImagePickerBinding
    private var imageUri: Uri? = attachment

    private var image: File? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetImagePickerBinding.inflate(layoutInflater)
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED
        setAttachment()

        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onResume() {
        super.onResume()

        // dismiss the bottom sheet
        binding.backButton.setOnClickListener {
            dismiss()
        }

        // delete the image
        binding.deleteImageButton.setOnClickListener {
            binding.uploadingImageLayout.attachmentsButton.visible()
            binding.uploadingImageLayout.imageImageView.invisible()
            binding.deleteImageButton.gone()
            imageUri = attachment
            setAttachment()
        }

        // select the image source
        binding.uploadingImageLayout.attachmentsButton.setOnClickListener { selectSource() }

        // confirm the image
        binding.confirmButton.setOnClickListener {
            // check if the image is null or not and send it to the caller if not null and dismiss the bottom sheet
            if (image != null) {
                communicator.setImage(image!!)
                dismiss()
                return@setOnClickListener
            }
            dismiss()
        }
    }

    private fun setAttachment() {
        binding.uploadingImageLayout.imageImageView.visible()
        Glide.with(requireContext()).load(imageUri).into(binding.uploadingImageLayout.imageImageView)
    }

    // Request codes
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private val CAPTURE_IMAGE_REQUEST = 3

    /**
     *    to select the image source (camera or gallery)
     *    it checks if the caller passed a specific source (camera or gallery) or not
     *    if both are the same (false or true), the user will be able to select an image from either the camera or the gallery with a dialog to choose from
     */
    private fun selectSource() {

        if (fromCamera && !fromGallery) {
            openCamera()
        } else if (fromGallery && !fromCamera) {
            // open gallery
            openGallery()
        } else {
            // open dialog to choose from
            openDialog()
        }


    }

    /**
     *   to open a dialog to choose from camera or gallery
     *   if the user selects camera, the camera will be opened
     *   if the user selects gallery, the gallery will be opened
     */
    private fun openDialog() {
        // setup the dialog options Array Take Photo, Choose from Gallery, Cancel
        val options = arrayOf<String>(
            context?.getString(R.string.takeImage) ?: "",
            context?.getString(R.string.choose_from_gallery) ?: "",
            context?.getString(R.string.cancel) ?: ""
        )

        // create the dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(context?.getString(R.string.select_option))

        // set the dialog options and handle the user selection
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == context?.getString(R.string.takeImage) -> {
                    openCamera()
                }

                options[item] == context?.getString(R.string.choose_from_gallery) -> {
                    openGallery()
                }

                options[item] == context?.getString(R.string.cancel) -> {
                    dialog.dismiss()
                }
            }
        }

        // show the dialog
        builder.show()
    }

    /**
     *   to open the gallery to select an image from it.
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    /**
     *   to open the camera to take a photo.
     */
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            val values = ContentValues()
            values.put(Images.Media.TITLE, "New Picture")
            values.put(Images.Media.DESCRIPTION, "From your Camera")
            imageUri = requireContext().contentResolver.insert(
                Images.Media.EXTERNAL_CONTENT_URI, values
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            Log.e(TAG, "openCamera: $e")
        }

    }


    /**
     *   to handle the result of the camera or the gallery
     *   if the result is ok, the image will be displayed and the delete button will be visible
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            binding.apply {
                uploadingImageLayout.attachmentsButton.invisible()
                uploadingImageLayout.imageImageView.visible()
                binding.deleteImageButton.visible()
            }

            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, imageUri
                    )
                    save("hello", imageBitmap)
                }

                REQUEST_IMAGE_GALLERY -> {
                    val imageUri = data?.data
                    val imageBitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        imageUri
                    )
                    save("hello", imageBitmap)
                }
            }
        }
    }


    private fun generateUniqueName(imageName: String?, returnFullPath: Boolean): String? {
        val sdf = SimpleDateFormat("yyyyMMddsshhmmss", Locale.getDefault())
        val date: String = sdf.format(Date())
        val filename = String.format("%s_%s.jpg", imageName, date)
        return if (returnFullPath) {
            val directory = context?.getDir("zest", Context.MODE_PRIVATE)
            String.format("%s/%s", directory, filename)
        } else {
            filename
        }
    }

    private fun save(name: String?, bitmapImage: Bitmap): String {
        val uniqueNamePath = generateUniqueName(name, true)
        val path = File(uniqueNamePath)
        val compressedFile = Compressor.saveBitmapToFile(path, bitmapImage) ?: path
        image = File(getPathFromURI(requireContext(), compressedFile.toUri()))
        setAttachment()
        return compressedFile.toString()
    }

    interface Communicator {
        fun setImage(file: File)
    }

    open fun getPathFromURI(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}