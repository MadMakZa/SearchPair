package com.example.searchpair

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.searchpair.databinding.ActivityGameMainBinding

class GameMainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityGameMainBinding
    private lateinit var btnNewGame: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityGameMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        btnNewGame = findViewById(R.id.btn_new_game)
        startNewGame()
    }

    //начать новую игру по нажатию на кнопку
    private fun startNewGame() {
        btnNewGame.setOnClickListener {
            val intent = Intent(this, Level5::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
            finish()
        }
    }

}