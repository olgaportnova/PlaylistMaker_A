package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val itunesBaseUrl = "https://itunes.apple.com"

private val retrofit = Retrofit.Builder()
    .baseUrl(itunesBaseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val itunesService = retrofit.create(Itunes::class.java)

var songs = ArrayList<Song>()


class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    private val adapter = TrackAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        val frameLayout = findViewById<FrameLayout>(R.id.container)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val buttonBack = findViewById<View>(R.id.back)
        val rcTrackList = findViewById<View>(R.id.rcTrackList)
        val placeholderMessage = findViewById<View>(R.id.placeholderMessage)


        buttonBack.setOnClickListener {
            finish()
        }


        fun showMessage(text: String, image: Int) {
            if (text.isNotEmpty()) {
                placeholderMessage.visibility = View.VISIBLE
                binding.imageNothingFound.setImageResource(image)
                binding.textNothingFound.text = text
                songs.clear()
                adapter.notifyDataSetChanged()


            } else {
                placeholderMessage.visibility = View.GONE
            }

        }

                inputEditText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (inputEditText.text.isNotEmpty()) {

                            itunesService.search(inputEditText.text.toString())
                                .enqueue(object : Callback<SongsResponse> {
                                    override fun onResponse(
                                        call: Call<SongsResponse>,
                                        response: Response<SongsResponse>
                                    ) {
                                        if (response.code() == 200) {
                                            songs.clear()
                                            if (response.body()?.results?.isNotEmpty() == true) {
                                                songs.addAll(response.body()?.results!!)
                                                adapter.notifyDataSetChanged()
                                            }
                                            if (songs.isEmpty()) {
                                                showMessage(
                                                    getString(R.string.nothing_found),
                                                    R.drawable.nothing_found
                                                )
                                            } else {
                                                showMessage("", R.drawable.something_wrong)
                                                binding.buttonUpdate.visibility = View.VISIBLE
                                                itunesService.search(inputEditText.text.toString())
                                            }
                                        } else {
                                            showMessage(
                                                getString(R.string.something_went_wrong),
                                                R.drawable.something_wrong
                                            )
                                            binding.buttonUpdate.visibility = View.VISIBLE
                                            itunesService.search(inputEditText.text.toString())

                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<SongsResponse>,
                                        t: Throwable
                                    ) {
                                        showMessage(
                                            getString(R.string.something_went_wrong),
                                            R.drawable.something_wrong
                                        )
                                        binding.buttonUpdate.visibility = View.VISIBLE
                                        binding.buttonUpdate.setOnClickListener {
                                            itunesService.search(inputEditText.text.toString())
                                        }

                                    }
                                })
                        }
                        true
                    }
                    false
                }


        clearButton.setOnClickListener {
            inputEditText.setText("")
            songs.clear()
            adapter.notifyDataSetChanged()
            binding.placeholderMessage.visibility = View.INVISIBLE



        }
        val editText = inputEditText.text
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {


                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun init() {
        binding.apply {
            rcTrackList.layoutManager = LinearLayoutManager(this@SearchActivity)
            rcTrackList.adapter = adapter
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        outState.putString(SEARCH_TYPE, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.text = savedInstanceState.getString(SEARCH_TYPE) as Editable
    }

    companion object {
        const val SEARCH_TYPE = "SEARCH_TYPE"
    }


}