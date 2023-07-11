package com.example.playlistmaker.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
 //   private lateinit var context: Context
    private lateinit var binding: ActivityMainBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  context = applicationContext



        binding.settings?.setOnClickListener {

            viewModel.toSettingsScreen() }

        binding.library?.setOnClickListener {

            viewModel.toLibraryScreen() }

        binding.search?.setOnClickListener {

            viewModel.toSearchScreen() }

    }
}

