package com.example.playlistmaker.presentation.playlistsCreation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.FileOutputStream

class PlaylistCreationFragment : Fragment() {

    private val viewModel: PlaylistCreationViewModel by viewModel { parametersOf(requireActivity() as AppCompatActivity) }
    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding get() = _binding!!
    private var isPhotoSelected = false


    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.frameForImage.setImageURI(uri)
                binding.icAddImage.visibility = View.GONE
                isPhotoSelected = true
                saveImageToPrivateStorage(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)

        setupTextChangeListener()
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.icBackArrow.setOnClickListener {
            viewModel.showBackConfirmationDialog(isAnyFieldNotEmpty() || isPhotoSelected)
        }
        binding.frameForImage.setOnClickListener {
            chooseAndUploadImage()
        }
        binding.btCreate.setOnClickListener {
            createNewPlaylist()
        }
    }

    // Check if any fields are not empty
    private fun isAnyFieldNotEmpty(): Boolean {
        val name = binding.editTextName.text.toString()
        val details = binding.editTextDetails.text.toString()
        return name.isNotEmpty() || details.isNotEmpty()
    }

    // Check the status of "create" button
    private fun setupTextChangeListener() {
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.btCreate.isEnabled = s?.isNotEmpty() == true


            }
        })
    }

    private fun createNewPlaylist() {
        viewModel.renameImageFile(binding.editTextName.text.toString())
        lifecycleScope.launch {
            viewModel.createNewPlaylist(
                binding.editTextName.text.toString(),
                binding.editTextDetails.text.toString(),
                getImageUrlFromStorage(binding.editTextName.text.toString()),
                null,
                0
            )
            showToastAndNavigateBack()
        }
    }

    private fun getImageUrlFromStorage(playlistName: String): String {
        return viewModel.getImageUrlFromStorage(playlistName)
    }

    private fun chooseAndUploadImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val file = ImageStorageHelper.getTemporaryImageFile(requireContext())
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private suspend fun showToastAndNavigateBack() {
        val playlistName = binding.editTextName.text.toString()
        Toast.makeText(requireContext(), "Плейлист '$playlistName' создан", Toast.LENGTH_SHORT).show()
        viewModel.navigatingBack(requireActivity() as AppCompatActivity)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
