package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.R.layout;
import android.content.Intent



class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSettings=findViewById<View>(R.id.settings)
        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }
        val buttonLibrary=findViewById<View>(R.id.library)
        buttonLibrary.setOnClickListener {
            val displayIntent = Intent(this, library::class.java)
            startActivity(displayIntent)
        }
        val buttonSearch=findViewById<View>(R.id.search)
        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, Search::class.java)
            startActivity(displayIntent)
        }
//123
//        val buttonSearch=findViewById<View>(R.id.search)
//        val buttonLibrary=findViewById<View>(R.id.library)
//        val buttonSettings=findViewById<View>(R.id.settings)
//        val buttonSearchClickListener: View.OnClickListener = object: View.OnClickListener {
//            override fun onClick (v: View) {
//                Toast.makeText(this@MainActivity,"Нажали на кнопку Поиск", Toast.LENGTH_SHORT).show()
//            }
//        }
//        val buttonLibraryClickListener: View.OnClickListener = object: View.OnClickListener {
//            override fun onClick (v: View) {
//                Toast.makeText(this@MainActivity,"Нажали на кнопку Медиатека", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        buttonSearch.setOnClickListener(buttonSearchClickListener)
//        buttonLibrary.setOnClickListener(buttonLibraryClickListener)
//
//
//        buttonSearch.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск", Toast.LENGTH_SHORT).show()
//        }
//        buttonLibrary.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажали на кнопку Медиатека", Toast.LENGTH_SHORT).show()
//        }
//        buttonSettings.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажали на кнопку Настройки", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun click_search(view: View) {
//        val search_buttom = Toast.makeText(this, "Вы нажали поиск", Toast.LENGTH_LONG)
//        search_buttom.show()
//
//    }
//
//
//    fun click_library(view: View) {
//        val library_buttom = Toast.makeText(this, "Вы нажали медиатика", Toast.LENGTH_LONG)
//        library_buttom.show()
//

    }
}

