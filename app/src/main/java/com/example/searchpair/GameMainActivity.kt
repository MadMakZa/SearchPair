package com.example.searchpair

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import com.example.searchpair.databinding.ActivityGameMainBinding
import java.util.ArrayList
import java.util.logging.Level

class GameMainActivity : AppCompatActivity() {

    private var arrayImageViewsButtons = ArrayList<ImageView?>()
    private lateinit var bindingClass: ActivityGameMainBinding
    private lateinit var btnNewGame: ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityGameMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        startNewGame()
        chooseLevel()

    }

    //начать новую игру по нажатию на кнопку
    private fun startNewGame() {
        bindingClass.btnNewGame.setOnClickListener {
            val intentStart = Intent(this, Level1::class.java)
            startActivity(intentStart)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)
            finish()
        }
    }
    //выбор уровня
    private fun chooseLevel(){
        bindingClass.btnLevel1.setOnClickListener{
            val intent = Intent(this, Level1::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel2.setOnClickListener{
            val intent = Intent(this, Level2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel3.setOnClickListener{
            val intent = Intent(this, Level3::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel4.setOnClickListener{
            val intent = Intent(this, Level4::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel5.setOnClickListener{
            val intent = Intent(this, Level5::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel6.setOnClickListener{
            val intent = Intent(this, Level6::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
    }

}