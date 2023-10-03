package com.example.playlistmaker.presentation.playlistsCreation

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerActivity
import com.example.playlistmaker.presentation.main.RootActivity
import com.example.playlistmaker.presentation.playlistDetails.PlaylistDetailsFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


class PlaylistCreationFragment : Fragment() {


    private val viewModel: PlaylistCreationViewModel by viewModel { parametersOf(requireActivity() as AppCompatActivity) }
    private lateinit var binding: FragmentPlaylistCreationBinding
    private var isPhotoSelected = false
    private var urlImageForNewPlaylist: String? = null
    private var editablePlaylist: Playlist? = null
    private var navigateBack: Boolean = false
    private var finalUrl:String? = null
    private var uniqueID:String?=null

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
        binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        editablePlaylist = arguments?.getSerializable("EDIT_PLAYLIST") as? Playlist

        setupListeners()
        setupTextChangeListener()
        setupViewModelObservers()
        editablePlaylist?.let { setupUiEditMode(it) }

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
        binding.frameForImage.setOnClickListener {
            chooseAndUploadImage()

        }
        binding.btCreate.setOnClickListener {
            if (editablePlaylist == null) {
                createNewPlaylist()
            } else {
                lifecycleScope.launch {
                    editPlaylist(editablePlaylist!!)
                }
            }
        }
    }

    private fun setupViewModelObservers() {
        viewModel.imagePathLiveData.observe(viewLifecycleOwner, Observer { url ->
            finalUrl = url
        })
    }
    private fun isAnyFieldNotEmpty(): Boolean {
        return binding.editTextName.text?.isNotEmpty() == true || binding.editTextDetails.text?.isNotEmpty() == true
    }

    private fun setupUiEditMode(playlist: Playlist) {
        binding.textHeader.text = "Редактировать"
        binding.editTextName.setText(playlist.name)
        binding.editTextDetails.setText(playlist.details)
        binding.btCreate.text = "Сохранить"
        if (playlist.imagePath!=null) {
            Glide.with(this)
                .load(playlist.imagePath)
                .into(binding.frameForImage)
            binding.icAddImage.visibility = View.GONE
        } else {
            binding.frameForImage.visibility = View.VISIBLE
            binding.icAddImage.visibility = View.VISIBLE
        }


        binding.icBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }




    }




    // functions creating new playlist
    private fun handleSelectedImage(uri: Uri) {
        binding.frameForImage.setImageURI(uri)
        binding.icAddImage.visibility = View.GONE
        isPhotoSelected = true
        viewModel.saveImage(uri)
    }
    private fun createNewPlaylist() {
        val playlistName = binding.editTextName.text.toString()

        if (isPhotoSelected) {
                lifecycleScope.launch {
                    viewModel.createNewPlaylist(
                        playlistName,
                        binding.editTextDetails.text.toString(),
                        finalUrl,
                        null,
                        0
                    )
                }
        } else {
            lifecycleScope.launch {
                viewModel.createNewPlaylist(
                    playlistName,
                    binding.editTextDetails.text.toString(),
                    null,
                    null,
                    0
                )
            }
        }
        showToastPlaylistCreated(playlistName)
        navigateBackAfterCreatingPlaylist()
    }


    private suspend fun editPlaylist(playlist: Playlist) {
        val updatedName = binding.editTextName.text.toString()
        val updatedDetails = binding.editTextDetails.text.toString()

        val updatedIdOfTracks = if (playlist.idOfTracks?.isEmpty() == true) null else playlist.idOfTracks
        val updatedNumberOfTracks = if (playlist.numberOfTracks == 0) null else playlist.numberOfTracks

        val updatedPlaylist = playlist.copy(
            name = updatedName,
            details = updatedDetails,
            imagePath = finalUrl,
            idOfTracks = updatedIdOfTracks,
            numberOfTracks = updatedNumberOfTracks
        )
        viewModel.editPlaylist(updatedPlaylist)
        val bundle = Bundle()
        bundle.putSerializable("playlist", updatedPlaylist)
        parentFragmentManager.setFragmentResult("playlist", bundle)

        findNavController().popBackStack()
    }



    private fun chooseAndUploadImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//
//        uniqueID = UUID.randomUUID().toString()
//        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
//        val file = File(filePath, "cover_$uniqueID.jpg")
//        finalUrl = file.absolutePath
    }

//    private fun saveImageToPrivateStorage(uri: Uri) {
//        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
//        if (!filePath.exists()){
//            filePath.mkdirs()
//        }
//        val file = File(filePath, "cover_$uniqueID.jpg")
//        val inputStream = requireActivity().contentResolver.openInputStream(uri)
//        val outputStream = FileOutputStream(file)
//        BitmapFactory
//            .decodeStream(inputStream)
//            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
//    }



    private fun showToastPlaylistCreated(playlistName: String) {
        val message = getString(R.string.playlist_created, playlistName)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }



    // navigation part
    fun navigateBack() {
            if (activity is RootActivity) {
                if (isAnyFieldNotEmpty() || isPhotoSelected) {
                    showBackConfirmationDialog()
                } else {
                    backNavigationListenerRoot?.onNavigateBack(true)
                }
            } else if (activity is AudioPlayerActivity) {
                if (isAnyFieldNotEmpty() || isPhotoSelected) {
                    showBackConfirmationDialog()
                } else {
                    backNavigationListenerAudio?.onNavigateBack(true)
                }
            }

        }

    private fun navigateBackAfterCreatingPlaylist() {
        if (activity is RootActivity) {
            backNavigationListenerRoot?.onNavigateBack(true)
        } else if (activity is AudioPlayerActivity) {
            backNavigationListenerAudio?.onNavigateBack(true)
            navigateBack = true

        }
    }

       fun checkIfCouldBeClosed(): Boolean {
           if (isAnyFieldNotEmpty() || isPhotoSelected) {
               showBackConfirmationDialog()
               return false
           } else {
               return true
           }
       }



    private fun showBackConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext(),R.style.DialogStyle)
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

