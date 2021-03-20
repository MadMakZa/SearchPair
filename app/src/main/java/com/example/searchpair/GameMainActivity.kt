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
        bindingClass.btnLevel7.setOnClickListener{
            val intent = Intent(this, Level7::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel8.setOnClickListener{
            val intent = Intent(this, Level8::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel9.setOnClickListener{
            val intent = Intent(this, Level9::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel10.setOnClickListener{
            val intent = Intent(this, Level10::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel11.setOnClickListener{
            val intent = Intent(this, Level11::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel12.setOnClickListener{
            val intent = Intent(this, Level12::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel13.setOnClickListener{
            val intent = Intent(this, Level13::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel14.setOnClickListener{
            val intent = Intent(this, Level14::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel15.setOnClickListener{
            val intent = Intent(this, Level15::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
        bindingClass.btnLevel16.setOnClickListener{
            val intent = Intent(this, Level16::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_activity, R.anim.close_activity)

        }
    }

}