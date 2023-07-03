package com.example.playlistmaker.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.settings.SettingViewModel
import com.example.playlistmaker.util.Creator



class MainActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = applicationContext


        viewModel = ViewModelProvider(this, MainViewModel.getViewModelFactory(context))[MainViewModel::class.java]


        binding.settings?.setOnClickListener {

            viewModel.toSettingsScreen() }

        binding.library?.setOnClickListener {

            viewModel.toLibraryScreen() }

        binding.search?.setOnClickListener {

            viewModel.toSearchScreen() }

    }
}

