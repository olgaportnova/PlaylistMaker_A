package com.example.playlistmaker.presentation.playlistsCreation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerActivity
import com.example.playlistmaker.presentation.main.RootActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            uri?.let {
                handleSelectedImage(it)
            } ?: run {}
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)

        setupListeners()
        setupTextChangeListener()



        return binding.root
    }

    private fun setupListeners() {

        binding.icBackArrow.setOnClickListener {
            lifecycleScope.launch {
                navigateBackIfRequired()
            }
        }
        binding.frameForImage.setOnClickListener { chooseAndUploadImage() }
        binding.btCreate.setOnClickListener { createNewPlaylist() }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    private suspend fun navigateBackIfRequired() {
        if (isAnyFieldNotEmpty() || isPhotoSelected) {
            showBackConfirmationDialog()
        } else {
            navigateBack()
        }
    }

    private fun showBackConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(context?.getString(R.string.dialog_title))
            .setMessage(context?.getString(R.string.dialog_message))
            .setNeutralButton(context?.getString(R.string.dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.dialog_finish)) { _, _ ->
                lifecycleScope.launch {
                    navigateBack()
                }
            }
            .show()
    }

    private fun isAnyFieldNotEmpty(): Boolean {
        return binding.editTextName.text?.isNotEmpty() == true || binding.editTextDetails.text?.isNotEmpty() == true
    }





    private fun setupTextChangeListener() {

        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.btCreate.isEnabled = s?.isNotEmpty() == true
            }
        })
    }



    private fun handleSelectedImage(uri: Uri) {
        binding.frameForImage.setImageURI(uri)
        binding.icAddImage.visibility = View.GONE
        isPhotoSelected = true
        saveImageToPrivateStorage(uri)
    }

    private fun createNewPlaylist() {
        val playlistName = binding.editTextName.text.toString()

        viewModel.renameImageFile(playlistName)
        lifecycleScope.launch {
            viewModel.createNewPlaylist(
                playlistName,
                binding.editTextDetails.text.toString(),
                viewModel.getImageUrlFromStorage(playlistName),
                null,
                0
            )
            showToastPlaylistCreated(playlistName)
            navigateBack()
        }
    }

    private fun chooseAndUploadImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showToastPlaylistCreated(playlistName: String) {
        val message = getString(R.string.playlist_created, playlistName)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun saveImageToPrivateStorage(uri: Uri) {
        val file = ImageStorageHelper.getTemporaryImageFile(requireContext())
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }


    private suspend fun navigateBack() {
        if (activity is RootActivity) {
            requireActivity().onBackPressed()
        } else if (activity is AudioPlayerActivity) {
            (requireActivity() as AudioPlayerActivity).methodToCallFromFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
