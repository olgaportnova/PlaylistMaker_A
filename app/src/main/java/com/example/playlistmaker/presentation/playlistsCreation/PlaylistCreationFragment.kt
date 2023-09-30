package com.example.playlistmaker.presentation.playlistsCreation

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
    private var urlImageForNewPlaylist: String? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                handleSelectedImage(it)
            } ?: run {}
        }

    private var backNavigationListenerRoot: BackNavigationListenerRoot? = null
    private var backNavigationListenerAudio: BackNavigationListenerAudioPlayer? = null

    // lifecycle functions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BackNavigationListenerRoot) {
            backNavigationListenerRoot = context
        }
        if (context is BackNavigationListenerAudioPlayer) {
            backNavigationListenerAudio = context
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onDetach() {
        super.onDetach()
        backNavigationListenerRoot = null
        backNavigationListenerAudio = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)

        setupListeners()
        setupTextChangeListener()
        setupViewModelObservers()

        return binding.root

    }




    // utils functions
    private fun setupTextChangeListener() {

        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                binding.btCreate.isEnabled = s?.isNotEmpty() == true
            }
        })
    }
    private fun setupListeners() {
        binding.icBackArrow.setOnClickListener {
            lifecycleScope.launch {
                navigateBack()
            }
        }
        binding.frameForImage.setOnClickListener { chooseAndUploadImage() }
        binding.btCreate.setOnClickListener { createNewPlaylist() }
    }
    private fun setupViewModelObservers() {
        viewModel.getImageUrlLiveData().observe(viewLifecycleOwner, Observer { url ->
            urlImageForNewPlaylist = url
        })
    }
    private fun isAnyFieldNotEmpty(): Boolean {
        return binding.editTextName.text?.isNotEmpty() == true || binding.editTextDetails.text?.isNotEmpty() == true
    }




    // functions creating new playlist
    private fun handleSelectedImage(uri: Uri) {
        binding.frameForImage.setImageURI(uri)
        binding.icAddImage.visibility = View.GONE
        isPhotoSelected = true
        saveImageToPrivateStorage(uri)
    }
    private fun createNewPlaylist() {
        val playlistName = binding.editTextName.text.toString()

        viewModel.renameImageFile(playlistName)
        viewModel.getImageUrlFromStorage(playlistName)
        lifecycleScope.launch {
            viewModel.createNewPlaylist(
                playlistName,
                binding.editTextDetails.text.toString(),
                null,
                0
            )
            viewModel.getImageUrlFromStorage(playlistName)
            showToastPlaylistCreated(playlistName)
            navigateBackAfterCreatingPlaylist()
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



    // navigation part
    fun navigateBack() {
        if (activity is RootActivity) {
            if (isAnyFieldNotEmpty() || isPhotoSelected) {
                showBackConfirmationDialog()
            } else {
                backNavigationListenerRoot?.onNavigateBack(true)
            }
        }
        else if (activity is AudioPlayerActivity) {
            if (isAnyFieldNotEmpty() || isPhotoSelected) {
                showBackConfirmationDialog() } else {
                backNavigationListenerAudio?.onNavigateBack(true) }
        }
    }
    private fun navigateBackAfterCreatingPlaylist() {
        if (activity is RootActivity) {
            backNavigationListenerRoot?.onNavigateBack(true)
        }
        else if (activity is AudioPlayerActivity) {
            backNavigationListenerAudio?.onNavigateBack(true) }
    }
    private fun showBackConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(context?.getString(R.string.dialog_title))
            .setMessage(context?.getString(R.string.dialog_message))
            .setNeutralButton(context?.getString(R.string.dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.dialog_finish)) { _, _ ->
                if(requireActivity() is RootActivity) {
                    lifecycleScope.launch {
                        backNavigationListenerRoot?.onNavigateBack(true)
                    }
                }
                if (requireActivity() is AudioPlayerActivity){
                    backNavigationListenerAudio?.onNavigateBack(true)
                }

            }
            .show()
            .apply {
                getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3772E7"))
                getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#3772E7"))
            }
    }



}

